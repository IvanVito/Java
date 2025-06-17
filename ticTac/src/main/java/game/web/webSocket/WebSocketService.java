package game.web.webSocket;

import game.abstractModels.AbstractModel;
import game.datasource.model.DataGame;
import game.datasource.model.repository.DataRepository;
import game.datasource.user.model.UserData;
import game.domain.model.DomainGame;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.*;

public class WebSocketService extends TextWebSocketHandler {
    private final DataRepository dataRepository;
    private final Map<String, Boolean> updateAfterConnected = new ConcurrentHashMap<>();
    private final Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();
    private final Map<String, Long> timerForDelete = new ConcurrentHashMap<>();
    private final Map<String, ScheduledExecutorService> gameSchedulers = new ConcurrentHashMap<>();

    public WebSocketService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String gameId = getGameIdFromSession(session);
        Optional<DataGame> gameOpt = dataRepository.findById(gameId);
        if (gameOpt.isEmpty()) return;

        timerForDelete.remove(gameId);
        sessions.putIfAbsent(gameId, new CopyOnWriteArrayList<>());
        sessions.get(gameId).add(session);

        if (!updateAfterConnected.getOrDefault(gameId, false) && sessions.get(gameId).size() == 2) {
            startGame(gameId);
            updateHtml(gameId);
            updateAfterConnected.put(gameId, true);
            ScheduledExecutorService scheduler = gameSchedulers.remove(gameId);
            if (scheduler != null)
                scheduler.shutdown();
        }
    }

    public void updateHtml(String gameId) throws Exception {
        List<WebSocketSession> sessionsForGame = sessions.get(gameId);
        if (sessionsForGame != null) {
            List<WebSocketSession> copySessions = new ArrayList<>(sessionsForGame);
            for (WebSocketSession session : copySessions) {
                if (!session.isOpen()) {
                    sessionsForGame.remove(session);
                } else {
                    session.sendMessage(new TextMessage("update game state"));
                }
            }
        }
    }

    private String getGameIdFromSession(WebSocketSession session) {
        return session.getUri().getPath().replaceAll(".*/", "");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String gameId = getGameIdFromSession(session);
        List<WebSocketSession> currentSessions = sessions.get(gameId);
        if (currentSessions != null) {
            synchronized (currentSessions) {
                currentSessions.remove(session);
                if (currentSessions.isEmpty()) {
                    sessions.remove(gameId);
                    timerForDelete.put(gameId, System.currentTimeMillis());
                }
            }
            outHandler(gameId);
        }
    }

    void outHandler(String gameId) {
        gameSchedulers.putIfAbsent(gameId, Executors.newSingleThreadScheduledExecutor());
        ScheduledExecutorService scheduler = gameSchedulers.get(gameId);
        scheduler.schedule(() -> {
            synchronized (sessions) {
                handleGameStateUpdate(gameId);
            }
        }, 5, TimeUnit.SECONDS);
    }

    private void handleGameStateUpdate(String gameId) {
        Optional<DataGame> gameOpt = dataRepository.findById(gameId);
        if (gameOpt.isEmpty()) return;
        List<WebSocketSession> currentSessions = sessions.get(gameId);

        if (currentSessions == null || currentSessions.isEmpty()) {
            if (timerForDelete.containsKey(gameId) &&
                    System.currentTimeMillis() - timerForDelete.get(gameId) >= 2000) {
                gameOpt.ifPresent(dataRepository::delete);
                updateAfterConnected.remove(gameId);
                stopGameScheduler(gameId);
            }
        } else if (currentSessions.size() == 1 && updateAfterConnected.get(gameId)) {
            gameOpt.ifPresent(game -> {
                game.setGameState(AbstractModel.GameState.WAITING);
                dataRepository.save(game);
                updateAfterConnected.put(gameId, false);
                try {
                    updateHtml(gameId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void startGame(String gameId) throws Exception {
        Optional<DataGame> gameOpt = dataRepository.findById(gameId);
        if (gameOpt.isEmpty()) return;
        DataGame game = gameOpt.get();
        game.setGameState(AbstractModel.GameState.PLAYER_TURN);
        dataRepository.save(game);
    }

    private void stopGameScheduler(String gameId) {
        ScheduledExecutorService scheduler = gameSchedulers.remove(gameId);
        if (scheduler != null)
            scheduler.shutdown();
    }

    public DomainGame connectPersons (DomainGame domainGame, UserData UserData, String id, HttpSession session) throws Exception {
        if (!domainGame.getWhoPlayer().equals("H")) return domainGame;
        String currentUuid = UserData.getUuid().toString();
        boolean update = false;

        if (domainGame.getNameFirst().isEmpty()) {
            if (domainGame.getUuidPlayer2() == null || !domainGame.getUuidPlayer2().equals(currentUuid)) {
                domainGame.setNameFirst((String) session.getAttribute("name"));
                domainGame.setDestinyFirst(domainGame.getDestinySecond() == 'X' ? 'O' : 'X');
                domainGame.setUuidPlayer1(currentUuid);
                update = true;
            }
        }

        if (domainGame.getNameSecond().isEmpty()) {
            if (domainGame.getUuidPlayer1() == null || !domainGame.getUuidPlayer1().equals(currentUuid)) {
                domainGame.setNameSecond((String) session.getAttribute("name"));
                domainGame.setDestinySecond(domainGame.getDestinyFirst() == 'X' ? 'O' : 'X');
                domainGame.setUuidPlayer2(currentUuid);
                update = true;
            }
        }

        if(update) {
            session.setAttribute("uuid", currentUuid);
            dataRepository.save(game.datasource.mapper.Mapper.domainToData(domainGame));
            updateHtml(id);
        }
        return domainGame;
    }
}

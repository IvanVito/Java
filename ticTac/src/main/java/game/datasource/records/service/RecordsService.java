package game.datasource.records.service;

import game.abstractModels.AbstractModel;
import game.datasource.records.model.Records;
import game.datasource.records.repository.RecordsRepository;
import game.domain.model.DomainGame;
import game.domain.service.logicService;
import game.web.webSocket.WebSocketService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecordsService {
    private final RecordsRepository recordsRepository;
    private final WebSocketService webSocketService;

    public RecordsService(RecordsRepository recordsRepository, WebSocketService webSocketService) {
        this.recordsRepository = recordsRepository;
        this.webSocketService = webSocketService;
    }

    public Records saveRecord(Records record) {
        return recordsRepository.save(record);
    }

    public Records getOrCreateRecord(String userId) {
        Optional<Records> record = recordsRepository.findById(userId);
        if (record.isEmpty()) {
            Records newRecord = new Records();
            newRecord.setId(userId);
            recordsRepository.save(newRecord);
            return newRecord;
        }
        return record.get();
    }

    public void plusWin(String userId) {
        Records record = getOrCreateRecord(userId);
        record.setWins(record.getWins() + 1);
        recordsRepository.save(record);
    }

    public void plusLose(String userId) {
        Records record = getOrCreateRecord(userId);
        record.setLoses(record.getLoses() + 1);
        recordsRepository.save(record);
    }

    public void plusDraw(String userId) {
        Records record = getOrCreateRecord(userId);
        record.setDraw(record.getDraw() + 1);
        recordsRepository.save(record);
    }

    public void deleteRecord(String id) {
        recordsRepository.deleteById(id);
    }

    public void changeRecords (DomainGame domainGame, String id) throws Exception {
        if ((domainGame.getGameState() == AbstractModel.GameState.WINNER
                || domainGame.getGameState() == AbstractModel.GameState.DRAW)
                && !logicService.getProcessedGames().contains(domainGame.getUuid().toString())) {
            logicService.getProcessedGames().add(domainGame.getUuid().toString());

            if (domainGame.getGameState() == AbstractModel.GameState.WINNER) {
                if ((domainGame.getDestinyFirst() == 'X' && domainGame.getWhoWin().equals("Крестики"))
                        || (domainGame.getDestinyFirst() == 'O' && domainGame.getWhoWin().equals("Нолики"))) {
                    plusWin(domainGame.getUuidPlayer1());
                    plusLose(domainGame.getUuidPlayer2());
                } else {
                    plusWin(domainGame.getUuidPlayer2());
                    plusLose(domainGame.getUuidPlayer1());
                }
            } else if (domainGame.getGameState() == AbstractModel.GameState.DRAW) {
                plusDraw(domainGame.getUuidPlayer1());
                plusDraw(domainGame.getUuidPlayer2());
            }
            webSocketService.updateHtml(id);
        }
    }
}

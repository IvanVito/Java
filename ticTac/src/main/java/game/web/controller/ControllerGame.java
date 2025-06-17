package game.web.controller;

import game.datasource.model.DataGame;
import game.datasource.model.repository.DataRepository;
import game.datasource.records.service.RecordsService;
import game.datasource.user.model.UserData;
import game.domain.model.DomainGame;
import game.domain.service.logicService;
import game.web.model.*;
import game.web.model.mapper.Mapper;
import game.web.webSocket.WebSocketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class ControllerGame {
    private final logicService logicService;
    private final WebSocketService webSocketService;
    private final RecordsService recordsService;

    private final game.datasource.user.repository.UserRepository userRepository;
    private final DataRepository dataRepository;

    public ControllerGame(logicService gameService, WebSocketService webSocketService
            , RecordsService recordsService, game.datasource.user.repository.UserRepository userRepository,
                          DataRepository dataRepository) {
        this.logicService = gameService;
        this.webSocketService = webSocketService;
        this.recordsService = recordsService;

        this.userRepository = userRepository;
        this.dataRepository = dataRepository;

    }

    @GetMapping("/")
    public String showForm(HttpSession session, Model model) {
        model.addAttribute("name", (String) session.getAttribute("name"));
        return "game";
    }

    @GetMapping("/games")
    public String showGames(Model model) {
        List<DataGame> gameList = dataRepository.findByGameOver(false);
        gameList.removeIf(game -> game.getWhoPlayer().equals("C"));
        model.addAttribute("gamesList", gameList);
        return "games";
    }

    @PostMapping("/")
    public String startGame(@RequestParam String name,
                            @RequestParam (required = false) String destiny,
                            @RequestParam (required = false) String person,
                            @RequestParam (required = false) String action,
                            HttpSession session,
                            Model model) {
        session.setAttribute("name", name);
        if ("join".equals(action))
            return "redirect:/games";

        Optional<UserData> optUserData = userRepository.findByLogin((String)session.getAttribute("login"));
        if (optUserData.isPresent()) {
            UserData userData = optUserData.get();
            String uuidPlayer = userData.getUuid().toString();
            session.setAttribute("uuid", uuidPlayer);
            DomainGame domainGame = logicService.createGame(uuidPlayer, person, destiny, name);
            DataGame dataGame = game.datasource.mapper.Mapper.domainToData(domainGame);
            dataRepository.save(dataGame);
            return "redirect:/game/" + dataGame.getUuid();
        } else {
            return redirectToError(model, "Ошибка: Вы не найдены в базе данных. Зарегистрируйтесь заново !");
        }
    }

    @GetMapping("/game/{id}")
    public String showGameField(@PathVariable String id,
                                HttpSession session,
                                Model model) throws Exception {
        Optional<UserData> optUserData = userRepository.findByLogin((String)session.getAttribute("login"));
        Optional<DataGame> dataGame = dataRepository.findById(id);

        if(optUserData.isEmpty())
            return redirectToError(model, "Ошибка: Учетная запись не найдена!");
        if(dataGame.isEmpty())
            return redirectToError(model, "Ошибка: Игра не найдена!");

        DomainGame domainGame = game.datasource.mapper.Mapper.dataToDomain(dataGame.get());
        recordsService.changeRecords (domainGame, id);
        domainGame = webSocketService.connectPersons(domainGame, optUserData.get(), id, session);

        WebGame webGame = Mapper.domainToWeb(domainGame);
        model.addAttribute("gameId", id);
        model.addAttribute("gameField", webGame.getField().getGameField());
        model.addAttribute("gameOver", webGame.isGameOver());
        model.addAttribute("destinyFirst", webGame.getDestinyFirst());
        model.addAttribute("destinySecond", webGame.getDestinySecond());
        model.addAttribute("whoWin", webGame.getWhoWin());
        model.addAttribute("nameFirst", webGame.getNameFirst());
        model.addAttribute("nameSecond", webGame.getNameSecond());
        model.addAttribute("uuidPlayer1", webGame.getUuidPlayer1());
        model.addAttribute("uuidPlayer2", webGame.getUuidPlayer2());
        model.addAttribute("gameState", webGame.getGameState().toString());
        model.addAttribute("whoPlayer", webGame.getWhoPlayer());
        model.addAttribute("recordsPlayer1",
                recordsService.getOrCreateRecord(webGame.getUuidPlayer1()));
        model.addAttribute("recordsPlayer2",
                recordsService.getOrCreateRecord(webGame.getUuidPlayer2()));

        if(webGame.getWhoTurns().equals(webGame.getUuidPlayer1()))
            model.addAttribute("whoTurns", webGame.getDestinyFirst());
        else if (webGame.getWhoTurns().equals(webGame.getUuidPlayer2()))
            model.addAttribute("whoTurns", webGame.getDestinySecond());
        return "gameField";
    }

    @PostMapping("/game/{id}")
    public String getGameState(@PathVariable String id,
                               @RequestParam (required = false) Integer cell,
                               @RequestParam (required = false) String action,
                               Model model, HttpSession session) throws Exception {
        Optional<DataGame> dataGame = dataRepository.findById(id);
        if (dataGame.isEmpty())
            return redirectToError (model, "Ошибка: Данной игры не существует !");

        DomainGame domainGame = game.datasource.mapper.Mapper.dataToDomain(dataGame.get());
        if ("reset".equalsIgnoreCase(action)) {
            domainGame = logicService.reset(domainGame, id);
            webSocketService.updateHtml(id);
            dataRepository.save(game.datasource.mapper.Mapper.domainToData(domainGame));
            return "redirect:/game/" + id;
        }

        logicService.turnHandler(domainGame, cell, (String) session.getAttribute("uuid"));

        DataGame dataGameSave = game.datasource.mapper.Mapper.domainToData(domainGame);
        dataRepository.save(dataGameSave);
        webSocketService.updateHtml(id);

        return "redirect:/game/" + id;
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    void disableFavicon() {
    }

    public String redirectToError (Model model, String error) {
        model.addAttribute("errorMessage", error);
        return "errorPage";
    }
}


package game.web.model.mapper;

import game.domain.model.DomainGame;
import game.domain.model.DomainField;
import game.web.model.WebField;
import game.web.model.WebGame;

public class Mapper {
    public static WebGame domainToWeb (DomainGame game) {
        WebField webField = new WebField(game.getField().getGameField());
        return new WebGame(game.getUuid(), webField, game.getNameFirst(), game.getNameSecond(), game.getDestinyFirst(),
                game.getWhoWin(), game.isGameOver(), game.getGameState(), game.getWhoTurns(), game.getWhoPlayer(),
                game.getUuidPlayer1(), game.getUuidPlayer2());
    }
    public static DomainGame webToDomain (WebGame game) {
        DomainField gameField = new DomainField(game.getField().getGameField());
        return new DomainGame(game.getUuid(), gameField, game.getNameFirst(), game.getNameSecond(), game.getDestinyFirst(),
                game.getWhoWin(), game.isGameOver(), game.getGameState(), game.getWhoTurns(), game.getWhoPlayer(),
                game.getUuidPlayer1(), game.getUuidPlayer2());
    }
}
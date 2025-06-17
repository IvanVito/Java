package game.datasource.mapper;

import game.datasource.model.DataField;
import game.datasource.model.DataGame;
import game.domain.model.DomainField;
import game.domain.model.DomainGame;

public class Mapper {
    public static DataGame domainToData (DomainGame game) {
        DataField dataField = new DataField(DataField.intToString(game.getField().getGameField()));
        return new DataGame(game.getUuid(), dataField, game.getNameFirst(), game.getNameSecond(),
                game.getDestinyFirst(), game.getWhoWin(), game.isGameOver(), game.getGameState(),
                game.getWhoTurns(), game.getWhoPlayer(), game.getUuidPlayer1(), game.getUuidPlayer2());
    }

    public static DomainGame dataToDomain (DataGame game) {
        DomainField gameField = new DomainField(DataField.stringToInt(game.getGameField()));
        return new DomainGame(game.getUuid(), gameField, game.getNameFirst(), game.getNameSecond(),
                game.getDestinyFirst(), game.getWhoWin(), game.isGameOver(), game.getGameState(),
                game.getWhoTurns(), game.getWhoPlayer(), game.getUuidPlayer1(), game.getUuidPlayer2());
    }
}

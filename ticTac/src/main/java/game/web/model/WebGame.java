package game.web.model;

import java.util.UUID;
import game.abstractModels.AbstractModel;

public class WebGame extends AbstractModel<WebField> {
    public WebGame(UUID id, WebField field, String nameFirst, String nameSecond, char destinyFirst, String whoWin,
                   boolean gameOver, GameState gameState, String whoTurns, String whoPlayer, String uuidPlayer1,
                   String uuidPlayer2) {
        super(id, field, nameFirst, nameSecond, destinyFirst, whoWin, gameOver, gameState, whoTurns ,whoPlayer, uuidPlayer1,
                uuidPlayer2);
    }
}

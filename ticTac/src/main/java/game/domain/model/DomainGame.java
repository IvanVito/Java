package game.domain.model;
import game.abstractModels.AbstractModel;
import java.util.UUID;
import static game.abstractModels.Constants.*;

public class DomainGame extends AbstractModel<DomainField> {
    public DomainGame(UUID id, DomainField field, String nameFirst, String nameSecond, char destiny,
                      String whoWin, boolean gameOver, GameState gameState, String whoTurns ,String whoPlayer,
                      String uuidPlayer1, String uuidPlayer2) {
        super(id, field, nameFirst, nameSecond, destiny, whoWin, gameOver, gameState, whoTurns, whoPlayer,
                uuidPlayer1, uuidPlayer2);
    }

    public DomainGame() {
        super(UUID.randomUUID(), new DomainField(fieldRows, fieldCols), "","", ' ', "", false, GameState.WAITING, "", "", "", "");
    }
}

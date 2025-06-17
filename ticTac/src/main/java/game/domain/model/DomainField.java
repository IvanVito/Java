package game.domain.model;
import static game.abstractModels.Constants.*;

public class DomainField  {
    private int [][] gameField;

    public DomainField (int rows, int cols) {
        gameField = new int[rows][cols];
        for(int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                gameField[i][j] = 0;
            }
        }
    }

    public DomainField (int[][] gameField) {
        this.gameField = new int[fieldCols][fieldRows];
        for(int i = 0; i < fieldRows; ++i) {
            System.arraycopy(gameField[i], 0, this.gameField[i], 0, fieldCols);
        }
    }

    public int[][] getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = gameField;
    }
}


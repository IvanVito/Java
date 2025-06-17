package game.web.model;
import static game.abstractModels.Constants.*;

public class WebField {
    private int [][] gameField;

    public WebField(int[][] gameField) {
        this.gameField = new int[fieldRows][fieldCols];
        for (int i = 0; i < fieldRows; i++) {
            this.gameField[i] = gameField[i].clone();
        }
    }


    public int[][] getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = new int[fieldRows][fieldCols];
        for (int i = 0; i < fieldRows; i++) {
            this.gameField[i] = gameField[i].clone();
        }
    }
}

package game.datasource.model;

import game.abstractModels.Constants;

public class DataField {
    private String gameField;

    public DataField () {
    }

    public DataField (String gameField) {
        this.gameField = gameField;
    }

    public String getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = intToString(gameField);
    }

    public static String intToString(int[][] gameField) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : gameField) {
            for (int col : row) {
                sb.append(col).append(",");
            }
            sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1);
            sb.append(";");
        }
        return sb.toString();
    }

    public static int[][] stringToInt(String field) {
        if (field == null || field.isEmpty()) return new int[3][3];

        String[] rows = field.split(";");
        int[][] gameField = new int[Constants.fieldRows][Constants.fieldCols];

        for (int i = 0; i < Constants.fieldRows; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < Constants.fieldCols; j++) {
                gameField[i][j] = Integer.parseInt(cols[j]);
            }
        }
        return gameField;
    }
}




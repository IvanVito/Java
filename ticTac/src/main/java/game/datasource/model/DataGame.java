package game.datasource.model;

import game.abstractModels.AbstractModel;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "games")
public class DataGame {
    @Id
    public String id;

    @Column(columnDefinition = "TEXT")
    private String gameField;

    @Column(columnDefinition = "TEXT")
    private String whoWin;

    @Column(columnDefinition = "BOOLEAN")
    private boolean gameOver;

    @Column(columnDefinition = "TEXT")
    private char destinyFirst;

    @Column(columnDefinition = "TEXT")
    private char destinySecond;

    @Column(columnDefinition = "TEXT")
    private String nameFirst;

    @Column(columnDefinition = "TEXT")
    private String nameSecond;

    @Column(columnDefinition = "TEXT")
    private String whoTurns;

    @Column(columnDefinition = "TEXT")
    private String whoPlayer;

    @Column(columnDefinition = "TEXT")
    private String uuidPlayer1;

    @Column(columnDefinition = "TEXT")
    private String uuidPlayer2;

    @Column(columnDefinition = "BOOLEAN")
    private boolean readRecord;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT")
    private AbstractModel.GameState gameState;

    public DataGame(UUID id, DataField field, String nameFirst, String nameSecond, char destinyFirst, String whoWin, boolean gameOver, AbstractModel.GameState gameState,
                   String whoTurns, String whoPlayer, String uuidPlayer1, String uuidPlayer2) {
        this.id = id.toString();
        this.gameField = field.getGameField();
        this.whoWin = whoWin;
        this.gameOver = gameOver;
        this.destinyFirst = destinyFirst;
        if(this.destinyFirst == 'X') this.destinySecond = 'O';
        else destinySecond = 'X';
        this.nameFirst = nameFirst;
        this.nameSecond = nameSecond;
        this.whoTurns = whoTurns;
        this.whoPlayer = whoPlayer;
        this.uuidPlayer1 = uuidPlayer1;
        this.uuidPlayer2 = uuidPlayer2;
        this.gameState = gameState;
    }

    public DataGame() {
    }

    public String getGameField() {
        return gameField;
    }

    public void setGameField(int[][] field) {
        this.gameField = DataField.intToString(field);
    }

    public int[][] getGameFieldArray() {
        return DataField.stringToInt(gameField);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWhoWin() {
        return whoWin;
    }

    public UUID getUuid() {
        return UUID.fromString(id);
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public char getDestinyFirst() {
        return destinyFirst;
    }

    public void setDestinyFirst(char destinyFirst) {
        this.destinyFirst = destinyFirst;
    }

    public char getDestinySecond() {
        return destinySecond;
    }

    public void setDestinySecond(char destinySecond) {
        this.destinySecond = destinySecond;
    }

    public String getNameSecond() {
        return nameSecond;
    }

    public void setNameSecond(String nameSecond) {
        this.nameSecond = nameSecond;
    }

    public String getWhoTurns() {
        return whoTurns;
    }

    public void setWhoTurns(String whoTurns) {
        this.whoTurns = whoTurns;
    }

    public String getWhoPlayer() {
        return whoPlayer;
    }

    public void setWhoPlayer(String whoPlayer) {
        this.whoPlayer = whoPlayer;
    }

    public String getUuidPlayer1() {
        return uuidPlayer1;
    }

    public void setUuidPlayer1(String uuidPlayer1) {
        this.uuidPlayer1 = uuidPlayer1;
    }

    public String getUuidPlayer2() {
        return uuidPlayer2;
    }

    public void setUuidPlayer2(String uuidPlayer2) {
        this.uuidPlayer2 = uuidPlayer2;
    }

    public AbstractModel.GameState getGameState() {
        return gameState;
    }

    public void setGameState(AbstractModel.GameState gameState) {
        this.gameState = gameState;
    }
}


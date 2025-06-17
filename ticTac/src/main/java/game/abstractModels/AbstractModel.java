package game.abstractModels;

import java.util.UUID;

public abstract class AbstractModel<T> {
    public enum GameState {
        WAITING,
        PLAYER_TURN,
        DRAW,
        WINNER
    }

    protected T field;
    protected UUID uuid;
    protected String nameFirst;
    protected String nameSecond;
    protected char destinyFirst;
    protected char destinySecond;
    protected String whoWin;
    protected boolean gameOver;
    protected GameState gameState;
    protected String whoTurns;
    protected String whoPlayer;
    protected String uuidPlayer1;
    protected String uuidPlayer2;


    public AbstractModel(UUID id, T field, String nameFirst, String nameSecond, char destiny, String whoWin,
                         boolean gameOver, GameState gameState, String whoTurns, String whoPlayer,
                         String uuidPlayer1, String uuidPlayer2) {
        this.uuid = id;
        this.field = field;
        this.nameFirst = nameFirst;
        this.nameSecond = nameSecond;
        this.destinyFirst = destiny;
        if(destinyFirst == 'X') destinySecond = 'O';
        else destinySecond = 'X';
        this.whoWin = whoWin;
        this.gameOver = gameOver;
        this.gameState = gameState;
        this.whoTurns = whoTurns;
        this.whoPlayer = whoPlayer;
        this.uuidPlayer1 = uuidPlayer1;
        this.uuidPlayer2 = uuidPlayer2;
    }

    public AbstractModel() {
    }

    public String getUuidPlayer1() { return uuidPlayer1; }
    public void setUuidPlayer1(String uuid) { uuidPlayer1 = uuid; }

    public String getUuidPlayer2() { return uuidPlayer2; }
    public void setUuidPlayer2(String uuid) { uuidPlayer2 = uuid; }

    public UUID getUuid() { return uuid; }
    public void setUuid(UUID uuid) { this.uuid = uuid; }

    public String getNameFirst() { return nameFirst; }
    public void setNameFirst(String nameFirst) { this.nameFirst = nameFirst; }

    public String getNameSecond() { return nameSecond; }
    public void setNameSecond(String nameSecond) { this.nameSecond = nameSecond; }

    public char getDestinyFirst() { return destinyFirst; }
    public void setDestinyFirst(char destiny) { this.destinyFirst = destiny; }

    public char getDestinySecond() { return destinySecond; }
    public void setDestinySecond(char destiny) { this.destinySecond = destiny; }

    public String getWhoWin() { return whoWin; }
    public void setWhoWin(String whoWin) { this.whoWin = whoWin; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public T getField() { return field; }
    public void setField(T field) { this.field = field; }

    public GameState getGameState() { return gameState; }
    public void setGameState(GameState gameState) { this.gameState = gameState; }

    public String getWhoTurns() {
        return whoTurns;
    }

    public void setWhoTurns(String whoTurns) {
        this.whoTurns = whoTurns;
    }

    public void setWhoPlayer (String whoPlayer) {
        this.whoPlayer = whoPlayer;
    }

    public String getWhoPlayer() {
        return whoPlayer;
    }
}

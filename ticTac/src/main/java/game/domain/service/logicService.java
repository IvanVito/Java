package game.domain.service;
import game.abstractModels.AbstractModel;
import game.domain.model.DomainField;
import game.domain.model.DomainGame;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static game.abstractModels.Constants.*;

public class logicService implements Service {
    private static final Set<String> processedGames = ConcurrentHashMap.newKeySet();

    public logicService() {
    }

    @Override
    public int[][] getTurn(int[][] board, int player, DomainGame domainGame) {
        isEnd(board, player == x ? y : x, domainGame);
        if(domainGame.getGameState() == AbstractModel.GameState.DRAW ||
        domainGame.getGameState() == AbstractModel.GameState.WINNER) return board;

        int bestScore = Integer.MIN_VALUE;
        int[] move = new int[]{-1, -1};
        int depth = 0;

        for (int i = 0; i < fieldRows; ++i) {
            for (int j = 0; j < fieldCols; ++j) {
                if (board[i][j] == voidCeil) {
                    board[i][j] = player;
                    int score = miniMax(board, player, player, false, depth);
                    board[i][j] = voidCeil;

                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }

        board[move[0]][move[1]] = player;
        isEnd(board, player, domainGame);

        return board;
    }

    private int miniMax(int[][] board, int player, int initPlayer, boolean isMaximizing, int depth) {
        if (checkGameOver(board))
            return (initPlayer == player) ? maxDepth - depth : depth - maxDepth;
        if (isBoardFull(board)) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int nextPlayer = (player == x) ? y : x;

        for (int i = 0; i < fieldRows; i++) {
            for (int j = 0; j < fieldCols; j++) {
                if (board[i][j] == voidCeil) {
                    board[i][j] = nextPlayer;
                    int score = miniMax(board, nextPlayer, initPlayer, !isMaximizing, depth + 1);
                    board[i][j] = voidCeil;
                    bestScore = isMaximizing ? Math.max(bestScore, score) : Math.min(bestScore, score);
                }
            }
        }

        return bestScore;
    }

    @Override
    public boolean validateGameState(int[][] board, int row, int col) {
        return board[row][col] != 0;
    }

    @Override
    public boolean checkGameOver(int[][] board) {
        boolean isWin = false;
        for (int i = 0; i < fieldRows; i++) {
            if ((board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != voidCeil) ||
                    (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != voidCeil)) {
                isWin = true;
                break;
            }
        }

        if (!isWin && (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != voidCeil) ||
                (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != voidCeil)) {
            isWin = true;
        }

        return isWin;
    }

    public static boolean isBoardFull(int[][] board) {
        for (int i = 0; i < fieldRows; i++) {
            for (int j = 0; j < fieldCols; j++) {
                if (board[i][j] == voidCeil) {
                    return false;
                }
            }
        }
        return true;
    }

    public String whoWinToStr (int player) {
        return player == 1 ? "Крестики" : player == 2 ? "Нолики" : "";
    }

    public void isEnd (int [][] board, int player, DomainGame domainGame) {
        boolean res = checkGameOver(board);
        if(res || isBoardFull(board)) {
            if(res) {
                domainGame.setWhoWin(whoWinToStr(player));
                domainGame.setGameState(AbstractModel.GameState.WINNER);
            } else {
                domainGame.setWhoWin(whoWinToStr(0));
                domainGame.setGameState(AbstractModel.GameState.DRAW);
            }
            domainGame.setGameOver(true);
        } else {
            domainGame.setGameState(AbstractModel.GameState.PLAYER_TURN);
        }
    }

    public void turnHandler(DomainGame domainGame, Integer cell, String currentUuid) {
        int[][] gameField = domainGame.getField().getGameField();
        int row = (cell - 1) / 3;
        int col = (cell - 1) % 3;

        if (domainGame.getWhoPlayer().equals("C")) {
            gameField[row][col] = domainGame.getDestinyFirst() == 'X' ? 1 : 2;
            Thread opponentTurn = getThread(domainGame, gameField);
            opponentTurn.start();
            try {
                opponentTurn.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if (currentUuid != null && currentUuid.equals(domainGame.getWhoTurns())) {
                if (currentUuid.equals(domainGame.getUuidPlayer1())) {
                    gameField[row][col] = domainGame.getDestinyFirst() == 'X' ? 1 : 2;
                    domainGame.setWhoTurns(domainGame.getUuidPlayer2());
                    isEnd(domainGame.getField().getGameField(), domainGame.getDestinyFirst() == 'X' ? 1 : 2, domainGame);
                } else if (currentUuid.equals(domainGame.getUuidPlayer2())) {
                    gameField[row][col] = domainGame.getDestinySecond() == 'X' ? 1 : 2;
                    domainGame.setWhoTurns(domainGame.getUuidPlayer1());
                    isEnd(domainGame.getField().getGameField(), domainGame.getDestinySecond() == 'X' ? 1 : 2, domainGame);
                }
            }
        }
    }

    private Thread getThread(DomainGame domainGame, int [][] gameField) {
        Runnable miniMaxTask = () -> {
            domainGame.getField().setGameField(getTurn(gameField, domainGame.getDestinyFirst() == 'X' ? 2 : 1, domainGame));
        };
        return new Thread(miniMaxTask);
    }

    public DomainGame reset(DomainGame domainGame, String id) throws Exception {
        domainGame.setField(new DomainField(3, 3));
        domainGame.setWhoWin("");
        domainGame.setGameOver(false);
        processedGames.remove(id);
        domainGame.setGameState(AbstractModel.GameState.PLAYER_TURN);

        if (domainGame.getWhoPlayer().equals("H")) {
            String nameFirst = domainGame.getNameFirst();
            domainGame.setNameFirst(domainGame.getNameSecond());
            domainGame.setNameSecond(nameFirst);
            char destinyFirst = domainGame.getDestinyFirst();
            domainGame.setDestinyFirst(domainGame.getDestinySecond());
            domainGame.setDestinySecond(destinyFirst);
            String uuid1 = domainGame.getUuidPlayer1();
            domainGame.setUuidPlayer1(domainGame.getUuidPlayer2());
            domainGame.setUuidPlayer2(uuid1);
            domainGame.setWhoTurns(domainGame.getUuidPlayer1());
        }
        return domainGame;
    }

    public DomainGame createGame(String uuidPlayer, String person, String destiny, String name) {
        DomainGame domainGame = new DomainGame();
        domainGame.setGameState(AbstractModel.GameState.WAITING);
        domainGame.setWhoPlayer(person);
        domainGame.setNameFirst(name);
        domainGame.setDestinyFirst(destiny.charAt(0));
        domainGame.setUuidPlayer1(uuidPlayer);
        domainGame.setWhoTurns(uuidPlayer);

        return domainGame;
    }

    public static Set<String> getProcessedGames() {
        return processedGames;
    }
}


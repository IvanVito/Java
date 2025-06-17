package game.domain.service;

import game.domain.model.DomainGame;

public interface Service {
    int[][] getTurn(int[][] board, int player, DomainGame domainGame);
    boolean validateGameState(int[][] board, int row, int col);
    boolean checkGameOver(int[][] gameState);
}

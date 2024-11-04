package com.gamejam.game.link;

public interface FrontendZugriff {
    /*
    This Interface is implemented by the BackendManager, its method called by GameJam.java.
    The methods are used to interact between front- and backend.
     */

    // method to start game (clean the Board)
    void startGame();

    // method to get current board
    Board getBoard();

    // method to get current GameState
    GameState getGameState();

    // check if a move is valid
    boolean isValidMove(Coordinate start, Coordinate end);

    // method to move a piece
    boolean movePiece(Coordinate start, Coordinate end);
}

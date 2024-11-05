package com.gamejam.game.backend;

import com.gamejam.game.link.*;

public class BackMan implements FrontendZugriff {
    /*
        * This class implements the interface linking front to backend.
     */

    // ---------------- ATTRIBUTES

    private Board gameBoard;
    private GameState gameState;


    // ---------------- METHODS

    public BackMan() {
        // Constructor! ---------------------------

    }


    /*
        * This method is used to start the game: Clear the existing Board and set up a new one.
     */
    @Override
    public void startGame() {

        gameState = GameState.WHITE_TURN; // setting starting GameState

        // create new board and set it up - EMPTY BOARD
        Tile[][] tiles = new Tile[8][8];
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                tiles[i][j] = new Tile();
            }
        }

        gameBoard = new Board(tiles); // 
    }

    /*
     * This method is used to get the board from the backend, it's used in the frontend to display the current board
     */
    @Override
    public Board getBoard() {
        return gameBoard;
    }

    /*
     * This method is used to get the current game state from the backend, it's used in the frontend to make sure
     * it's in the same state as the backend
     */
    @Override
    public GameState getGameState() {
        return gameState;
    }

    /*
     * This method is used to see if a move a Player has made is allowed. It's used in the frontend to make sure
     * that the backend controls whether a move would be allowed or not
     * returns: false if the move is not allowed, true if it is
     */
    @Override
    public boolean isValidMove(Coordinate start, Coordinate end) {
        return true;
    }

    /*
    * This method is used to move a piece on the board. Used by the frontend to move a piece of a coordinate
    * to another coordinate.
    * returns: false if no hit has taken place, true if a hit has taken place
     */
    @Override
    public boolean movePiece(Coordinate start, Coordinate end) {
        boolean returnBool = gameBoard.updateByExchange(start, end);
        switchGameState();
        // if a king has been killed,
        if (returnBool){
            // check if the game is over
            if (isGameOver()){
                gameState = GameState.NOT_IN_GAME;
            }
        }
        return returnBool;
    }

    // ---------- DUMMIES for US TO USE IN TESTING FOR VALIDMOVE AND MOVEPIECE
    private void switchGameState(){
        // switch between white turn and black turn
        if (gameState == GameState.WHITE_TURN){
            gameState = GameState.BLACK_TURN;
        } else {
            gameState = GameState.WHITE_TURN;
        }
    }
    private boolean isGameOver(){
        // check if the game is over
        // iterate over the board and check if there are any pieces left
        Tile[][] tiles = gameBoard.getGameBoard();

        boolean whiteExists = false;
        boolean blackExists = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (tiles[i][j].getPieceType() != null){
                    if (tiles[i][j].getPieceType()==Piece.KING) {
                        if (tiles[i][j].getTeamColor() == TeamColor.WHITE) {
                            whiteExists = true;
                        } else {
                            blackExists = true;
                        }
                    }
                }
            }
        }
        return false;
    }
}

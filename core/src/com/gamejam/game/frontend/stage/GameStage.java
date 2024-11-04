package com.gamejam.game.frontend.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.gamejam.game.GameJam;
import com.gamejam.game.link.*;

import static com.gamejam.game.GameJam.*;

public class GameStage {

    private Stage gameStage;
    private Table rootBoard;

    private Table moveLogoTable;

    // ----- Asset Variables -------------------------------------------------------------------------------------------

    private static Image whiteLogo;
    private static Image blackLogo;

    private Texture impossibleMoveLogo;
    private static Texture tile;
    private static Texture empty;
    private static Texture obstacle;

    // white pieces ---------------------------------------------------------------------------------
    private static Texture whitePawn;
    private static Texture whiteRook;
    private static Texture whiteKnight;
    private static Texture whiteBishop;
    private static Texture whiteQueen;
    private static Texture whiteKing;
    private static Texture whiteProgrammer;
    private static Texture whiteChancellor;

    // black pieces ---------------------------------------------------------------------------------
    private static Texture blackPawn;
    private static Texture blackRook;
    private static Texture blackKnight;
    private static Texture blackBishop;
    private static Texture blackQueen;
    private static Texture blackKing;
    private static Texture blackProgrammer;
    private static Texture blackChancellor;

    // sounds ---------------------------------------------------------------------------------------
    private static Texture hitSound;

    public GameStage() {
        Gdx.app.log("GameStage", "Creating GameStage");

        loadAllAssets();

        Gdx.app.log("GameStage", "creating the board game with pieces");
        this.gameStage = new Stage(); // create gameStage object
        rootBoard = new Table(); // create rootTable
        gameStage.addActor(rootBoard); // add rootTable to gameStage
        gameStage.addActor(buttonsTable(getTileSize())); // add the buttonsTable to the gameStage
        updateGameStage(); // first update of the gameStage
        gameStage.addActor(moveLogoTable); // add the move logo table to the stage
    }

    // ----- Game Methods ----------------------------------------------------------------------------------------------

    /*
     * Method to create the game stage, each actor is added to the stage and the stage is returned
     */
    private void updateGameStage() {

        Gdx.app.log("GameStage", "Updating the GameStage");

        updateMoveLogo();

        // ----- getting and setting all info from GameJam we need to create the current iteration of the board

        Board gameBoard = getBackend().getBoard();
        final Tile[][] gameBoardArray = gameBoard.getGameBoard();

        float tileSize = getTileSize();

        // add the audio table to gameStage as Actor and position on the far right of the Screen
        Stage gameStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        GameJam.getBatch().begin();

        // ---------------------------------------------------------------------------------------------------

        Table root = new Table();

        // for the size of the tiles
        int numRows = getNumberRows();
        int numColumns = getNumberColumns();

        // root of size numRows to numColumns and center of screen
        root.setSize(numColumns * tileSize, numRows * tileSize);
        root.setPosition((float) Gdx.graphics.getWidth() / 2 - root.getWidth() / 2,
                (float) Gdx.graphics.getHeight() / 2 - root.getHeight() / 2);

        for (int j = 0; j < numRows; j++) {
            root.row();
            for (int i = 0; i < numColumns; i++) {

                // add a chessBoardTile underneath the piece to build the board --------------
                Image tileImage = new Image(tile);
                tileImage.setSize(tileSize, tileSize);
                root.add(tileImage).size(tileSize);

                // tileWidget is "the Piece" ----------------------------------------
                final Stack tileWidget = new Stack();
                Texture pieceText = getTextFromTile(gameBoardArray[i][j]);
                Image solPiece = new Image(pieceText);
                solPiece.setSize(tileSize, tileSize);
                tileWidget.add(solPiece);


                final int finalI = i;
                final int finalJ = j;
                tileWidget.addListener(new DragListener() {
                    @Override
                    public void dragStart(InputEvent event, float x, float y, int pointer) {
                        // Code here will run when the player starts dragging the actor.

                        Gdx.app.log("GameStage", "a drag has started");

                        // if the current color is not the color of the player, we return and display a move not
                        // allowed actor on screen for 2 seconds

                        GameState state = GameJam.getBackend().getGameState();
                        if (state == GameState.WHITE_TURN &
                                gameBoardArray[finalI][finalJ].getTeamColor() == TeamColor.BLACK) {
                            // we return and display a move not allowed actor on screen for 2 seconds
                            moveNotAllowed();
                            event.cancel();
                        } else if (state == GameState.BLACK_TURN &
                                gameBoardArray[finalI][finalJ].getTeamColor() == TeamColor.WHITE) {
                            // we return and display a move not allowed actor on screen for 2 seconds
                            moveNotAllowed();
                            event.cancel();
                        }
                    }

                    @Override
                    public void drag(InputEvent event, final float x, final float y, int pointer) {
                        // move the tileWidget with the mouse
                        tileWidget.moveBy(x - tileWidget.getWidth() / 2, y - tileWidget.getHeight() / 2);
                    }


                    @Override
                    public void dragStop(InputEvent event, float x, float y, int pointer) {

                        // Code here will run when the player lets go of the actor

                        // Get the position of the tileWidget relative to the parent actor (the gameBoard)
                        Vector2 localCoords = new Vector2(x, y);
                        // Convert the position to stage (screen) coordinates
                        Vector2 screenCoords = tileWidget.localToStageCoordinates(localCoords);

                        Coordinate currentCoord = calculateTileByPX((int) screenCoords.x, (int) screenCoords.y);

                        // for loop through validMoveTiles, at each tile we check for equality of currentCoord
                        // with the Coordinate
                        // in the ArrayList by using currentCoord.checkEqual(validMoveTiles[i]) and if true,
                        // we set the
                        // validMove Variable to true, call on the update method of the Board class and break
                        // the for loop
                        // then clear the Board.

                        // Coordinate
                        Coordinate startingCoord = new Coordinate(finalI, finalJ);
                        Coordinate targetCoord = new Coordinate(currentCoord.getX(), currentCoord.getY());

                        // ask via Interface link if the moves is Valid
                        boolean moveLegit = getBackend().isValidMove(startingCoord, targetCoord
                        );

                        if (moveLegit) {
                            // Board.update with oldX, oldY, newX, newY
                            boolean hitTarget = getBackend().movePiece(startingCoord, targetCoord);
                            if(hitTarget){ playHitSound(); }
                            GameJam.setLegitTurn(true);
                            Gdx.app.log("GameStage", "a drag has stopped, a move WAS made");
                        } else {
                            Gdx.app.log("GameStage", "a drag has stopped, a move WAS NOT made");
                            // we return and display a move not allowed actor on screen for 2 seconds
                            moveNotAllowed();
                            // resets gameBoard, since move wasn't allowed, and we have to set the drag back to start
                            updateGameStage();
                        }
                    }
                });
                root.add(tileWidget).size(tileSize);
            }
        }
        GameJam.getBatch().end();
        setRootTable(root);
    }

    private void playHitSound() {
        // play the hit sound
        Gdx.app.log("GameStage", "Playing the hit sound");
        hitSound = new Texture("Misc/hitSound.png");
    }

    private void moveNotAllowed() {
        Gdx.app.log("GameStage", "The Move was not allowed, displaying the NotAllowed Actor");
        class NotAllowed extends Actor{
            NotAllowed(){
                // create a new Stack
                Stack stack = new Stack();
                // create a new Image
                Image image = new Image(impossibleMoveLogo);
                // set the size of the image
                float tileSize = getTileSize();
                image.setSize(tileSize, tileSize);
                // add the image to the stack
                stack.add(image);
                // add the stack to the stage
                gameStage.addActor(stack);
                // set the position of the stack
                stack.setPosition((float) Gdx.graphics.getWidth() / 2 - tileSize,
                        (float) Gdx.graphics.getHeight() / 2 - tileSize);
            }
        }
    }

    /*
    * Method to take a piece and return the class attribute loaded Texture of that piece
     */
    private Texture getTextFromTile(Tile tile) {
        TeamColor teamColor = tile.getTeamColor();
        Piece pieceType = tile.getPieceType();

        // checking for niche cases, where tile has a team color but is actually an obstacle
        if (pieceType == Piece.OBSTACLE) {
            return obstacle;
        }

        switch (teamColor.name()) {
            case "WHITE":
                return getWhitePieceTexture(pieceType);
            case "BLACK":
                return getBlackPieceTexture(pieceType);
            default:
                Gdx.app.log("GameStage", "NO valid team color -> Empty");
                return empty;
        }
    }

    private Texture getBlackPieceTexture(Piece pieceType) {
        switch (pieceType.name()) {
            case "PAWN":
                return whitePawn;
            case "ROOK":
                return whiteRook;
            case "KNIGHT":
                return whiteKnight;
            case "BISHOP":
                return whiteBishop;
            case "QUEEN":
                return whiteQueen;
            case "KING":
                return whiteKing;
            case "PROGRAMMER":
                return whiteProgrammer;
            case "CHANCELLOR":
                return whiteChancellor;
            default:
                Gdx.app.log("GameStage", "NO valid piece type -> Empty");
                return empty;
        }
    }

    private Texture getWhitePieceTexture(Piece pieceType) {
        switch (pieceType.name()) {
            case "PAWN":
                return blackPawn;
            case "ROOK":
                return blackRook;
            case "KNIGHT":
                return blackKnight;
            case "BISHOP":
                return blackBishop;
            case "QUEEN":
                return blackQueen;
            case "KING":
                return blackKing;
            case "PROGRAMMER":
                return blackProgrammer;
            case "CHANCELLOR":
                return blackChancellor;
            default:
                Gdx.app.log("GameStage", "NO valid piece type -> Empty");
                return empty;
        }
    }

    public Stage getStage() {
        return gameStage;
    }

    private void setRootTable(Table root) {
        this.rootBoard = root;
    }

    private void loadAllAssets() {
        Gdx.app.log("GameStage", "Loading Assets: Environment Textures starting"); // ----------------

        impossibleMoveLogo = new Texture("Misc/impossibleMove.png");
        tile = new Texture("Misc/Tile.png");
        empty = new Texture("Misc/Empty.png");
        obstacle = new Texture("Misc/Obstacle.png");

        whiteLogo = new Image(new Texture("Misc/WhiteMove.png"));
        blackLogo = new Image(new Texture("Misc/BlackMove.png"));


        Gdx.app.log("GameStage", "Loading Assets: Environment Textures loading finished"); // --------
        Gdx.app.log("GameStage", "Loading Assets: Piece Textures starting"); // ----------------------

        // white pieces ----------------------------------------------------
        whitePawn = new Texture("white/pawn.png");
        whiteRook = new Texture("white/rook.png");
        whiteKnight = new Texture("white/knight.png");
        whiteBishop = new Texture("white/bishop.png");
        whiteQueen = new Texture("white/queen.png");
        whiteKing = new Texture("white/king.png");
        whiteProgrammer = new Texture("white/programmer.png");
        whiteChancellor = new Texture("white/chancellor.png");

        // black pieces ----------------------------------------------------
        blackPawn = new Texture("black/pawn.png");
        blackRook = new Texture("black/rook.png");
        blackKnight = new Texture("black/knight.png");
        blackBishop = new Texture("black/bishop.png");
        blackQueen = new Texture("black/queen.png");
        blackKing = new Texture("black/king.png");
        blackProgrammer = new Texture("black/programmer.png");
        blackChancellor = new Texture("black/chancellor.png");

        Gdx.app.log("GameStage", "Loading Assets: Piece Textures starting"); // ----------------------
        Gdx.app.log("GameStage", "Loading Assets: Sounds starting"); // ------------------------------

        hitSound = new Texture("Misc/hitSound.png");

        Gdx.app.log("GameStage", "Loading Assets: Sounds finished"); // ------------------------------
    }

    private Table buttonsTable(float tileSize) {
        // create another table for the option buttons

        Gdx.app.log("GameStage", "Creating the Buttons Table");

        Table backTable = new Table();
        backTable.setSize(tileSize * 5, tileSize * 2f);
        // bottom right the table in the parent container
        backTable.setPosition(Gdx.graphics.getWidth() - backTable.getWidth(), tileSize);
        gameStage.addActor(backTable); // Add the table to the stage

        // Exit to Main Menu button to return to the main menu
        TextButton menuButton = new TextButton("Main Menu", GameJam.getSkin());
        menuButton.align(Align.bottomRight);
        backTable.add(menuButton);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameJam.setInGame(false);
                createMainMenuStage();
            }
        });

        return backTable;
    }

    private void updateMoveLogo() {
        /*
         * method for updating the moveLogoStage with the correct logo
         * if in game, add an actor depending on current GameState
         */

        Gdx.app.log("GameStage", "Updating the Move Logo");

        // get currentGameState
        GameState currentState = GameJam.getBackend().getGameState();

        Table currentMover = new Table();

        float tileSize = getTileSize();

        float width = tileSize * 3;
        float height = tileSize * 2;
        currentMover.setSize(width, height);

        // Position at upper left corner
        float xPosition = tileSize / 3; // Left edge of the screen
        float yPosition = Gdx.graphics.getHeight() - height;
        // Subtract height of the mover, positioning it at the top
        currentMover.setPosition(xPosition, yPosition);

        if (currentState == GameState.WHITE_TURN) {
            currentMover.addActor(whiteLogo);
        } else if (currentState == GameState.BLACK_TURN) {
            currentMover.addActor(blackLogo);
        }

        moveLogoTable = currentMover;
    }
}

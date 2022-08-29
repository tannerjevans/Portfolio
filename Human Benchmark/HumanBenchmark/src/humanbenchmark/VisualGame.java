package humanbenchmark;

import humanbenchmark.Data.Status;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <code>VisualGame</code> is the class for the Visual Memory Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. The Math Game is the one minigame not present on the Human
 * Benchmark website. It is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class VisualGame extends Data.Game {
    // CLASS ENUMS -----------------------------------------------------------\\

    private enum TileType {
        ACTIVE,
        INACTIVE
    }
    private enum TileState {
        IN_REVEAL,
        PRESSED,
        UNPRESSED
    }


    // FINAL CLASS VARIABLES -------------------------------------------------\\

    private final Data.GameType    GAME_TYPE    = Data.GameType.VISUAL;
    private final Random           RANDOM       = new Random();
    private final LinkedList<Tile> TILE_SET     = new LinkedList<>();
    private final LinkedList<Tile> ACTIVE_TILES = new LinkedList<>();
    private final GridPane         TILE_GRID    = new GridPane();
    private final VBox             GAME_BOX     = new VBox();
    private final Label            LARGE_LINE   = new Label();
    private final Label            MIDDLE_LINE  = new Label();
    private final Label            SMALL_LINE   = new Label();

    private final ScheduledExecutorService scheduler   =
            Executors.newScheduledThreadPool(2);


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_CONTINUE = "Click to continue.";
    private final String STR_DONE     = "Game over.";
    private final String STR_RESULTS  = "Level Achieved: ";
    private final String STR_LEVEL    = "Level: ";
    private final String STR_LIVES    = "  Lives: ";
    private final String STR_QUIT     =
            "Click anywhere to return to the main menu.";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_HIDDEN      = "visualHiddenTile";
    private final String ID_REVEAL      = "visualActiveTileRevealed";
    private final String ID_WRONG       = "visualWrongTile";
    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";
    private final String ID_GAME        = "tileGamePane";
    private final String ID_GRID        = "tileGameGrid";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private int    level            = 1;
    private int    gridSize         = 3;
    private int    activeTileNumber = 3;
    private int    roundLives       = 3;
    private int    gameLives        = 3;
    private Status status           = Status.START;


    // CLASS OBJECTS ---------------------------------------------------------\\

    /*
     * In the tile-based games, it became clear that creating a button with
     * information about its own state and value would be beneficial. The
     * Tile bears the responsibility of knowing what it should be doing based
     * on the state of it and the game.
     */
    private class Tile extends Button {
        protected final TileType TILE_TYPE;
        protected TileState      tileState = TileState.IN_REVEAL;

        private Tile(TileType tileType, double dimension) {
            TILE_TYPE = tileType;

            MainDisplay.constrainElement(this, dimension, dimension);
            this.setId(ID_HIDDEN);

            switch (TILE_TYPE) {
            case ACTIVE:
                this.setOnAction(event -> {
                    switch (tileState) {
                    case UNPRESSED:
                        tileState = TileState.PRESSED;
                        ACTIVE_TILES.remove(this);
                        this.setId(ID_REVEAL);
                        if (ACTIVE_TILES.isEmpty()) {
                            level++;
                            activeTileNumber = level + 2;
                            if (gridSize*gridSize < 2*activeTileNumber) {
                                gridSize++;
                            }
                            initializeTiles();
                        }
                    case PRESSED:
                    case IN_REVEAL:
                        break;
                    }
                });
                break;
            case INACTIVE:
                this.setOnAction(event -> {
                    switch (tileState) {
                    case UNPRESSED:
                        tileState = TileState.PRESSED;
                        this.setId(ID_WRONG);
                        roundLives--;
                        if (roundLives == 0) {
                            gameLives--;
                            if (gameLives == 0) {
                                status = Status.DONE;
                                GAME_BOX.getChildren().clear();
                                LARGE_LINE.setText(STR_DONE);
                                MIDDLE_LINE.setText(STR_RESULTS + level);
                                SMALL_LINE.setText(STR_QUIT);
                                GAME_BOX.getChildren().addAll(LARGE_LINE,
                                                              SMALL_LINE);
                            } else {
                                initializeTiles();
                            }
                        }
                    case PRESSED:
                    case IN_REVEAL:
                        break;
                    }
                });
                break;
            }
        }

        protected void hideTile() {
            tileState = TileState.UNPRESSED;
            this.setId(ID_HIDDEN);
        }

        protected void showTile() {
            if (TILE_TYPE == TileType.ACTIVE) {
                this.setId(ID_REVEAL);
            }
        }
    }


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        LARGE_LINE.setText(GAME_TYPE.getName());
        MIDDLE_LINE.setText(GAME_TYPE.getDescription());
        SMALL_LINE.setText(STR_CONTINUE);
        GAME_BOX.getChildren().addAll(LARGE_LINE, MIDDLE_LINE, SMALL_LINE);

        Scene gameScene = new Scene(GAME_BOX, 0, 0);

        gameScene.getStylesheets().add(Data.STYLESHEET);
        LARGE_LINE.setId(ID_TITLE);
        MIDDLE_LINE.setId(ID_DESCRIPTION);
        SMALL_LINE.setId(ID_DESCRIPTION);
        GAME_BOX.setId(ID_GAME);
        TILE_GRID.setId(ID_GRID);

        gameScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            switch (status) {
            case START:
                status = Status.IN_ROUND;
                initializeTiles();
            case IN_ROUND:
                break;
            case DONE:
                safeGameShutdown();
            }
        });

        return gameScene;
    }

    @Override
    protected void safeGameShutdown() {
        scheduler.shutdownNow();
        MainDisplay.endGame();
    }


    // PRIVATE CLASS MEMBERS -------------------------------------------------\\

    private void initializeTiles() {
        GAME_BOX.getChildren().clear();
        TILE_SET.clear();
        ACTIVE_TILES.clear();
        TILE_GRID.getChildren().clear();
        roundLives = 3;
        MIDDLE_LINE.setText(STR_LEVEL + level + STR_LIVES + gameLives);

        double tileDimension = (700 - 20*((float)gridSize-1))/gridSize;
        LinkedList<Tile> tileSetCopy = new LinkedList<>();

        Runnable showTiles = () -> Platform.runLater(() -> {
            for (Tile tile : TILE_SET) {
                tile.showTile();
            }
        });

        Runnable hideTiles = () -> Platform.runLater(() -> {
            for (Tile tile : TILE_SET) {
                tile.hideTile();
            }
        });

        for (int i = 0; i < gridSize*gridSize; i++) {
            TileType tileType = TileType.INACTIVE;

            if (i < activeTileNumber)  {
                tileType = TileType.ACTIVE;
            }

            Tile newTile = new Tile(tileType, tileDimension);
            TILE_SET.add(newTile);
            tileSetCopy.add(newTile);

            if (tileType == TileType.ACTIVE) {
                ACTIVE_TILES.add(newTile);
            }
        }

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                int index = RANDOM.nextInt(tileSetCopy.size());
                TILE_GRID.add(tileSetCopy.remove(index), i, j);
            }
        }

        scheduler.schedule(showTiles, 500, TimeUnit.MILLISECONDS);
        scheduler.schedule(hideTiles, 2, TimeUnit.SECONDS);

        GAME_BOX.getChildren().addAll(MIDDLE_LINE, TILE_GRID);
    }
}

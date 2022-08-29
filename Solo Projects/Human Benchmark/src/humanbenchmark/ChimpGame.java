package humanbenchmark;

import humanbenchmark.Data.Status;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.Random;

/**
 * <code>ChimpGame</code> is the class for the Chimp Game Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. It is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class ChimpGame extends Data.Game {

    // FINAL CLASS MEMBERS ---------------------------------------------------\\

    private final Data.GameType    GAME_TYPE   = Data.GameType.CHIMP;
    private final Random           RANDOM      = new Random();
    private final LinkedList<Tile> TILE_SET    = new LinkedList<>();
    private final LinkedList<Tile> SHOWN_TILES = new LinkedList<>();
    private final GridPane         TILE_GRID   = new GridPane();
    private final VBox             GAME_BOX    = new VBox();
    private final Label            TOP_LINE    = new Label();
    private final Label            MIDDLE_LINE = new Label();
    private final Label            BOTTOM_LINE = new Label();


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_CONTINUE = "Click to continue.";
    private final String STR_NUMBERS  = "Numbers: ";
    private final String STR_STRIKES  = "Strikes Remaining: ";
    private final String STR_OF       = " of 3";
    private final String STR_QUIT     =
            "Click anywhere to return to the main menu.";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_BLANK       = "chimpTileBlank";
    private final String ID_LABEL       = "chimpTileLabel";
    private final String ID_INITIAL     = "chimpTileInit";
    private final String ID_CLICKED     = "chimpTileClicked";
    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";
    private final String ID_GAME        = "tileGamePane";
    private final String ID_GRID        = "tileGameGrid";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private long   strikes       = 3;
    private int    numberOfTiles = 4;
    private int    currentTarget = 1;
    private Status status        = Status.START;


    // CLASS OBJECTS ---------------------------------------------------------\\

    /*
     * In the tile-based games, it became clear that creating a button with
     * information about its own state and value would be beneficial. The
     * Tile bears the responsibility of knowing what it should be doing based
     * on the state of it and the game.
     */
    private class Tile extends Button {
        protected final int value;
        protected boolean pressed = false;

        private Tile(int value) {
            this.value = value;
            if (value == 0) {
                this.setId(ID_BLANK);
            } else {
                Label label = new Label(Integer.toString(value));
                this.setGraphic(label);

                label.setId(ID_LABEL);
                this.setId(ID_INITIAL);

                this.setOnAction(event -> {
                    if (!pressed) {
                        pressed = true;
                        if (this.value == currentTarget) {
                            if (currentTarget == numberOfTiles) {
                                numberOfTiles++;
                                pushInfo();
                            } else {
                                for (Tile tempTile : SHOWN_TILES) {
                                    tempTile.setId(ID_CLICKED);
                                    tempTile.setGraphic(null);
                                }
                                SHOWN_TILES.removeFirst();
                                this.setGraphic(null);
                                this.setId(ID_BLANK);
                                currentTarget++;
                            }
                        } else {
                            strikes--;
                            pushInfo();
                            if (strikes <= 0) {
                                status = Status.DONE;
                            }
                        }
                    }
                });
            }
        }
    }


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        TOP_LINE.setText(GAME_TYPE.getName());
        MIDDLE_LINE.setText(GAME_TYPE.getDescription());
        BOTTOM_LINE.setText(STR_CONTINUE);
        GAME_BOX.getChildren().addAll(TOP_LINE, MIDDLE_LINE, BOTTOM_LINE);
        Scene gameScene = new Scene(GAME_BOX, 0, 0);

        gameScene.getStylesheets().add(Data.STYLESHEET);
        TOP_LINE.setId(ID_TITLE);
        MIDDLE_LINE.setId(ID_DESCRIPTION);
        BOTTOM_LINE.setId(ID_DESCRIPTION);
        GAME_BOX.setId(ID_GAME);
        TILE_GRID.setId(ID_GRID);

        gameScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            switch (status) {
            case START:
            case BETWEEN_ROUNDS:
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
        MainDisplay.endGame();
    }


    // PRIVATE CLASS MEMBERS -------------------------------------------------\\

    private void initializeTiles() {
        currentTarget = 1;
        status = Status.IN_ROUND;
        TILE_SET.clear();
        SHOWN_TILES.clear();
        TILE_GRID.getChildren().clear();
        GAME_BOX.getChildren().clear();

        for (int i = 0; i < 8*5; i++) {
            int value = 0;
            if (i < numberOfTiles) value = i+1;
            Tile newTile = new Tile(value);
            TILE_SET.add(newTile);
            if (newTile.value > 0) SHOWN_TILES.add(newTile);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 5; j++) {
                int index = RANDOM.nextInt(TILE_SET.size());
                TILE_GRID.add(TILE_SET.remove(index), i, j);
            }
        }
        GAME_BOX.getChildren().add(TILE_GRID);
    }

    private void pushInfo() {
        int number = numberOfTiles-1;
        status = Status.BETWEEN_ROUNDS;
        TOP_LINE.setText(STR_NUMBERS + number);
        MIDDLE_LINE.setText(STR_STRIKES + strikes + STR_OF);
        if (strikes<= 0) {
            BOTTOM_LINE.setText(STR_QUIT);
        }
        GAME_BOX.getChildren().clear();
        GAME_BOX.getChildren().addAll(TOP_LINE, MIDDLE_LINE, BOTTOM_LINE);
    }
}

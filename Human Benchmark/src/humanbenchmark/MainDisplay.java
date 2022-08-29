package humanbenchmark;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * <code>MainDisplay</code> serves as the hub through which the JavaFX
 * elements operate. It contains helper <code>method</code>s for the
 * instantiation of mini-games, <code>Scene</code>s, and such. It deals
 * primarily in the logic of the main menu, and serves only to call and pass
 * data from mini-games to the screen. Mini-game game- and display-logic is
 * encapsulated within individual <code>classes</code>.
 *
 * MainDisplay is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
abstract class MainDisplay {
    // CSS IDS ---------------------------------------------------------------\\

    private static final String ID_WINDOW          = "windowPane";
    private static final String ID_TITLE           = "title";
    private static final String ID_TILE_SET        = "gameTiles";
    private static final String ID_TILE            = "gameTile";
    private static final String ID_CONTENT         = "gameTileContent";
    private static final String ID_PREVIEW         = "previewPane";
    private static final String ID_PREVIEW_TITLE   = "previewPaneTitle";
    private static final String ID_PREVIEW_CONTENT = "previewPaneContent";
    private static final String ID_BUTTON          = "previewPaneButton";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private static Stage gameWindow;
    private static Data.Game game;


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    /*
     * makeStartScreen() provides the main stage and mini-game selector to
     * the  main window. It constructs this by means of the Data class's Game
     * enum's helper methods, enabling it to remain trim.
     */
    protected static void makeStartScene() {
        VBox mainPanel      = new VBox(Data.PADDING);
        Label gameTitle     = new Label(Data.TITLE);
        Label  gameSubtitle = new Label(Data.SUBTITLE);
        Region spacer       = new Region();
        spacer.setPrefHeight(20);
        GridPane miniGames = new GridPane();

        mainPanel.setId(ID_WINDOW);
        gameTitle.setId(ID_TITLE);
        miniGames.setId(ID_TILE_SET);

        int count  = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Data.GameType gameType;
                try {
                    gameType = Data.GameType.getType(count);
                } catch (Exception e) {
                    break;
                }
                Button        button   = makeGameTile(gameType);
                miniGames.add(button, i, j);
                button.setOnAction(
                        event -> setPreview(gameType));
                if (count == 0) button.fire();
                count++;
            }
        }

        Region spacer2 = new Region();
        spacer2.setPrefWidth(10);

        miniGames.add(spacer2, 3, 0,
                      1, 3);
        miniGames.add(HumanBenchmark.previewPane, 4, 0,
                      2, 3);

        mainPanel.getChildren().addAll(gameTitle, gameSubtitle, spacer,
                                       miniGames);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainPanel);
        HumanBenchmark.startScene = new Scene(scrollPane, 0, 0);
        System.out.println(Data.STYLESHEET);
        HumanBenchmark.startScene.getStylesheets().add(Data.STYLESHEET);
    }

    protected static void endGame() {
        gameWindow.close();
    }

    /*
     * Performs repetitive, boilerplate window conditioning.
     */
    protected static void conditionWindow(Stage stage, String string) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setTitle(string);
        stage.setMaxWidth(Data.WIDTH);
        stage.setMaxHeight(Data.HEIGHT);
        stage.setMinWidth(Data.WIDTH);
        stage.setMinHeight(Data.HEIGHT);
        stage.setX((screenBounds.getWidth() - Data.WIDTH) / 2);
        stage.setY((screenBounds.getHeight() - Data.HEIGHT) / 2);
    }

    /*
     * Extracts code for specifying an exact size.
     */
    protected static void constrainElement(Region region,
                                           double width,
                                           double height) {
        region.setMinWidth(width);
        region.setMaxWidth(width);
        region.setMinHeight(height);
        region.setMaxHeight(height);
    }


    // PRIVATE CLASS MEMBERS -------------------------------------------------\\

    /*
     * All minigames are started through the startGame() method. It sets up
     * the stage, passes the data to it, and then steps out of the way to
     * permit the mini-game to perform the remaining logic. It does enforce
     * that when the mini-game window is exited via the window buttons, the
     * mini-game's safeGameShutdown() is called to stop still-running threads
     * processes.
     */
    private static void startGame(Data.GameType gameType) {
        gameWindow = new Stage();
        Platform.setImplicitExit(true);
        conditionWindow(gameWindow, gameType.getName());
        game = gameType.makeGame();
        assert game != null;
        Scene gameScene = game.getScene();
        gameWindow.setScene(gameScene);
        gameWindow.initModality(Modality.APPLICATION_MODAL);

        gameWindow.setOnCloseRequest(event -> game.safeGameShutdown());

        gameWindow.show();
    }

    /*
     * Makes the mini-game tiles for display on the main menu.
     */
    private static Button makeGameTile(Data.GameType gameType) {
        Label label = new Label(gameType.getName());
        label.setId(ID_CONTENT);
        Button button = new Button();
        button.setId(ID_TILE);
        button.setGraphic(label);
        return button;
    }

    /*
     * Sets the preview pane for the minigame when the minigame's tile is
     * selected.
     */
    private static void setPreview(Data.GameType gameType) {
        HumanBenchmark.previewPane.getChildren().clear();
        VBox preview = new VBox();
        preview.setId(ID_PREVIEW);
        Label previewTitle = new Label(gameType.getName());
        previewTitle.setId(ID_PREVIEW_TITLE);
        Label previewText = new Label(gameType.getDescription());
        previewText.setId(ID_PREVIEW_CONTENT);
        previewText.setWrapText(true);
        Label buttonLabel = new Label("Launch " + gameType.getName());
        buttonLabel.setId(ID_CONTENT);
        Button launchButton = new Button();
        launchButton.setId(ID_BUTTON);
        launchButton.setGraphic(buttonLabel);
        launchButton.setOnAction(event -> startGame(gameType));
        preview.getChildren().addAll(previewTitle, previewText,
                                     launchButton);
        HumanBenchmark.previewPane.getChildren().add(preview);
    }
}

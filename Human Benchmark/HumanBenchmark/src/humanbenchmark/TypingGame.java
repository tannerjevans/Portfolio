package humanbenchmark;

import humanbenchmark.Data.Status;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class TypingGame extends Data.Game {
    private final Data.GameType GAME_TYPE   = Data.GameType.VERBAL;
    private final Random        RANDOM      = new Random();
    private final VBox          GAME_BOX    = new VBox();
    private final TextArea      TEXT_AREA   = new TextArea();
    private final Label         TOP_LINE    = new Label();
    private final Label         MIDDLE_LINE = new Label();
    private final Label         BOTTOM_LINE = new Label();
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(2);

    private int    level            = 1;
    private int    gridSize         = 3;
    private int    activeTileNumber = 3;
    private int    roundLives       = 3;
    private int    gameLives        = 3;
    private Status status           = Status.START;

    private String matchString = "Hello.";

    @Override
    protected Scene getScene() {
        Scene gameScene;
        TOP_LINE.setText(GAME_TYPE.getName());
        TOP_LINE.setId("gameTitle");
        MIDDLE_LINE.setText(GAME_TYPE.getDescription());
        MIDDLE_LINE.setId("gameDescription");
        BOTTOM_LINE.setText("Click to continue.");
        BOTTOM_LINE.setId("gameDescription");
        GAME_BOX.setId("tileGamePane");
        GAME_BOX.getChildren().addAll(TOP_LINE, MIDDLE_LINE, BOTTOM_LINE);
        gameScene = new Scene(GAME_BOX, 0, 0);
        gameScene.getStylesheets().add(Data.STYLESHEET);
        TEXT_AREA.setId("typingTextArea");

        gameScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            switch (status) {
            case START:
                status = Status.IN_ROUND;
                MIDDLE_LINE.setText("WPM: 20");
                GAME_BOX.getChildren().clear();
                GAME_BOX.getChildren().addAll(MIDDLE_LINE, TEXT_AREA);
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
}

package humanbenchmark;

import humanbenchmark.Data.GameType;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * <code>ReactionGame</code> is the class for the Reaction Game Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. It is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class ReactionGame extends Data.Game {
    // CLASS ENUMS -----------------------------------------------------------\\

    private enum State {
        READY,
        RED,
        GREEN
    }


    // FINAL CLASS VARIABLES -------------------------------------------------\\

    private final GameType GAME_TYPE      = GameType.REACTION;
    private final Random   RANDOM         = new Random();
    private final long[]   REACTION_TIMES = { 0, 0, 0, 0, 0};
    private final Label    LARGE_LINE     = new Label();
    private final Label    SMALL_LINE     = new Label();
    private final Label    BUTTON_TEXT    = new Label();
    private final VBox     GAME_BOX       = new VBox();

    private final ScheduledExecutorService scheduler      =
            Executors.newScheduledThreadPool(1);


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_BEGIN    = "Begin";
    private final String STR_CONTINUE = "Continue";
    private final String STR_RETRY    = "Try Again!";
    private final String STR_SUBMIT   = "Click Me!";
    private final String STR_EXCLAIM  = "!!!";
    private final String STR_PROMPT   = "Click!";
    private final String STR_QUIT     = "Exit";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_BUTTON      = "paneButton";
    private final String ID_PANE_BLU    = "bluePane";
    private final String ID_PANE_GRN    = "reactionPaneGreen";
    private final String ID_PANE_RED    = "reactionPaneRed";
    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private long  startTime;
    private int   round = 0;
    private State state = State.READY;


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        Scene gameScene;
        LARGE_LINE.setText(GAME_TYPE.getName() + " Test");
        LARGE_LINE.setId(ID_TITLE);
        SMALL_LINE.setText(GAME_TYPE.getDescription());
        SMALL_LINE.setId(ID_DESCRIPTION);
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        BUTTON_TEXT.setText(STR_BEGIN);
        BUTTON_TEXT.setId(ID_DESCRIPTION);
        Button clickButton = new Button();
        clickButton.setGraphic(BUTTON_TEXT);
        clickButton.setId(ID_BUTTON);
        GAME_BOX.setId(ID_PANE_BLU);
        GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE, spacer, clickButton);
        gameScene = new Scene(GAME_BOX, 0, 0);
        gameScene.getStylesheets().add(Data.STYLESHEET);
        Future<?>[] change = {null};
        Runnable waitForClick = () -> Platform.runLater(() -> {
            LARGE_LINE.setText(STR_EXCLAIM);
            SMALL_LINE.setText(STR_PROMPT);
            GAME_BOX.setId(ID_PANE_GRN);
            BUTTON_TEXT.setText(STR_SUBMIT);
            state = State.GREEN;
            startTime = System.currentTimeMillis();
        });



        clickButton.setOnAction(event -> {
            if (round == 5) {
                LARGE_LINE.setText("Final Results:");
                long sum = Arrays.stream(REACTION_TIMES).sum();
                long average = sum/(round+1);
                SMALL_LINE.setText("Average: " + average + " ms");
                BUTTON_TEXT.setText(STR_QUIT);
                round++;
            } else if (round >= 6) {
                safeGameShutdown();
            } else if (state == State.READY) {
                LARGE_LINE.setText("...");
                SMALL_LINE.setText("Wait for green...");
                GAME_BOX.setId(ID_PANE_RED);
                BUTTON_TEXT.setText("Wait!");
                state = State.RED;
                int    sleepTime = RANDOM.nextInt(6000) + 2000;
                change[0] = scheduler.schedule(waitForClick, sleepTime,
                                               TimeUnit.MILLISECONDS);
            } else if (state == State.RED) {
                change[0].cancel(true);
                LARGE_LINE.setText("Too soon!\n");
                SMALL_LINE.setText("Try again!");
                GAME_BOX.setId(ID_PANE_BLU);
                BUTTON_TEXT.setText(STR_RETRY);
                state = State.READY;
            } else if (state == State.GREEN) {
                long endTime = System.currentTimeMillis();
                REACTION_TIMES[round] = (endTime - startTime);
                LARGE_LINE.setText(REACTION_TIMES[round] + " ms");
                long sum = Arrays.stream(REACTION_TIMES).sum();
                long average = sum/(round+1);
                SMALL_LINE.setText("Average: " + average + " ms");
                GAME_BOX.setId(ID_PANE_BLU);
                BUTTON_TEXT.setText(STR_CONTINUE);
                state = State.READY;
                round++;
            }
        });

        return gameScene;
    }

    @Override
    protected void safeGameShutdown() {
        scheduler.shutdownNow();
        MainDisplay.endGame();
    }
}

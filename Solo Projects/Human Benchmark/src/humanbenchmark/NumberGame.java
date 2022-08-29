package humanbenchmark;

import humanbenchmark.Data.Status;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <code>NumberGame</code> is the class for the Number Game Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. It is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class NumberGame extends Data.Game {
    // FINAL CLASS VARIABLES -------------------------------------------------\\

    private final Data.GameType GAME_TYPE   = Data.GameType.NUMBER;
    private final Random        RANDOM      = new Random();
    private final VBox          GAME_BOX    = new VBox();
    private final Label         LARGE_LINE  = new Label();
    private final Label         BUTTON_TEXT = new Label();
    private final Label         SMALL_LINE  = new Label();
    private final Button        BUTTON      = new Button();
    private final TextField     TEXT_FIELD  = new TextField();

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_BEGIN    = "Begin";
    private final String STR_CONTINUE = "Continue";
    private final String STR_SUBMIT   = "Submit";
    private final String STR_PROVIDED = "Your answer: ";
    private final String STR_CORRECT  = "Correct answer: ";
    private final String STR_LEVEL    = "You reached level ";
    private final String STR_QUIT     = "Exit";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_BUTTON      = "paneButton";
    private final String ID_FIELD       = "numberGameTextField";
    private final String ID_PANE        = "bluePane";
    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";
    private final String ID_TIMER       = "timerText";
    private final String ID_FAIL        = "numberGameTextFail";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private int    digits = 1;
    private Status status = Status.START;
    private int    time;
    private String intToMatch;


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        LARGE_LINE.setText(GAME_TYPE.getName() + " Test");
        SMALL_LINE.setText(GAME_TYPE.getDescription());
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        BUTTON_TEXT.setText(STR_BEGIN);
        BUTTON.setGraphic(BUTTON_TEXT);
        GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE, spacer, BUTTON);
        Scene gameScene = new Scene(GAME_BOX, 0, 0);


        gameScene.getStylesheets().add(Data.STYLESHEET);
        LARGE_LINE.setId(ID_TITLE);
        SMALL_LINE.setId(ID_DESCRIPTION);
        BUTTON_TEXT.setId(ID_DESCRIPTION);
        BUTTON.setId(ID_BUTTON);
        TEXT_FIELD.setId(ID_FIELD);
        GAME_BOX.setId(ID_PANE);

        Future<?>[] change = {null};
        Runnable updateTimer = () -> Platform.runLater(() -> {
            time--;
            SMALL_LINE.setText(String.valueOf(time));
            if (time == 0) {
                change[0].cancel(true);
                status = Status.CHECKING_ANSWER;
                GAME_BOX.getChildren().clear();
                LARGE_LINE.setText("Enter the number:");
                BUTTON_TEXT.setText(STR_SUBMIT);
                GAME_BOX.getChildren().addAll(LARGE_LINE, TEXT_FIELD, spacer,
                                              BUTTON);
            }
        });

        gameScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) BUTTON.fire();
        });

        BUTTON.setOnAction(event -> {
            switch (status) {
            case CHECKING_ANSWER:
                GAME_BOX.getChildren().clear();
                SMALL_LINE.setId(ID_DESCRIPTION);
                String answer = TEXT_FIELD.getText();
                TEXT_FIELD.setText("");
                LARGE_LINE.setText(STR_CORRECT + intToMatch + "\n" +
                                   STR_PROVIDED + answer);
                if (!answer.equals(intToMatch)) {
                    status = Status.DONE;
                    BUTTON_TEXT.setText(STR_QUIT);
                    LARGE_LINE.setId(ID_FAIL);
                    SMALL_LINE.setText(STR_LEVEL + digits + ".\n");
                } else {
                    digits++;
                    status = Status.BETWEEN_ROUNDS;
                    BUTTON_TEXT.setText(STR_CONTINUE);
                    SMALL_LINE.setText("Level " + digits + " reached.");
                }
                GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE,
                                              spacer, BUTTON);
                break;
            case BETWEEN_ROUNDS:
            case START:
                status = Status.IN_ROUND;
                GAME_BOX.getChildren().clear();
                getRandInt();
                LARGE_LINE.setText(intToMatch);
                time = digits + 1;
                SMALL_LINE.setText(String.valueOf(time));
                SMALL_LINE.setId(ID_TIMER);
                GAME_BOX.getChildren().addAll(LARGE_LINE, spacer, SMALL_LINE);
                change[0] = scheduler.scheduleAtFixedRate(
                        updateTimer, 1, 1, TimeUnit.SECONDS);
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

    private void getRandInt() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < digits; i++) {
            int digit;
            if (i == 0) digit = RANDOM.nextInt(9) + 1;
            else digit = RANDOM.nextInt(10);
            stringBuilder.append(digit);
        }
        intToMatch = stringBuilder.toString();
    }
}

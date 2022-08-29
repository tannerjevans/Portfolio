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
 * <code>MathGame</code> is the class for the Math Game Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. The Math Game is the one minigame not present on the Human
 * Benchmark website. It is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class MathGame extends Data.Game {
    // CLASS ENUMS -----------------------------------------------------------\\

    private enum Operation {
        ADD  (0),
        SUB  (1);

        private final int value;

        Operation(int value) {
            this.value = value;
        }

        protected static Operation getType(int value) {
            return Operation.values()[value];
        }
    }


    // FINAL CLASS VARIABLES -------------------------------------------------\\

    private final Data.GameType GAME_TYPE   = Data.GameType.MATH;
    private final Random        RANDOM      = new Random();
    private final VBox          GAME_BOX    = new VBox();
    private final Label         LARGE_LINE  = new Label();
    private final Label         BUTTON_TEXT = new Label();
    private final Label         SMALL_LINE  = new Label();
    private final Button        BUTTON      = new Button();
    private final TextField     TEXT_FIELD  = new TextField();
    private final Future<?>[]   CHANGE      = {null};

    private final ScheduledExecutorService scheduler  =
            Executors.newScheduledThreadPool(1);


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_BEGIN    = "Begin";
    private final String STR_CONTINUE = "Continue";
    private final String STR_SUBMIT   = "Submit";
    private final String STR_PROVIDED = "Your answer: ";
    private final String STR_CORRECT  = "\n Correct answer: ";
    private final String STR_LEVEL    = "Level: ";
    private final String STR_LIVES    = "  Lives: ";
    private final String STR_QUIT     = "Exit";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_BUTTON      = "paneButton";
    private final String ID_FIELD       = "numberGameTextField";
    private final String ID_PANE        = "bluePane";
    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";
    private final String ID_TIMER       = "timerText";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private int      level  = 1;
    private int      lives  = 3;
    private Status   status = Status.START;
    private int      time;
    private int      correctAnswer;
    private int      providedAnswer;
    private String   equation;
    private Runnable UPDATE_TIMER;


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        LARGE_LINE.setText(GAME_TYPE.getName());
        SMALL_LINE.setText(GAME_TYPE.getDescription());
        BUTTON_TEXT.setText(STR_BEGIN);
        Region spacer = new Region();
        spacer.setPrefHeight(50);
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

        UPDATE_TIMER = () -> Platform.runLater(() -> {
            time--;
            SMALL_LINE.setText(String.valueOf(time));
            if (time == 0) {
                BUTTON.fire();
            }
        });

        gameScene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) BUTTON.fire();
        });

        BUTTON.setOnAction(event -> {
            switch (status) {
            case IN_ROUND:
                CHANGE[0].cancel(true);
                GAME_BOX.getChildren().clear();
                try {
                    providedAnswer = Integer.parseInt(TEXT_FIELD.getText());
                } catch (Exception exception) {
                    providedAnswer = 0;
                }
                TEXT_FIELD.clear();
                SMALL_LINE.setId(ID_TITLE);
                BUTTON_TEXT.setText(STR_CONTINUE);
                if (correctAnswer == providedAnswer) {
                    level++;
                    status = Status.BETWEEN_ROUNDS;
                } else {
                    lives--;
                    if (lives == 0) {
                        BUTTON_TEXT.setText(STR_QUIT);
                        status = Status.DONE;
                    } else {
                        status = Status.BETWEEN_ROUNDS;
                    }
                }
                LARGE_LINE.setText(STR_PROVIDED + providedAnswer +
                                   STR_CORRECT + correctAnswer);
                SMALL_LINE.setText(STR_LEVEL + level + STR_LIVES + lives);
                GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE, BUTTON);
                break;
            case START:
            case BETWEEN_ROUNDS:
                newRound();
                status = Status.IN_ROUND;
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

    private void newRound() {
        GAME_BOX.getChildren().clear();
        time = 6;
        getEquationAndAnswer();
        LARGE_LINE.setText(equation);
        SMALL_LINE.setText(String.valueOf(time));
        SMALL_LINE.setId(ID_TIMER);
        BUTTON_TEXT.setText(STR_SUBMIT);
        GAME_BOX.getChildren().addAll(LARGE_LINE, TEXT_FIELD, SMALL_LINE,
                                      BUTTON);
        TEXT_FIELD.requestFocus();
        CHANGE[0] = scheduler.scheduleAtFixedRate(
                UPDATE_TIMER, 1, 1, TimeUnit.SECONDS);
    }

    private void getEquationAndAnswer() {
        Operation operation = Operation.getType(RANDOM.nextInt(2));
        int first, second;
        switch (operation) {
        case ADD:
            first          = RANDOM.nextInt(level*3 + 1) + 1;
            second         = RANDOM.nextInt(level*3 + 1) + 1;
            equation       = first + " + " + second;
            correctAnswer = first + second;
            break;
        case SUB:
            first  = RANDOM.nextInt(level*3 + 1) + 1;
            second = RANDOM.nextInt(level*3 + 1) + 1;
            if (second > first) {
                int temp = second;
                second   = first;
                first    = temp;
            }
            if (second == first) {
                first++;
            }
            equation       = first + " - " + second;
            correctAnswer = first - second;
            break;
        }
    }
}

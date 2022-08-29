package humanbenchmark;

import humanbenchmark.Data.Status;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Random;

/**
 * <code>VerbalGame</code> is the class for the Verbal Memory Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. It is not for extra-application use.
 *
 * The Verbal Memory Minigame utilizes a text file that contains over 400000
 * English words to randomly populate the list of words to be selected from.
 * I was under the impression that there were only about 180000 English
 * words, so I'm not sure how that's possible, even given plurals and
 * alternate forms, but all the words seem legitimate upon testing.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */

public class VerbalGame extends Data.Game {
    // FINAL CLASS VARIABLES -------------------------------------------------\\

    private final Data.GameType      GAME_TYPE  = Data.GameType.VERBAL;
    private final Random             RANDOM     = new Random();
    private final VBox               GAME_BOX   = new VBox();
    private final HBox               BUTTON_BOX = new HBox();
    private final Label              LARGE_LINE = new Label();
    private final Label              SMALL_LINE = new Label();
    private final Button             NEW_BUTTON = new Button();
    private final Button             OLD_BUTTON = new Button();
    private final LinkedList<String> NEW_LIST   = new LinkedList<>();
    private final LinkedList<String> OLD_LIST   = new LinkedList<>();


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_BEGIN = "\n\n Click to begin.";
    private final String STR_QUIT  =
            "Click anywhere to return to the main menu.";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_BUTTON      = "paneButton";
    private final String ID_BUTTON_BOX  = "verbalButtonBox";
    private final String ID_PANE        = "bluePane";
    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private int     score = 0;
    private int     lives = 3;
    private boolean newWord;
    private Status status = Status.START;


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        try {
            constructRandomList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LARGE_LINE.setText(GAME_TYPE.getName() + " Test");
        SMALL_LINE.setText(GAME_TYPE.getDescription() + STR_BEGIN);
        Region spacer = new Region();
        spacer.setPrefHeight(50);
        GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE);
        Scene gameScene = new Scene(GAME_BOX, 0, 0);
        Label oldButtonLabel = new Label();
        oldButtonLabel.setText("SEEN");
        OLD_BUTTON.setGraphic(oldButtonLabel);
        Label newButtonLabel = new Label();
        newButtonLabel.setText("NEW");
        NEW_BUTTON.setGraphic(newButtonLabel);
        BUTTON_BOX.getChildren().addAll(OLD_BUTTON, NEW_BUTTON);
        BUTTON_BOX.setId(ID_BUTTON_BOX);

        gameScene.getStylesheets().add(Data.STYLESHEET);
        newButtonLabel.setId(ID_DESCRIPTION);
        NEW_BUTTON.setId(ID_BUTTON);
        OLD_BUTTON.setId(ID_BUTTON);
        GAME_BOX.setId(ID_PANE);
        SMALL_LINE.setId(ID_DESCRIPTION);
        LARGE_LINE.setId(ID_TITLE);
        oldButtonLabel.setId(ID_DESCRIPTION);

        gameScene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            switch (status) {
            case START:
                status = Status.IN_ROUND;
                newRound();
            case IN_ROUND:
                break;
            case DONE:
                safeGameShutdown();
            }
        });

        OLD_BUTTON.setOnAction(event -> {
            if (!newWord) {
                score++;
                newRound();
            } else {
                wrongAnswer();
            }
        });

        NEW_BUTTON.setOnAction(event -> {
            if (newWord) {
                score++;
                newRound();
            } else {
                wrongAnswer();
            }
        });

        return gameScene;
    }

    @Override
    protected void safeGameShutdown() {
        MainDisplay.endGame();
    }


    // PRIVATE CLASS MEMBERS -------------------------------------------------\\

    private void newRound() {
        GAME_BOX.getChildren().clear();
        SMALL_LINE.setText("Lives | " + lives + "   Score | " + score);
        String word;
        if (OLD_LIST.size() < 4 || RANDOM.nextBoolean()) {
            newWord = true;
            int index = RANDOM.nextInt(NEW_LIST.size());
            word = NEW_LIST.remove(index);
        } else {
            newWord = false;
            int index = RANDOM.nextInt(OLD_LIST.size());
            word = OLD_LIST.get(index);
        }
        OLD_LIST.add(word);
        LARGE_LINE.setText(word);
        GAME_BOX.getChildren().addAll(SMALL_LINE, LARGE_LINE, BUTTON_BOX);
    }

    private void wrongAnswer() {
        lives--;
        if (lives == 0) {
            GAME_BOX.getChildren().clear();
            LARGE_LINE.setText("Game over! \n" +
                               "Final Score | " + score);
            SMALL_LINE.setText(STR_QUIT);
            GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE);
            status = Status.DONE;
        } else {
            newRound();
        }
    }

    private void constructRandomList() throws IOException {
        RandomAccessFile words = new RandomAccessFile(Data.FILE, "r");
        NEW_LIST.clear();
        for (int i = 0; i < 500; i++) {
            boolean unique = false;
            String newWord = null;
            while (!unique) {
                long location = RANDOM.nextInt(4234800);
                words.seek(location);
                words.readLine();
                newWord = words.readLine();
                unique = true;
                for (String newString : NEW_LIST) {
                    if (newWord.equals(newString)) {
                        unique = false;
                        break;
                    }
                }
            }
            NEW_LIST.add(newWord);
        }
        words.close();
    }
}

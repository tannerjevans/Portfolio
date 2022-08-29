package humanbenchmark;

import humanbenchmark.Data.GameType;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

/**
 * <code>AimGame</code> is the class for the Aim Trainer Minigame. It is
 * created by <code>MainDisplay</code>, and its members contain the logic of
 * its operation. It is not for extra-application use.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class AimGame extends Data.Game {
    // FINAL CLASS VARIABLES -------------------------------------------------\\

    private final GameType GAME_TYPE  = GameType.AIM;
    private final Random   RANDOM     = new Random();
    private final Pane     FIELD      = new Pane();
    private final Label    LARGE_LINE = new Label();
    private final Label    SMALL_LINE = new Label();
    private final VBox     GAME_BOX   = new VBox();


    // CLASS STRINGS ---------------------------------------------------------\\

    private final String STR_BEGIN     = "\n Click to begin.";
    private final String STR_TARGETS   = "Targets Remaining: ";
    private final String STR_RESULTS_1 = "You hit 30 targets in";
    private final String STR_RESULTS_2 = "seconds.";
    private final String STR_FORMAT    = " %2.2f ";
    private final String STR_QUIT    =
            "Click anywhere to return to the main menu.";


    // CSS IDS ---------------------------------------------------------------\\

    private final String ID_TITLE       = "gameTitle";
    private final String ID_DESCRIPTION = "gameDescription";
    private final String ID_GAME        = "aimPane";


    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    private long  startTime     = -1;
    private int   targets       = 30;
    private Group currentTarget;


    // PROTECTED CLASS MEMBERS -----------------------------------------------\\

    @Override
    protected Scene getScene(){
        LARGE_LINE.setText(GAME_TYPE.getName());
        SMALL_LINE.setText(GAME_TYPE.getDescription() + STR_BEGIN);
        GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE);
        Scene gameScene = new Scene(GAME_BOX, 0, 0);

        gameScene.getStylesheets().add(Data.STYLESHEET);
        LARGE_LINE.setId(ID_TITLE);
        SMALL_LINE.setId(ID_DESCRIPTION);
        GAME_BOX.setId(ID_GAME);

        currentTarget = getTarget();
        FIELD.getChildren().add(currentTarget);
        MainDisplay.constrainElement(FIELD, 1150, 700);

        gameScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (startTime == -1) {
                startTime = System.currentTimeMillis();
                SMALL_LINE.setText(STR_TARGETS + targets);
                GAME_BOX.getChildren().remove(LARGE_LINE);
                GAME_BOX.getChildren().add(FIELD);
            }
        });

        return gameScene;
    }

    @Override
    protected void safeGameShutdown() {
        MainDisplay.endGame();
    }


    // PRIVATE CLASS MEMBERS -------------------------------------------------\\

    private Group getTarget() {
        Group  target      = new Group();
        int    xPos        = RANDOM.nextInt(1050) + 50;
        int    yPos        = RANDOM.nextInt(550) + 100;
        Circle outerRing   = new Circle(xPos, yPos, 75, Color.DARKRED);
        Circle outerBlank  = new Circle(xPos, yPos,60, Color.WHITE);
        Circle middleRing  = new Circle(xPos, yPos, 45, Color.DARKRED);
        Circle middleBlank = new Circle(xPos, yPos,30, Color.WHITE);
        Circle center      = new Circle(xPos, yPos, 15, Color.DARKRED);

        target.getChildren().addAll(outerRing, outerBlank, middleRing,
                                    middleBlank, center);

        target.setOnMousePressed(event -> {
            if (targets == 1) {
                long   endTime   = System.currentTimeMillis();
                long   totalTime = (endTime - startTime);

                String time = String.format(STR_FORMAT, (float)totalTime/1000);

                GAME_BOX.getChildren().clear();
                GAME_BOX.getChildren().addAll(LARGE_LINE, SMALL_LINE);

                LARGE_LINE.setText(STR_RESULTS_1 + time + STR_RESULTS_2);
                SMALL_LINE.setText(STR_QUIT);
                GAME_BOX.addEventFilter(MouseEvent.MOUSE_CLICKED,
                                       event2 -> safeGameShutdown());

            } else {
                targets--;
                SMALL_LINE.setText(STR_TARGETS + targets);

                Group newTarget = getTarget();
                currentTarget   = newTarget;

                FIELD.getChildren().remove(target);
                FIELD.getChildren().add(newTarget);
            }
        });

        return target;
    }
}

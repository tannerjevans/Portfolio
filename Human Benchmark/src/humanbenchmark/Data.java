package humanbenchmark;

import javafx.scene.Scene;

import java.io.File;

/**
 * <code>Data</code> is an abstract <code>class</code> which serves to
 * centralize common data structures, minimize repetitive code, and provide a
 * known location for application-wide setting which may change for the
 * HumanBenchmark application. Notably, its members are for in-package use
 * only, and as such, only important members will be significantly commented,
 * and all will be internal, non JavaDoc comments.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public abstract class Data {
    // PROJECT-WIDE DATA AND STRUCTURES --------------------------------------\\

    protected static final String TITLE = "Human Benchmark";
    protected static final String SUBTITLE =
            "A series of games and tests to measure your cognitive abilities.";
    protected static final int WIDTH = 1200;
    protected static final int HEIGHT = 850;
    protected static final int PADDING = 10;
    /* HumanBenchmark uses a Cascading Stylesheet as an attempt to decant as
     * much styling from the programmatic portions of the application into a
     * central space. While css files are long, they provide an opportunity
     * to modify the presentation of elements quickly and cleanly. */

    protected static final String STYLESHEET =
            HumanBenchmark.class.getResource(
                    "MainStyle.css").toExternalForm();
    // .toExternalForm();
    protected static final File FILE =
            new File(HumanBenchmark.class.getResource(
                    "Words.txt").getFile());

    /*
     * An abstract Game class was chosen in order to enable easier passing of
     * the minigame objects around the application. Since JavaFX Nodes carry
     * the event handling with them, this enables a very clean and
     * generalized MainDisplay class. This also enforces the use of a
     * safeGameShutdown() method, which should be utilized to ensure that any
     * processes brought into being during the runtime of a minigame are
     * closed prior to the closing of the window.
     */
    protected static abstract class Game {
        /*
         * Each minigame provides a basic scene, whose elements are
         * instantiated with event handlers and even filters to perform as
         * expected.
         */
        protected abstract Scene getScene();
        protected abstract void safeGameShutdown();
    }

    protected enum Status {
        START,
        IN_ROUND,
        CHECKING_ANSWER,
        BETWEEN_ROUNDS,
        DONE
    }

    /*
     * The GameType enum is a bit of a monster, but it provides a generalized
     * selector for game-type-specific data, distilling selection logic into
     * a single structure. I found this to vastly ease the addition and
     * modification of mini-game metadata.
     */
    protected enum GameType {
        REACTION(0),
        AIM     (1),
        CHIMP   (2),
        VISUAL  (3),
        NUMBER  (5),
        VERBAL  (6),
        MATH    (7);

        private final int value;

        GameType(int value) {
            this.value = value;
        }

        protected static GameType getType(int value) {
            return GameType.values()[value];
        }

        protected String getName() {
            switch (this) {
            case REACTION:
                return "Reaction Time";
            case AIM:
                return "Aim Test";
            case CHIMP:
                return "Chimp Test";
            case VISUAL:
                return "Visual Memory Test";
            case NUMBER:
                return "Number Memory";
            case VERBAL:
                return "Verbal Memory";
            case MATH:
                return "Math Test";
            }
            return null;
        }

        protected String getDescription() {
            switch (this) {
            case REACTION:
                return "Click the button as quickly as possible after the " +
                       "screen changes from red to green.";
            case AIM:
                return "Click 30 randomly-placed targets as quickly as " +
                       "possible.";
            case CHIMP:
                return "Click the squares in the order of their numbers.\n" +
                       "After your first click, the numbers will disappear.";
            case VISUAL:
                return "Each round, a number of tiles will momentarily flip" +
                       " themselves.\n" +
                       "Once they disappear, select all the tiles that " +
                       "flipped.\n\n" +
                       "You have three strikes per round, and you can fail " +
                       "three\nrounds before the game is over.";
            case NUMBER:
                return "Memorize increasingly long numbers.";
            case VERBAL:
                return "Identify whether a word has been shown to you or not " +
                       "throughout the test.";
            case MATH:
                return "Solve the provided equation before the timer runs out" +
                       ".\nYou may press enter to submit.";
            }
            return null;
        }

        protected Game makeGame() {
            switch (this) {
            case REACTION:
                return new ReactionGame();
            case AIM:
                return new AimGame();
            case CHIMP:
                return new ChimpGame();
            case VISUAL:
                return new VisualGame();
            case NUMBER:
                return new NumberGame();
            case VERBAL:
                return new VerbalGame();
            case MATH:
                return new MathGame();
            }
            return null;
        }

    }

}

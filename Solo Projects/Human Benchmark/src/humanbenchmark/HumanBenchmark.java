package humanbenchmark;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * <code>HumanBenchmark</code> is the main entry point for the HumanBenchmark
 * application. All versions have a JAR file which can be run via
 * <code>java -jar [jarName].jar</code>. The latest version is recommended.
 * Incomplete versions are not stable.
 *
 * Non-public members of other classes will be commented where it would seem
 * helpful, but JavaDoc comments will be reserved for publicly-visible
 * elements.
 *
 * This application makes use of a CSS for non-programmatic stylization.
 * Where possible, I have placed the CSS IDs as final Strings near the
 * beginning of each class (as well as other Strings) to clean up code and
 * improve editability.
 *
 * @author  Tanner J. Evans
 * @version %I%, %G%
 * @since   11.0.8
 */
public class HumanBenchmark extends Application {
    // MUTABLE CLASS VARIABLES -----------------------------------------------\\

    protected static Scene startScene;
    protected static VBox  previewPane = new VBox();


    // PUBLIC CLASS MEMBERS --------------------------------------------------\\

    /**
     * The <code>main method</code> for HumanBenchmark. Initializes the game
     * and calls <code>launch()</code>.
     * @param args  The arguments passed to the program when started via the
     *              command line.
     */
    public static void main(String[] args) { launch(args); }

    /**
     * <code>start</code> is the <code>method</code> that JavaFX applications
     * begin in. In the case of HumanBenchmark, most application logic has
     * been outsourced to other classes.
     * @param mainStage  The stage provided as part of the initialization
     *                   process performed by launch(args).
     */
    @Override
    public void start(Stage mainStage) {
        Platform.setImplicitExit(true);
        mainStage.setTitle(Data.TITLE);
        MainDisplay.conditionWindow(mainStage, Data.TITLE);
        MainDisplay.makeStartScene();
        mainStage.setScene(startScene);
        MainDisplay.conditionWindow(mainStage, Data.TITLE);
        mainStage.show();
    }
}

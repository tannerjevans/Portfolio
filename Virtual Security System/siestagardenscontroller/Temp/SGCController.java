

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

 /**
 * This is the main class for this project. This class will create the main
 * front end GUI that a system operator will see and it will change
 * what is displayed based on the buttons clicked on by the user.
 * 
 * @author Jacob Varela
 */
public class SGCController extends Application {

    // Launch the program.
    public static void main(String[] args) {
        launch(args);
    }

    // The main pane for our system's GUI.
    BorderPane rootPane;

    // Global variables for the GUI.
    VBox statusBarVBox, alarmVBox, atsVBox, carVBox, exhibitVBox, navBarVBox;
    HBox statusBarHBox, alarmHBox, atsHBox, carHBox, exhibitHBox;

    // Used to outline the top, middle, and left/right sides the main Border Pane.
    Border SECTIONBORDER = new Border( new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0) , new BorderWidths(2)));

    // Used for setting the background colors of some GUI elements.
    String BACKGROUNDGOLD      = "-fx-background-color: #7dd6ff";
    String BACKGROUNDSILVER    = "-fx-background-color: #d6d6d6";
    String BACKGROUNDTURQUOISE = "-fx-background-color: #007A86";
    String BASESILVER = "-fx-base-color: #d6d6d6;";

    // Used for setting the status lights for each of the systems.
    Circle alarmLight1, alarmLight2, alarmLight3;
    Circle atsLight1, atsLight2, atsLight3;
    Circle carLight1, carLight2, carLight3;
    Circle exhibitLight1, exhibitLight2, exhibitLight3;
    LinearGradient greenGradient, yellowGradient, redGradient;
    int CIRCLERADIUS = 15;

    // Buttons for the Navigation bar.
    ToggleButton alarmBtn, atsBtn, cameraBtn, carBtn, exhibitBtn, mapBtn;
    ToggleGroup navGroup;

    // Used for rounding the corners of some GUI elements.
    String ROUNDCORNERS = "-fx-background-radius: 20px";

    // Create instances of each subsystem.
    // AlarmSystem alarmSystem = new AlarmSystem();
    // ATS ats = new ATS();
    // Cameras cameras = new Cameras();
    // Cars cars = new Cars();
    // Sys_Exhibit exhibit = new Sys_Exhibit();
    // Map map = new map();

    /**
     * Creates the primaryStage to hold the GUI elements.
     * Creates a BorderPane to separate and organize the display.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        rootPane = createRootPane();

        Scene scene = new Scene(rootPane, 1200, 700);

        // Restrict the minimum size of the stage so the user can't make the window too small.
        primaryStage.sizeToScene();
        primaryStage.setMinWidth(rootPane.getWidth());
        primaryStage.setMinHeight(rootPane.getHeight());

        primaryStage.getIcons().add(new Image("file:resources/unmLobo.png"));
        primaryStage.setTitle("Siesta Gardens Controller - Team T02");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * 
     * @return The rootPane with a Status Bar and Navigation Bar.
     */
    private BorderPane createRootPane() {
        
        // Create a new BorderPane.
        BorderPane rootPane = new BorderPane();

        // Creates status lights for each system.
        createStatusLights();

        createStatusBar(rootPane);

        createNavigationBar(rootPane);
        
        // Set the rootPane's minimum size values based on the Status Bar and Navigation Bar size.
        rootPane.setMinSize(statusBarVBox.getMinWidth(), statusBarVBox.getMinHeight() + navBarVBox.getMinHeight());
        
        // Add a black border around the rootpane sections.
        rootPane.setBorder(SECTIONBORDER);

        return rootPane;
    }

    /**
     * This function creates and adds three lights to each of the
     * system HBox's that will be located on the status bar.
     * Each light is initially set to a red light until it connects its 
     * corresponding system.
     */
    private void createStatusLights() {
        
        // Indicate how far apart the 3 lights are from each other
        int circleSpacing = 10;
        
        // Creates a green gradient that can be assigned to the lights. 
        Stop[] stopsGreen = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.FORESTGREEN)};
        greenGradient = new LinearGradient(0, 0, 0.6, 0.6, true, CycleMethod.NO_CYCLE, stopsGreen);

        // Creates a red gradient that can be assigned to the lights. 
        Stop[] stopsRed = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.RED)};
        redGradient = new LinearGradient(0, 0, 0.75, 0.75, true, CycleMethod.NO_CYCLE, stopsRed);

        // Creates a yellow gradient that can be assigned to the lights. 
        Stop[] stopsYellow = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.GOLD)};
        yellowGradient = new LinearGradient(0, 0, 0.75, 0.75, true, CycleMethod.NO_CYCLE, stopsYellow);

        // Initialize the alarm lights.
        alarmLight1  = new Circle(CIRCLERADIUS);
        alarmLight2  = new Circle(CIRCLERADIUS);
        alarmLight3  = new Circle(CIRCLERADIUS);
   
        // Initialize the ats lights.
        atsLight1 = new Circle(CIRCLERADIUS);
        atsLight2 = new Circle(CIRCLERADIUS);
        atsLight3 = new Circle(CIRCLERADIUS);

        // Initialize the car lights.
        carLight1 = new Circle(CIRCLERADIUS);
        carLight2 = new Circle(CIRCLERADIUS);
        carLight3 = new Circle(CIRCLERADIUS);

        // Initialize the exhibit lights.
        exhibitLight1 = new Circle(CIRCLERADIUS);
        exhibitLight2 = new Circle(CIRCLERADIUS);
        exhibitLight3 = new Circle(CIRCLERADIUS);

        // Add the alarm lights to the alarmHBox and add HBox formatting.
        alarmHBox = new HBox(alarmLight1, alarmLight2, alarmLight3);
        alarmHBox.setAlignment(Pos.CENTER);
        alarmHBox.setSpacing(circleSpacing);

        // Add the ats lights to the atsHBox and add HBox formatting.
        atsHBox = new HBox(atsLight1, atsLight2, atsLight3);
        atsHBox.setAlignment(Pos.CENTER);
        atsHBox.setSpacing(circleSpacing);

        // Add the car lights to the carHBox and add HBox formatting.
        carHBox = new HBox(carLight1, carLight2, carLight3);
        carHBox.setAlignment(Pos.CENTER);
        carHBox.setSpacing(circleSpacing);  
        
        // Add the exhibit lights to the exhibitHBox and add HBox formatting.
        exhibitHBox = new HBox(exhibitLight1, exhibitLight2, exhibitLight3);
        exhibitHBox.setAlignment(Pos.CENTER);
        exhibitHBox.setSpacing(circleSpacing);  

        // Set each status to an initial color.
        setStatusLights("alarm"  , "green");
        setStatusLights("ats"    , "red");
        setStatusLights("car"    , "yellow");
        setStatusLights("exhibit", "green");
    } 

    /**
     * This function will change the status lights of a system on the status bar. 
     * @param system lowercase String that indicating which system lights need to be updated.
     * @param colorChoice lowercase String indicating which color the lights will change to (defaults red)
     */
    private void setStatusLights(String system, String colorChoice) {

        // Initialize the newColor to red.
        LinearGradient newColor = redGradient;

        switch(colorChoice) {
            case "red":
                newColor = redGradient;
                break;

            case "green":
                newColor = greenGradient;
                break;

            case "yellow":
                newColor = yellowGradient;
                break;

            default:
                break;
        }
        
        // Change the status box circles based on the input system and colorChoice.
        switch(system) {
            case "alarm":
                alarmLight1  = new Circle(CIRCLERADIUS, newColor);
                alarmLight2  = new Circle(CIRCLERADIUS, newColor);
                alarmLight3  = new Circle(CIRCLERADIUS, newColor);
                alarmHBox.getChildren().clear();
                alarmHBox.getChildren().addAll(alarmLight1, alarmLight2, alarmLight3);
                break;

            case "ats":
                atsLight1  = new Circle(CIRCLERADIUS, newColor);
                atsLight2  = new Circle(CIRCLERADIUS, newColor);
                atsLight3  = new Circle(CIRCLERADIUS, newColor);
                atsHBox.getChildren().clear();
                atsHBox.getChildren().addAll(atsLight1, atsLight2, atsLight3);
                break;

            case "car":
                carLight1  = new Circle(CIRCLERADIUS, newColor);
                carLight2  = new Circle(CIRCLERADIUS, newColor);
                carLight3  = new Circle(CIRCLERADIUS, newColor);
                carHBox.getChildren().clear();
                carHBox.getChildren().addAll(carLight1, carLight2, carLight3);
                break;

            case "exhibit":
                exhibitLight1  = new Circle(CIRCLERADIUS, newColor);
                exhibitLight2  = new Circle(CIRCLERADIUS, newColor);
                exhibitLight3  = new Circle(CIRCLERADIUS, newColor);
                exhibitHBox.getChildren().clear();
                exhibitHBox.getChildren().addAll(exhibitLight1, exhibitLight2, exhibitLight3);
                break;      

            default:
                break;
        }
    }

    /**
     * This function will create the buttons to navigate through each system.
     * It will place them on the left side of the given rootPane.
     * @param rootPane The BorderPane that the navigation bar will be applied to.
     */
    private void createNavigationBar(BorderPane rootPane) {

        // Initialize our Toggle Group
        navGroup = new ToggleGroup();

        // Used to set the navBar Gui settings
        int navBarSpacing = 20;
        int navBarHeight  = 550;
        int navBarWidth   = 250;

        // Create Navigation bar label.
        Label navLabel = new Label("Navigation Bar");
        navLabel.setFont(Font.font("Cambria", FontPosture.ITALIC, 20));
        navLabel.setTextFill(Color.WHITE);

        // Initialize the alarm system button for the Navigation Bar.
        alarmBtn = new ToggleButton("Alarm System");
        applyNavButtonStyle(alarmBtn);

        // Initialize the ats button for the Navigation Bar.
        atsBtn = new ToggleButton("ATS");
        applyNavButtonStyle(atsBtn);
        
        // Initialize the camera button for the Navigation Bar.
        cameraBtn  = new ToggleButton("Security Cameras");
        applyNavButtonStyle(cameraBtn);

        // Initialize the Self-Driving Sys_SelfDrivingCar.Car button for the Navigation Bar.
        carBtn = new ToggleButton("Self-Driving Cars");
        applyNavButtonStyle(carBtn);

        // Initialize the Park Map button for the Navigation Bar.
        mapBtn = new ToggleButton("Park Map");
        applyNavButtonStyle(mapBtn);

        // Initialize the T-Rex Sys_Exhibit button for the Navigation Bar.
        exhibitBtn = new ToggleButton("T-Red Sys_Exhibit");
        applyNavButtonStyle(exhibitBtn);

        // Toggle Bar listener
        navGroup.selectedToggleProperty().addListener(
            new ChangeListener<Toggle>(){
                public void changed(ObservableValue<? extends Toggle> ov,
                    Toggle toggle, Toggle newToggle){
                        if(newToggle == null){
                            //rootPane.setCenter(homeScreenVBox);
                        }
                        else{
                            
                        }
                    }
                }
        );

        // Add all buttons to the navigation bar.
        navBarVBox = new VBox(navLabel, alarmBtn, atsBtn, cameraBtn, carBtn, mapBtn, exhibitBtn);

        // Navigation bar formatting.
        navBarVBox.setAlignment(Pos.CENTER);
        navBarVBox.setSpacing(navBarSpacing);
        navBarVBox.setStyle(BACKGROUNDTURQUOISE);
        navBarVBox.setMinHeight(navBarHeight);
        navBarVBox.setMinWidth(navBarWidth);
        navBarVBox.setBorder(SECTIONBORDER);

        // Add the Navigation Bar to the rootPane.
        rootPane.setLeft(navBarVBox);
    }

    /**
     * Apply a consistent button size, background, and font. 
     * Also add this button to the Toggle Group.
     * @param navBtn
     */
    private void applyNavButtonStyle(ToggleButton navBtn) {

        // Set the button size
        int minWidth = 175;
        int minHeight = 50;
        navBtn.setMinSize(minWidth, minHeight);

        // Add the button to the Toggle Group
        navBtn.setToggleGroup(navGroup);
        
        navBtn.setStyle(BASESILVER);
        navBtn.setFont(Font.font("Cambria", 17));
    }

    /**
     * This function will set up the different status bar indicators for each
     * of the controller's monitoring systems and place them on the
     * top of the given BorderPane.
     * @param rootPane This is the pane the status bar will be applied to.
     */
    private void createStatusBar(BorderPane rootPane) {

        // Variables to change size and layout of each system light section.
        int statusFontSize = 15;
        int statusWidth    = 150;
        int statusHeight   = 80;
        int statusSpacing  = 10;

        // Used to create a black border with rounded edges.
        Border statusBorder = new Border( new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(15) , new BorderWidths(4)));

        // Create VBox to store the Status Bar items.
        statusBarVBox = new VBox();
        statusBarVBox.setStyle(BACKGROUNDTURQUOISE);
        statusBarVBox.setMinHeight(150);
        statusBarVBox.setAlignment(Pos.CENTER);
        
        // Create status bar label.
        Label statusLabel = new Label("System Status Bar");
        statusLabel.setFont(Font.font("Cambria", FontPosture.ITALIC, 20));
        statusLabel.setTextFill(Color.WHITE);

        // Create alarm label.
        Label alarmLabel = new Label("Alarm System");
        alarmLabel.setFont(Font.font("Cambria", FontWeight.BOLD, statusFontSize));

        // Create VBox to store the current status of the Alarm System.
        alarmVBox = new VBox(alarmLabel, alarmHBox);
        alarmVBox.setAlignment(Pos.CENTER);
        alarmVBox.setStyle(BACKGROUNDSILVER + ";" + ROUNDCORNERS);
        alarmVBox.setMinSize(statusWidth, statusHeight);
        alarmVBox.setBorder(statusBorder);
        alarmVBox.setSpacing(statusSpacing);

        // Create ats label.
        Label atsLabel = new Label("ATS");
        atsLabel.setFont(Font.font("Cambria", FontWeight.BOLD, statusFontSize));

        // Create VBox to store the current status of the ATS.
        atsVBox = new VBox(atsLabel, atsHBox);
        atsVBox.setAlignment(Pos.CENTER);
        atsVBox.setStyle(BACKGROUNDSILVER + ";" + ROUNDCORNERS);
        atsVBox.setMinSize(statusWidth, statusHeight);
        atsVBox.setBorder(statusBorder);
        atsVBox.setSpacing(statusSpacing);

        // Create Self-Driving Cars label.
        Label carLabel = new Label("Self-Driving Cars");
        carLabel.setFont(Font.font("Cambria", FontWeight.BOLD, statusFontSize));

        // Create VBox to store the current status of the Self-Driving Cars.
        carVBox = new VBox(carLabel, carHBox);
        carVBox.setAlignment(Pos.CENTER);
        carVBox.setStyle(BACKGROUNDSILVER + ";" + ROUNDCORNERS);
        carVBox.setMinSize(statusWidth, statusHeight);
        carVBox.setBorder(statusBorder);
        carVBox.setSpacing(statusSpacing);

        // Create Sys_Exhibit label.
        Label exhibitLabel = new Label("T-Rex Sys_Exhibit");
        exhibitLabel.setFont(Font.font("Cambria", FontWeight.BOLD, statusFontSize));

        // Create VBox to store the current status of the T-Rex Sys_Exhibit.
        exhibitVBox = new VBox(exhibitLabel, exhibitHBox);
        exhibitVBox.setAlignment(Pos.CENTER);
        exhibitVBox.setStyle(BACKGROUNDSILVER + ";" + ROUNDCORNERS);
        exhibitVBox.setMinSize(statusWidth, statusHeight);
        exhibitVBox.setBorder(statusBorder);
        exhibitVBox.setSpacing(statusSpacing);

        // Create HBox to store the current status of each system.
        statusBarHBox = new HBox();
        statusBarHBox.setAlignment(Pos.CENTER);
        statusBarHBox.getChildren().addAll(
            alarmVBox, atsVBox, carVBox, exhibitVBox);
        statusBarHBox.setSpacing(50);

        // Add Label and status objects to the main VBox.
        statusBarVBox.getChildren().addAll(statusLabel, statusBarHBox);
        statusBarVBox.setAlignment(Pos.CENTER);
        statusBarVBox.setSpacing(10);
        statusBarVBox.setBorder(SECTIONBORDER);
        statusBarVBox.setMinHeight(150);
        
        // Add the status bar to the rootPane.
        rootPane.setTop(statusBarVBox);
    }

 }
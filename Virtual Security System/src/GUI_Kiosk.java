/**
 * @author Thomas Bowidowicz
 * CS460 - Spring 2021
 * GUI_Kiosk class which serves as the point of user interaction at the entrace to the park
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class GUI_Kiosk {

    public Stage getStage() {
        Stage stage = new Stage();
        Sys_AutomatedTicket ats = new Sys_AutomatedTicket();

        // Welcome Screen
        // Welcome text
        Text welcome = new Text("Welcome to Siesta Gardens!");
        welcome.setFill(Color.BLACK);
        welcome.setFont(Font.font("Cambria", FontWeight.BOLD, 34));
        welcome.setX(250);
        welcome.setY(150);

        // Move on text
        Text moveOn = new Text("Please click the button below to move on to begin.");
        moveOn.setFill(Color.BLACK);
        moveOn.setFont(Font.font("Cambria", FontWeight.BOLD, 34));
        moveOn.setX(100);
        moveOn.setY(250);

        // Move on Button
        Button nextPage = new Button("Begin");
        nextPage.setTextFill(Color.BLACK);
        nextPage.setLayoutX(425);
        nextPage.setLayoutY(550);

        Group root = new Group();
        root.getChildren().add(welcome);
        root.getChildren().add(moveOn);
        root.getChildren().add(nextPage);

        Scene welcomeScene = new Scene(root, 900, 600);

        // Waiver scene
        // Waiver text
        Text intro = new Text("Please read the following waiver.\n You must sign it to move on:\n");
        intro.setFill(Color.BLACK);
        intro.setFont(Font.font("Cambria", FontWeight.BOLD, 34));
        intro.setX(220);
        intro.setY(150);

        Text waiver = new Text("You may be eaten by a T-Rex. Are you cool with this?");
        waiver.setFill(Color.BLACK);
        waiver.setFont(Font.font("Cambria", FontWeight.BOLD, 24));
        waiver.setX(175);
        waiver.setY(250);

        // Move on to payment button
        Button paymentButton = new Button("Yes, I agree");
        paymentButton.setTextFill(Color.BLACK);
        paymentButton.setLayoutX(350);
        paymentButton.setLayoutY(550);

        // Decline waiver button
        Button declineWaiver = new Button("No, I don't agree");
        declineWaiver.setTextFill(Color.BLACK);
        declineWaiver.setLayoutX(500);
        declineWaiver.setLayoutY(550);

        Group root2 = new Group();
        root2.getChildren().add(intro);
        root2.getChildren().add(waiver);
        root2.getChildren().add(paymentButton);
        root2.getChildren().add(declineWaiver);

        Scene waiverScene = new Scene(root2, 900, 600);
        waiverScene.setFill(Color.TURQUOISE);

        // Payment scene
        // Payment text
        Text paymentInfo = new Text("Please enter your personal and payment information below:");
        paymentInfo.setFill(Color.BLACK);
        paymentInfo.setFont(Font.font("Cambria", FontWeight.BOLD, 24));
        paymentInfo.setX(175);
        paymentInfo.setY(100);

        // First name
        Label firstNameText = new Label("First name: ");
        firstNameText.setTextFill(Color.BLACK);
        firstNameText.setLayoutX(20);
        firstNameText.setLayoutY(120);
        firstNameText.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField firstNameTextField = new TextField();
        firstNameTextField.setLayoutX(180);
        firstNameTextField.setLayoutY(120);

        // Middle initial
        Label middleInitialText = new Label("Middle initial: ");
        middleInitialText.setTextFill(Color.BLACK);
        middleInitialText.setLayoutX(20);
        middleInitialText.setLayoutY(160);
        middleInitialText.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField middleInitialTextField = new TextField();
        middleInitialTextField.setLayoutX(180);
        middleInitialTextField.setLayoutY(160);

        // Last name
        Label lastNameText = new Label("Last name: ");
        lastNameText.setTextFill(Color.BLACK);
        lastNameText.setLayoutX(20);
        lastNameText.setLayoutY(200);
        lastNameText.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField lastNameTextField = new TextField();
        lastNameTextField.setLayoutX(180);
        lastNameTextField.setLayoutY(200);

        // Age
        Label ageText = new Label("Age: ");
        ageText.setTextFill(Color.BLACK);
        ageText.setLayoutX(20);
        ageText.setLayoutY(240);
        ageText.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField ageTextField = new TextField();
        ageTextField.setLayoutX(180);
        ageTextField.setLayoutY(240);

        // Payment information
        // Credit card number
        Label creditCardNumber = new Label("Credit card number: ");
        creditCardNumber.setTextFill(Color.BLACK);
        creditCardNumber.setLayoutX(20);
        creditCardNumber.setLayoutY(280);
        creditCardNumber.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField creditCardNumberTextField = new TextField();
        creditCardNumberTextField.setLayoutX(180);
        creditCardNumberTextField.setLayoutY(280);

        // Expiration date
        Label expirationDateText = new Label("Expiration date (MMYY): ");
        expirationDateText.setTextFill(Color.BLACK);
        expirationDateText.setLayoutX(20);
        expirationDateText.setLayoutY(320);
        expirationDateText.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField expirationDateTextField = new TextField();
        expirationDateTextField.setLayoutX(180);
        expirationDateTextField.setLayoutY(320);

        // Security Number
        Label securityNumberText = new Label("Security number: ");
        securityNumberText.setTextFill(Color.BLACK);
        securityNumberText.setLayoutX(20);
        securityNumberText.setLayoutY(360);
        securityNumberText.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        TextField securityNumberTextField = new TextField();
        securityNumberTextField.setLayoutX(180);
        securityNumberTextField.setLayoutY(360);

        // Submit information and verify payment
        Button verify = new Button("Submit information and verify payment");
        verify.setTextFill(Color.BLACK);
        verify.setLayoutX(350);
        verify.setLayoutY(550);

        Group root3 = new Group();
        root3.getChildren().add(paymentInfo);
        // Name
        root3.getChildren().add(firstNameText);
        root3.getChildren().add(firstNameTextField);
        root3.getChildren().add(middleInitialText);
        root3.getChildren().add(middleInitialTextField);
        root3.getChildren().add(lastNameText);
        root3.getChildren().add(lastNameTextField);
        // Age
        root3.getChildren().add(ageText);
        root3.getChildren().add(ageTextField);
        // Payment information
        root3.getChildren().add(creditCardNumber);
        root3.getChildren().add(creditCardNumberTextField);
        root3.getChildren().add(expirationDateText);
        root3.getChildren().add(expirationDateTextField);
        root3.getChildren().add(securityNumberText);
        root3.getChildren().add(securityNumberTextField);
        root3.getChildren().add(verify);

        Scene paymentScene = new Scene(root3, 900, 600);
        paymentScene.setFill(Color.TURQUOISE);

        // Shutdown signal scene
        Text shutdownMessage = new Text("Siesta Garden's is no longer accepting customers at this time.\n" +
                "Please exit back to the welcome screen.");
        shutdownMessage.setFill(Color.BLACK);
        shutdownMessage.setFont(Font.font("Cambria", FontWeight.BOLD, 24));
        shutdownMessage.setX(140);
        shutdownMessage.setY(150);

        Button exit = new Button("Return to welcome screen");
        exit.setTextFill(Color.BLACK);
        exit.setLayoutX(400);
        exit.setLayoutY(550);

        Group root4 = new Group();
        root4.getChildren().add(shutdownMessage);
        root4.getChildren().add(exit);

        Scene shutdownScene = new Scene(root4, 900, 600);
        shutdownScene.setFill(Color.TURQUOISE);

        // Action listeners
        nextPage.setOnAction(e -> {
            if(ats.getShutdownSignal()) {
                stage.setScene(shutdownScene);
            } else {
                stage.setScene(waiverScene);
            }
        });
        paymentButton.setOnAction(e -> {
            if(ats.getShutdownSignal()) {
                stage.setScene(shutdownScene);
            } else {
                stage.setScene(paymentScene);
            }
        });
        declineWaiver.setOnAction(e -> {
            if(ats.getShutdownSignal()) {
                stage.setScene(shutdownScene);
            } else {
                stage.setScene(welcomeScene);
            }
        });
        verify.setOnAction(e -> {
            if(ats.getShutdownSignal()) {
                stage.setScene(shutdownScene);
            } else {
                if (ats.verifyPayment(firstNameTextField.getText(), lastNameTextField.getText(), middleInitialTextField.getText(),
                        Integer.parseInt(ageTextField.getText()), Integer.parseInt(creditCardNumberTextField.getText()),
                        Integer.parseInt(expirationDateTextField.getText()), Integer.parseInt(securityNumberTextField.getText()))) {
                    System.out.println("Payment verified! Welcome to the park!");
                    // Clear all textfields
                    firstNameTextField.clear();
                    middleInitialTextField.clear();
                    lastNameTextField.clear();
                    ageTextField.clear();
                    creditCardNumberTextField.clear();
                    expirationDateTextField.clear();
                    securityNumberTextField.clear();
                    stage.setScene(welcomeScene);
                } else {
                    System.out.println("No park for you!");
                }
            }

        });
        exit.setOnAction(e -> stage.setScene(welcomeScene));

        welcomeScene.setFill(Color.TURQUOISE);
        stage.setTitle("Kiosk");
        stage.setScene(welcomeScene);
        return stage;
    }

}

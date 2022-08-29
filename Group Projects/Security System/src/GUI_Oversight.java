import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GUI_Oversight {
    public static void main(String args[]) {
        //starts the oversight timer which shows the pictures
        Sys_Oversight ov = new Sys_Oversight();
        ov.main(args);
    }

    public Stage getStage() {
        Stage stage = new Stage();
        //used to get the current pictures
        Sys_Oversight.TimeHelper helper = new Sys_Oversight.TimeHelper();

        //video labels
        Text text1 = new Text();
        Text text2 = new Text();
        Text text3 = new Text();

        //displays status
        Text textStatus = new Text();
        Text statusLabel = new Text();

        //oversight title
        Text ovtitle= new Text();

        //video labels
        text1.setText("Video Feed 1");
        text1.setX(100);
        text1.setY(400);
        text1.setFill(Color.BEIGE);

        text2.setText("Video Feed 2");
        text2.setX(400);
        text2.setY(400);
        text2.setFill(Color.BEIGE);

        text3.setText("Video Feed 3");
        text3.setX(700);
        text3.setY(400);
        text3.setFill(Color.BEIGE);

        //status text
        statusLabel.setText("Status: ");
        statusLabel.setX(100);
        statusLabel.setY(470);
        statusLabel.setFill(Color.BISQUE);
        statusLabel.setFont(Font.font ("Helvetica.", 20));

        textStatus.setText(Sys_Oversight.getStatus());
        textStatus.setX(175);
        textStatus.setY(472);
        textStatus.setFill(Color.YELLOW);
        textStatus.setFont(Font.font ("Verdana", FontWeight.BOLD,40));

        //oversight title
        ovtitle.setText("Oversight System");
        ovtitle.setX(250);
        ovtitle.setY(100);
        ovtitle.setFill(Color.BURLYWOOD);
        ovtitle.setFont(Font.font ("Verdana", FontWeight.BOLD,40));


        //Images
        Image vid1 = new Image("file:resources/DinoLr.jpg");
        Image vid2 = new Image("file:resources/DinoR.jpg");
        Image vid3 = new Image("file:resources/DinoLL.jpg");
        Image vid4 = new Image("file:resources/DinoL.jpg");

        ImageView imageView1 = new ImageView(vid1);
        imageView1.setX(25);
        imageView1.setY(200);
        imageView1.setFitHeight(300);
        imageView1.setFitWidth(250);
        imageView1.setPreserveRatio(true);

        ImageView imageView2 = new ImageView(vid2);
        imageView2.setX(325);
        imageView2.setY(200);
        imageView2.setFitHeight(300);
        imageView2.setFitWidth(250);
        imageView2.setPreserveRatio(true);

        ImageView imageView3 = new ImageView(vid3);
        imageView3.setX(625);
        imageView3.setY(200);
        imageView3.setFitHeight(300);
        imageView3.setFitWidth(250);
        imageView3.setPreserveRatio(true);

        //group images and texts
        Group root = new Group(imageView1, imageView2, imageView3);
        root.getChildren().add(text1);
        root.getChildren().add(text2);
        root.getChildren().add(text3);
        root.getChildren().add(textStatus);
        root.getChildren().add(statusLabel);
        root.getChildren().add(ovtitle);

        Scene scene = new Scene(root, 900, 600);
        scene.setFill(Color.BLACK);
        stage.setTitle("Oversight System");
        stage.setScene(scene);


        //timer to update the display with new information
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Show the current status based on incoming codes
                if(Sys_Oversight.getStatus().contains("ALARM")){
                    textStatus.setFill(Color.RED);
                }
                else if (Sys_Oversight.getStatus().equals("WORKING")){
                    textStatus.setFill(Color.MEDIUMSEAGREEN);
                }
                else{
                    textStatus.setFill(Color.YELLOW);
                }

                textStatus.setText(Sys_Oversight.getStatus());

                //show current image for each video
                if (helper.getVid().equals("v1")){
                    imageView1.setImage(vid1);
                    imageView2.setImage(vid2);
                    imageView3.setImage(vid3);
                }
                else if (helper.getVid().equals("v2")){
                    imageView1.setImage(vid4);
                    imageView2.setImage(vid1);
                    imageView3.setImage(vid2);
                }
                else{
                    imageView1.setImage(vid3);
                    imageView2.setImage(vid4);
                    imageView3.setImage(vid1);
                }
            }
        }, 0, 2000);

        return stage;

    }
}



package traincontroller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import traincontroller.model.TrainController;
import traincontroller.view.TrainControllerController;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    // this Timeline will be the basis off of which we will run all the module
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0),
            new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent actionEvent) {
                TrainControllerController.runInstances();
              }
            }
        ),
        new KeyFrame(Duration.millis(500))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play(); // initialize feedback loop
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/TrainControllerUI.fxml"));
    Parent root = loader.load();
    primaryStage.setTitle("Train 1");
    primaryStage.setScene(new Scene(root, 600, 400));
    primaryStage.show();

  }
}

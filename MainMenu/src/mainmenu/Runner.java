package mainmenu;

import ctc.model.CentralTrafficControl;
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

public class Runner extends Application {

  // create instances of modules
  CentralTrafficControl ctc = CentralTrafficControl.getInstance();
  Clock clk = Clock.getInstance();

  @Override
  public void start(Stage primaryStage) throws Exception {

    // use to initialize all modules
    initialize();

    // this Timeline will be the basis off of which we will run all the modules
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0),
            new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent actionEvent) {

                // TODO: add other module run() functions inside this event handler
                if (ctc.isActive()) {
                  clk.tick();
                  ctc.run();
                }
              }
            }
        ),
        new KeyFrame(Duration.millis(50))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play(); // initialize feedback loop

    // TODO: change this to MainMenu GUI and instantiate other windows onClick
    // for now - just add your module loading commands here and replace run()
    // function above to test with the clock
    Parent root = FXMLLoader.load(getClass().getResource("ctc.fxml"));
    primaryStage.setTitle("Centralized Traffic Control");
    primaryStage.setScene(new Scene(root, 1400, 600));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  private void initialize() {

    clk.setInitialTime();
    clk.tick();
    ctc.initialize();
  }
}

package mainmenu.view;

import ctc.controller.CentralTrafficControlController;

import ctc.model.CentralTrafficControl;
import ctc.view.CentralTrafficControlUserInterface;
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
import mainmenu.Clock;
import mainmenu.controller.MainMenuController;

public class Runner extends Application {

  // create instances of modules
  private CentralTrafficControlController ctcc =
      CentralTrafficControlUserInterface.getInstance().getController();
  private CentralTrafficControl ctc = CentralTrafficControl.getInstance();
  private MainMenuController mmc = MainMenuController.getInstance();
  private Clock clk = Clock.getInstance();

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
                  ctcc.run();
                }
              }
            }
        ),
        new KeyFrame(Duration.millis(5))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play(); // initialize feedback loop

    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenuview.fxml"));
    loader.setController(mmc);
    Parent root = loader.load();
    primaryStage.setTitle("On-Track Train Simulator");
    primaryStage.setScene(new Scene(root, 450, 442));
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

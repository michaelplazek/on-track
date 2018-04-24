package mainmenu.view;

import ctc.controller.CentralTrafficControlController;

import ctc.model.CentralTrafficControl;
import ctc.model.CentralTrafficControlInterface;
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
import trackctrl.control.TrackControllerController;
import trackctrl.model.TrackControllerInitializer;
import trackctrl.model.TrackControllerLineManager;
import trackmodel.model.Track;
import trackmodel.view.TrackModelUserInterface;

import traincontroller.model.TrainControllerManager;
import trainmodel.model.TrainModel;

public class Runner extends Application {

  // create instances of modules
  private CentralTrafficControlController ctcc =
      CentralTrafficControlUserInterface.getInstance().getController();
  private CentralTrafficControlInterface ctc = CentralTrafficControl.getInstance();
  private MainMenuController mmc = MainMenuController.getInstance();
  private TrackControllerInitializer init = new TrackControllerInitializer();
  private Clock clk = Clock.getInstance();
  private TrackModelUserInterface tmc = TrackModelUserInterface.getInstance();

  @Override
  public void start(Stage primaryStage) throws Exception {

    // use to initialize all modules
    initialize();

    // this Timeline will be the basis off of which we will run all the modules
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0),
            new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent actionEvent) {

                if (ctc.isActive()) {
                  clk.tick();
                  tmc.getController().run();
                  TrainModel.runAllInstances();
                  TrainControllerManager.runTrainControllers();
                  TrackControllerLineManager.runTrackControllers();
                  TrackControllerController.runCtrlrControllers();
                  ctcc.run();
                }
              }
            }
        ),
        new KeyFrame(Duration.millis(20))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play(); // initialize feedback loop

    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainmenuview.fxml"));
    loader.setController(mmc);
    Parent root = loader.load();
    primaryStage.setTitle("On-Track Train Simulator");
    Scene scene = new Scene(root, 450, 520);
    scene.getStylesheets().add("utils/ctc_style.css");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }

  private void initialize() {

    Track.initialize();
    clk.setInitialTime();
    clk.tick();
    ctc.initialize();
    init.parseConfig();
    init.initializeLogic();
  }
}

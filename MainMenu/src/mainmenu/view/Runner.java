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
import trackctrl.model.TrackControllerInitializer;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerManager;

public class Runner extends Application {

  // create instances of modules
  private CentralTrafficControlController ctcc =
      CentralTrafficControlUserInterface.getInstance().getController();
  private CentralTrafficControlInterface ctc = CentralTrafficControl.getInstance();
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
                  TrainControllerManager.runTrainControllers();
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

    //TODO: Insert Track Initializer Here
    sampleTrackMaker();

    clk.setInitialTime();
    clk.tick();
    ctc.initialize();
    TrackControllerInitializer.parseTrack();

  }

  // TODO: replace this awful function with a real one
  private void sampleTrackMaker() {

    Track test = new Track("blue");

    for (int i = 0; i < 20; i++) {

      if (i == 10 || i == 15) {
        Switch block = new Switch();
        block.setNumber(i);
        block.setSection("V");
        block.setLine("blue");
        test.addBlock(block);
      } else {
        Block block = new Block();
        block.setNumber(i);
        block.setSection("V");
        block.setLine("blue");
        test.addBlock(block);
      }
    }

    test.getBlock(10).setSwitchHere(true);
    test.getBlock(15).setSwitchHere(true);
    test.getBlock(11).setLeftStation(true);
    test.getBlock(11).setStationName("SOME STATION");
    test.getBlock(16).setRightStation(true);
    test.getBlock(16).setStationName("ANOTHER STATION");

    Track test2 = new Track("yellow");

    for (int i = 0; i < 20; i++) {

      if (i == 10 || i == 15) {
        Switch block = new Switch();
        block.setNumber(i);
        block.setSection("A");
        block.setLine("yellow");
        test2.addBlock(block);
      } else {
        Block block = new Block();
        block.setNumber(i);
        block.setSection("A");
        block.setLine("yellow");
        test2.addBlock(block);
      }
    }

    test2.getBlock(10).setSwitchHere(true);
    test2.getBlock(15).setSwitchHere(true);
    test2.getBlock(11).setLeftStation(true);
    test2.getBlock(11).setStationName("yellow station");
    test2.getBlock(16).setRightStation(true);
    test2.getBlock(16).setStationName("Another yellow station");

  }
}

package trainmodel.model;

import ctc.model.CentralTrafficControl;
import ctc.model.Route;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import mainmenu.Clock;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerManager;
import utils.train.TrainData;
import static org.junit.Assert.assertEquals;

/**
 * Created by jeremyzang on 3/13/18.
 */
public class TrainModelTest {

  @Rule
  public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

  private static TrainModel trainModel;
  private static Track testTrack;


  @BeforeClass
  public static void setUp() throws InterruptedException {
    trainModel = new TrainModel(null, "001", "RAD");
//    TrainModel.add(trainModel);
//    testTrack = new Track("RAD");
//    trainModel.setActiveTrack(testTrack);
//
//    //Create a track.
//    testTrack.addBlock(new Block("RAD", "A", 0, 100, 0, 70, "NONE", 0, 0, false, 0, 1, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 1, 100, 0, 70, "NONE", 0, 0, false, 10, 2, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 2, 100, 0, 70, "NONE", 0, 0, false, 1, 3, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 3, 100, 0, 70, "NONE", 0, 0, false, 2, 4, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 4, 100, 0, 70, "NONE", 0, 0, false, 3, 5, false, false));
//
//    Block station1 = new Block("RAD", "A", 5, 100, 0, 70, "STATION", 0, 0, false, 4, 6, true, false);
//      station1.setStationName("Station1");
//    testTrack.addBlock(station1);
//
//    testTrack.addBlock(new Block("RAD", "A", 6, 100, 0, 70, "NONE", 0, 0, false, 5, 7, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 7, 100, 0, 70, "NONE", 0, 0, false, 6, 8, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 8, 100, 0, 70, "NONE", 0, 0, false, 7, 9, false, false));
//    testTrack.addBlock(new Block("RAD", "A", 9, 100, 0, 70, "NONE", 0, 0, false, 8, 10, false, false));
//
//    Block station2 = new Block("RAD", "A", 10, 100, 0, 70, "STATION", 0, 0, false, 9, 1, false, true);
//    station2.setStationName("Station2");
//    testTrack.addBlock(station2);



  }

  @Test
  public void testRun() throws Exception {
    trainModel.run();
    assertEquals(.000001, trainModel.getAcceleration(), 0);

    trainModel.run();
    System.out.println("Acceleration: " + trainModel.getAcceleration());
    System.out.println("Velocity: " + trainModel.getVelocity());
    System.out.println("PowerCmd: " + trainModel.getPowerCommand());

    trainModel.setPowerCommand(50.0);
    trainModel.run();

    System.out.println("Force: " + trainModel.getForce());
    System.out.println("Acceleration: " + trainModel.getAcceleration());
    System.out.println("Velocity: " + trainModel.getVelocity());
    System.out.println("PowerCmd: " + trainModel.getPowerCommand());

  }

  @Test
  public void testTrainModelSimulation() {
    Clock clk = Clock.getInstance();

    // Test timeline
    Timeline timeline = new Timeline(
        new KeyFrame(Duration.seconds(0),
            new EventHandler<ActionEvent>() {
              @Override public void handle(ActionEvent actionEvent) {

                // TODO: add other module run() functions inside this event handler

                clk.tick();
                TrainModel.runAllInstances();
                System.out.println("Acceleration: " + trainModel.getAcceleration() + " Vel: " + trainModel.getVelocity()
                + " Block: " + trainModel.getCurrentBlock());
              }
            }
        ),
        new KeyFrame(Duration.millis(5))
    );
    timeline.setCycleCount(10);
    timeline.play(); // initialize feedback loop
  }

  @Test
  public void addPassengers() {
    trainModel.addPassengers(1);
    assertEquals(1, trainModel.numPassengersProperty().get(), 0);
    assertEquals(TrainData.EMPTY_WEIGHT + TrainData.PASSENGER_WEIGHT, trainModel.getMass(), .01);
    trainModel.addPassengers(299);
    assertEquals(TrainData.MAX_PASSENGERS, trainModel.numPassengersProperty().get(), 0);
    assertEquals(TrainData.EMPTY_WEIGHT + (TrainData.PASSENGER_WEIGHT * TrainData.MAX_PASSENGERS), trainModel.getMass(), .01);
  }

  @Test
  public void removePassengers() {
    trainModel.removePassengers(1);
    assertEquals(221, trainModel.numPassengersProperty().get());
    assertEquals(TrainData.EMPTY_WEIGHT + (221 * TrainData.PASSENGER_WEIGHT), trainModel.getMass(), .01);
    trainModel.removePassengers(299);
    assertEquals(0, trainModel.numPassengersProperty().get());
    assertEquals(TrainData.EMPTY_WEIGHT, trainModel.getMass(), .01);
  }


}
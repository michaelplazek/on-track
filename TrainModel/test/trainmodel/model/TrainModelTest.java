package trainmodel.model;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import utils.train.TrainData;

import static org.junit.Assert.assertEquals;

/**
 * Created by jeremyzang on 3/13/18.
 */
public class TrainModelTest {

  @Rule
  public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

  private static TrainModel trainModel;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    trainModel = new TrainModel(null, "001", "RED");
  }

  @Test
  public void addPassengers() {
    trainModel.addPassengers(1);
    assertEquals(1, trainModel.numPassengersProperty().get(), 0);
    assertEquals(TrainData.EMPTY_WEIGHT + TrainData.PASSENGER_WEIGHT, trainModel.getMass(), .01);
    assertEquals(TrainData.MAX_PASSENGERS - 1, trainModel.capacityProperty().get(), 0);
    trainModel.addPassengers(299);
    assertEquals(TrainData.MAX_PASSENGERS, trainModel.numPassengersProperty().get(), 0);
    assertEquals(TrainData.EMPTY_WEIGHT + (TrainData.PASSENGER_WEIGHT * TrainData.MAX_PASSENGERS), trainModel.getMass(), .01);
    assertEquals(0, trainModel.capacityProperty().get(), 0);
    assertEquals(0, trainModel.capacityProperty().get());
  }

  @Test
 public void removePassengers() {
    trainModel.removePassengers(1);
    assertEquals(221, trainModel.numPassengersProperty().get());
    assertEquals(TrainData.EMPTY_WEIGHT + (221 * TrainData.PASSENGER_WEIGHT), trainModel.getMass(), .01);
    assertEquals(1, trainModel.capacityProperty().get(), 0);
    trainModel.removePassengers(299);
    assertEquals(0, trainModel.numPassengersProperty().get());
    assertEquals(TrainData.EMPTY_WEIGHT, trainModel.getMass(), .01);
    assertEquals(TrainData.MAX_PASSENGERS, trainModel.capacityProperty().get(), 0);
  }

  @Test
  public void run() {
    System.out.println("Run Called");
  }


}
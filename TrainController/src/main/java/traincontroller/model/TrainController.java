package traincontroller.model;

import java.util.HashMap;

public class TrainController implements TrainControllerInterface, Runnable {
  private static HashMap<String, TrainController> listOfTrains;

  private String id;
  private String line;
  private boolean running;

  /**
   * Base constructor for TrainController.
   * @param id String for name of train
   * @param line String for line that train in running on
   */
  public TrainController(String id, String line) {
    this.id = id;
    this.line = line;
    listOfTrains = new HashMap<>();
  }

  public void setAntennaSignal(Byte[] signal) {

  }

  public void setBeaconSignal(Byte[] signal) {

  }

  public void setTrackCircuitSignal(Byte[] signal) {

  }

  private String getId() {
    return id;
  }

  private void setId(String id) {
    this.id = id;
  }

  private String getLine() {
    return line;
  }

  private void setLine(String line) {
    this.line = line;
  }

  private void start() {
    running = true;
  }

  public static void start(String trainId) {
    listOfTrains.get(trainId).start();
  }

  public static void delete(String trainId) {
    listOfTrains.remove(trainId);
  }

  public void run() {

  }

  protected static void addTrain(TrainController train) {
    listOfTrains.put(train.getId(), train);
  }

  /** Run all instances of the train controller. */
  public static void runAllInstances() {
    for (String key : listOfTrains.keySet()) {
      listOfTrains.get(key).run();
    }
  }
}

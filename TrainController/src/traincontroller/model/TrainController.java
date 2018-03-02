package traincontroller.model;

import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.controller.MainMenuController;
import trainmodel.TrainModelInterface;
import trainmodel.model.TrainModel;

public class TrainController implements TrainControllerInterface, Runnable {
  private static HashMap<String, TrainController> listOfTrains = new HashMap<>();

  private TrainModelInterface trainModel;
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
    trainModel = new TrainModel(this, id, line);
    running = true;
  }

  /**
   * Starts a train.
   * */
  public static boolean start(String trainId) {
    TrainController temp = listOfTrains.get(trainId);
    if (temp == null) {
      return false;
    }
    temp.start();
    return true;
  }

  /**
   * Removes a train from the list.
   * */
  public static boolean delete(String trainId) {
    TrainController temp = listOfTrains.get(trainId);
    if (temp == null) {
      return false;
    }
    listOfTrains.remove(trainId);
    MainMenuController.getInstance().updateTrainControllerDropdown();
    return true;
  }

  public void run() {

  }

  protected static void addTrain(TrainController train) {
    listOfTrains.put(train.getId(), train);
    MainMenuController.getInstance().updateTrainControllerDropdown();
  }

  /** Run all instances of the train controller. */
  public static void runAllInstances() {
    for (String key : listOfTrains.keySet()) {
      listOfTrains.get(key).run();
    }
  }

  public static ObservableList<String> getListOfTrains() {
    return FXCollections.observableArrayList(listOfTrains.keySet());
  }

  public static HashMap<String, TrainController> getTrainControllers() {
    return listOfTrains;
  }
}

package traincontroller.model;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;
import mainmenu.ClockInterface;
import mainmenu.controller.MainMenuController;
import trackmodel.model.Block;

public class TrainControllerManager {
  private static ClockInterface clock = Clock.getInstance();

  private static HashMap<String, TrainController> listOfTrains = new HashMap<>();

  /**
   * Ticks all trains once.
   */
  public static void runTrainControllers() {
    Map<String, TrainController> trains = getTrainControllers();
    for (String s : trains.keySet()) {
      TrainController tc = trains.get(s);
      if(tc.isRunning()) {
        PowerCalculator.run(tc);
      }
    }
  }

  /**
   * Starts a train.
   * */
  protected static boolean start(String trainId) {
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
  protected static boolean delete(String trainId) {
    TrainController temp = listOfTrains.get(trainId);
    if (temp == null) {
      return false;
    }
    listOfTrains.remove(trainId);
    MainMenuController.getInstance().updateTrainControllerDropdown();
    return true;
  }

  protected static void addTrain(TrainController train) {
    listOfTrains.put(train.getId(), train);
    MainMenuController.getInstance().updateTrainControllerDropdown();
  }

  public static ObservableList<String> getListOfTrainControllers() {
    return FXCollections.observableArrayList(listOfTrains.keySet());
  }

  public static HashMap<String, TrainController> getTrainControllers() {
    return listOfTrains;
  }

  public static TrainController getTrainController(String id) {
    return listOfTrains.get(id);
  }

}

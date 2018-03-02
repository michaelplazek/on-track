package mainmenu.model;

import ctc.model.CentralTrafficControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MainMenuModel {

  private static MainMenuModel instance;
  private ObservableList<String> trainModelList;
  private ObservableList<String> trackControllerList;
  private ObservableList<String> trainControllerList;

  private MainMenuModel() {

    this.trainModelList = FXCollections.observableArrayList();
    this.trainControllerList = FXCollections.observableArrayList();
    this.trackControllerList = FXCollections.observableArrayList();
  }

  /**
   * This is the logic to maintain a single instance of a CTC object.
   * @return the single instance of the CTC
   */
  public static MainMenuModel getInstance() {
    if (instance == null) {
      instance = new MainMenuModel();
    }
    return instance;
  }
}

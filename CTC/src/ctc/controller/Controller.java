package ctc.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class Controller {

  @FXML
  private ChoiceBox tracks;

  @FXML
  private ChoiceBox blocks1;

  @FXML
  private ChoiceBox blocks2;

  @FXML
  private ChoiceBox actions;

  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  public void initialize() {

    // *** populate Track Maintenance dropdowns ***
    ObservableList<String> track = FXCollections.observableArrayList(
        "Select track", "Green", "Red");
    tracks.setValue("Select track");
    tracks.setItems(track);

    ObservableList<String> block = FXCollections.observableArrayList(
        "Block",
        "A1", "A2", "A3",
        "B1", "B2", "B3",
        "C1", "C2", "C3");
    blocks1.setValue("Block");
    blocks1.setItems(block);
    blocks2.setValue("Block");
    blocks2.setItems(block);

    ObservableList<String> action = FXCollections.observableArrayList(
        "Select action", "Close block", "Repair block", "Toggle switch");
    actions.setValue("Select action");
    actions.setItems(action);
  }
}

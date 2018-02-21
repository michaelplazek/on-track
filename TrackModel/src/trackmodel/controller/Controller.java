package trackmodel.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class Controller {
  //DROPDOWNS
  @FXML
  private ChoiceBox trackSelection;
  @FXML
  private ChoiceBox blockSelection;

  //BLOCK SPINNER
  @FXML
  private Spinner blockNumber;

  public void initialize() {
    populateDropdown();
    populateSpinner();
  }

  private void populateDropdown() {
    ObservableList<String> track = FXCollections.observableArrayList(
        "Select track", "Green", "Red");
    trackSelection.setValue("Select track");
    trackSelection.setItems(track);

    ObservableList<String> block = FXCollections.observableArrayList(
        "Section", "A", "B", "C");
    blockSelection.setValue("Section");
    blockSelection.setItems(block);
  }

  private void populateSpinner() {
    SpinnerValueFactory<Integer> valueFactory =
        new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);
    blockNumber.setValueFactory(valueFactory);
  }

}
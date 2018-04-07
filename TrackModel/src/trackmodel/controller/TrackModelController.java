package trackmodel.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import trackmodel.view.TrackModelUserInterface;

public class TrackModelController {
  //DROPDOWNS
  @FXML
  private ChoiceBox trackSelection;
  @FXML
  private ChoiceBox blockSelection;

  //BLOCK SPINNER
  @FXML
  private Spinner blockNumber;

  //Output Labels
  @FXML private Label blockSize;
  @FXML private Label blockGrade;
  @FXML private Label blockElevation;
  @FXML private Label blockCumElevation;
  @FXML private Label blockSpeedLimit;
  @FXML private Label blockSwitch;

  //Indicator Lights
  @FXML private Circle railStatus;
  @FXML private Circle powerStatus;
  @FXML private Circle circuitStatus;
  @FXML private Circle beaconStatus;
  @FXML private Circle crossingStatus;
  @FXML private Circle undergroundStatus;
  @FXML private Circle occupiedStatus;
  @FXML private Circle trackHeating;
  @FXML private Circle stationStatus;

  //FAILURE OBJECTS
  @FXML private CheckMenuItem railFailureSelect;
  @FXML private CheckMenuItem powerFailureSelect;
  @FXML private CheckMenuItem trackFailureSelect;
  @FXML private Button start;
  @FXML private Button end;
  @FXML private MenuButton failures;

  private Block[] blocks;
  private boolean running;
  private static HashMap<String, Track> listOfTracks = new HashMap<>();
  private static HashMap<String, ArrayList<String>> trackSections = new HashMap<>();
  private static HashMap<String, ArrayList<Integer>> trackBlockNum = new HashMap<>();

  /**
   * This method initializes many of the fields.
   */
  public void initialize() {
    start.setOnAction(this::toggleSelectedFailures);
    end.setOnAction(this::toggleSelectedFailures);


  }

  /**
   * Run all instances of tracks.
   */
  public static void runAllInstances() {
    for (String key: listOfTracks.keySet()) {
      listOfTracks.get(key).run();
    }
  }

  public void updateTracks() {
    trackSelection.setItems(FXCollections.observableArrayList(Track.getListOfTracks().keySet()));
  }

  private void run() {
    if (running) {
      update();
    }
  }

  /**
   * Toggle the failures.
   * @param event pass event.
   */
  public void toggleSelectedFailures(ActionEvent event) {
    Button btn = (Button) event.getSource();

    for (MenuItem item : failures.getItems()) {
      if (CheckMenuItem.class.isInstance(item) && CheckMenuItem.class.cast(item).isSelected()) {
        if (item.getId().equals(powerFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setPowerStatus(true);
            powerStatus.setFill(Color.GREEN);
          } else if (btn.getId().equals(end.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setPowerStatus(false);
            powerStatus.setFill(Color.WHITE);
          }
        } else if (item.getId().equals(railFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setBrokenRailStatus(true);
            railStatus.setFill(Color.GREEN);
          } else if (btn.getId().equals(end.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setBrokenRailStatus(false);
            railStatus.setFill(Color.WHITE);
          }
        } else if (item.getId().equals(trackFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setTrackCircuitStatus(true);
            circuitStatus.setFill(Color.GREEN);
          } else if (btn.getId().equals(end.getId())) {
            blocks[(int)blockNumber.getValue() - 1].setTrackCircuitStatus(false);
            circuitStatus.setFill(Color.WHITE);
          }
        }
      }
    }
  }

  private void populateDropdown() {

  }

  private void populateSpinner() {

  }

  private void update() {

  }

  /**
   * This method will allow for others to get a Track based on Track Name.
   * @param trackName The name of the track the user wants
   * @return A Track object shall be returned or null if invalid trackName
   */
  public Track getTrack(String trackName) {

    Track val = listOfTracks.get(trackName.toUpperCase());

    if (val != null) {
      return val;
    } else {
      return null;
    }
  }
}
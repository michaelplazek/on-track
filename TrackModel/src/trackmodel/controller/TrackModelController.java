package trackmodel.controller;

import java.io.File;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import trackmodel.model.Block;
import trackmodel.model.Track;

public class TrackModelController {
  //DROPDOWNS
  @FXML
  private ChoiceBox trackSelection;
  @FXML
  private ChoiceBox blockSelection;

  //BLOCK SPINNER
  @FXML
  private Spinner blockNumber;

  //UPLOAD BUTTON
  @FXML private Button uploadButton;

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

  /**
   * This method initializes many of the fields.
   */
  public void initialize() {
    populateDropdown();
    populateSpinner();
    makeBlockArray();

    uploadButton.setOnAction(this::handleImportTrack);
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
    ObservableList<String> track = FXCollections.observableArrayList(
        "Select track", "Blue");
    trackSelection.setValue("Select track");
    trackSelection.setItems(track);

    ObservableList<String> block = FXCollections.observableArrayList(
        "Section", "A");
    blockSelection.setValue("Section");
    blockSelection.setItems(block);
  }

  private void populateSpinner() {
    blockNumber.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
        1, 15, 1, 1));

    blockNumber.valueProperty().addListener(new ChangeListener<Integer>() {
      @Override
      public void changed(ObservableValue<? extends Integer> observable,
                          Integer oldVal, Integer newVal) {
        Block picked = blocks[newVal - 1];

        blockSize.setText(Float.toString(picked.getSize()));
        blockGrade.setText(Float.toString(picked.getGrade()));
        blockElevation.setText(Float.toString(picked.getGrade()));
        blockCumElevation.setText(Float.toString(picked.getCumElevation()));
        blockSpeedLimit.setText(Float.toString(picked.getSpeedLimit()));

        if (picked.getBrokenRailStatus()) {
          railStatus.setFill(Color.GREEN);
        } else {
          railStatus.setFill(Color.WHITE);
        }

        if (picked.getPowerStatus()) {
          powerStatus.setFill(Color.GREEN);
        } else {
          powerStatus.setFill(Color.WHITE);
        }

        if (picked.getTrackCircuitStatus()) {
          circuitStatus.setFill(Color.GREEN);
        } else {
          circuitStatus.setFill(Color.WHITE);
        }
        
        if (picked.hasBeacon()) {
          beaconStatus.setFill(Color.GREEN);
        } else {
          beaconStatus.setFill(Color.WHITE);
        }

        if (picked.isOccupied()) {
          occupiedStatus.setFill(Color.GREEN);
        } else {
          occupiedStatus.setFill(Color.WHITE);
        }

        if (picked.isHeated()) {
          trackHeating.setFill(Color.GREEN);
        } else {
          trackHeating.setFill(Color.WHITE);
        }
      }
    });
  }

  private void makeBlockArray() {

    blocks = new Block[15];

    for (int i = 1; i <= 15; i++) {
      Block b = new Block();

      b.setLine("Blue");
      b.setSection("A");
      b.setNumber(i);
      b.setSize(i * (float)2.5);
      b.setGrade((float).2);
      b.setSpeedLimit(55);
      b.setElevation((float).2);
      b.setCumElevation((float).35);

      if (i % 2 == 0) {
        b.setHeated(true);
      }

      if (i == 2) {
        b.setOccupied(true);
      }

      if (i == 3) {
        b.setInfrastructure("STATION;PIONEER");
      } else if (i == 4) {
        b.setInfrastructure("RAILWAY");
      } else if (i == 9) {
        b.setInfrastructure("UNDERGROUND");
      } else {
        b.setInfrastructure("");
      }

      blocks[i - 1] = b;
    }
  }

  private void handleImportTrack(ActionEvent event) {
    FileChooser fc = new FileChooser();
    fc.setTitle("Choose a track file");
    fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Track Files", ".csv"));

    File inFile = fc.showOpenDialog((Stage) uploadButton.getScene().getWindow());
  }

  private void update() {

  }
}
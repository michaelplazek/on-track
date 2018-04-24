package trackmodel.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;

import utils.alerts.AlertWindow;

public class TrackModelController {
  //DROPDOWNS
  @FXML
  private ChoiceBox trackSelection;
  @FXML
  private ChoiceBox blockSelection;

  //Output Labels
  @FXML private Label blockSize;
  @FXML private Label blockGrade;
  @FXML private Label blockElevation;
  @FXML private Label blockCumElevation;
  @FXML private Label blockSpeedLimit;
  @FXML private Label blockSwitch;
  @FXML private Label occupiedList;
  @FXML private Label closedList;

  //Indicator Lights
  @FXML private Circle railStatus;
  @FXML private Circle powerStatus;
  @FXML private Circle circuitStatus;
  @FXML private Circle beaconStatus;
  @FXML private Label beaconLabel;
  @FXML private Circle crossingStatus;
  @FXML private Circle undergroundStatus;
  @FXML private Circle occupiedStatus;
  @FXML private Circle trackHeating;
  @FXML private Label stationLabel;
  @FXML private Circle stationStatus;

  //FAILURE OBJECTS
  @FXML private CheckMenuItem railFailureSelect;
  @FXML private CheckMenuItem powerFailureSelect;
  @FXML private CheckMenuItem trackFailureSelect;
  @FXML private Button start;
  @FXML private MenuButton failures;

  private ObservableList<String> blockList = FXCollections.observableArrayList();
  private Track currentTrack = null;
  private boolean running;
  private static HashMap<String, Track> listOfTracks = new HashMap<>();

  EventHandler<MouseEvent> alertBeaconData = new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent e) {
      AlertWindow alert = new AlertWindow();

      alert.setTitle("Beacon Data");
      alert.setHeader("Data for the Beacon at Block");

      int blockId = extractBlock(blockSelection);

      if (currentTrack != null) {
        Block block = currentTrack.getBlock(blockId);

        String content = "Distance to Station:\t\t" + block.getBeacon().getDistance();
        content += "\nStation Identified:\t\t"
            + currentTrack.getStationList().get(block.getBeacon().getStationId() - 1);
        content += "\nToggle Underground:\t" + block.getBeacon().isUnderground();
        content += "\nRight Side Station:\t\t" + block.getBeacon().isRight();
        content += "\nUser Message:\t\t\t" + block.getBeacon().getUserMessage();

        alert.setContent(content);

        alert.show();
      }
    }
  };

  EventHandler<MouseEvent> alertStationData = new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent e) {
      AlertWindow alert = new AlertWindow();

      alert.setTitle("Station Data");
      alert.setHeader("Data for the Station Block");

      int blockId = extractBlock(blockSelection);

      if (currentTrack != null) {
        Block block = currentTrack.getBlock(blockId);

        String content = block.getStationName() + " STATION";
        content += "\nStation Temperature:\t\t" + block.getTemperature();
        content += "\nPassengers Waiting:\t\t" + block.getPassengersWaiting();

        alert.setContent(content);

        alert.show();
      }
    }
  };

  /**
   * This method initializes many of the fields.
   */
  public void initialize() {
    start.setOnAction(this::toggleSelectedFailures);

    blockSelection.setDisable(true);

    trackSelection.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (newValue != null && !newValue.equals("")) {
            currentTrack = Track.getListOfTracks().get(newValue);

            this.makeBlockList(currentTrack);

            blockSelection.setItems(blockList);

            if (currentTrack == null) {
              blockSelection.setDisable(true);
            } else {
              blockSelection.setDisable(false);

              if (blockList.size() > 0) {
                blockSelection.setValue(blockList.get(0));
              }

            }

            updateOccupiedBlock();
            updateClosedBlocks();
          }
        });

    blockSelection.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {

          int blockId = extractBlock(blockSelection);

          if (currentTrack != null && newValue != null) {
            Block block = currentTrack.getBlock(blockId);

            updateUi(block);
          } else {
            blankBlock();
          }

        });
  }

  private int extractBlock(ChoiceBox<String> blocks) {

    StringBuilder blockName = new StringBuilder();

    if (!blocks.getSelectionModel().isEmpty()) {
      char[] temp = blocks.getSelectionModel().getSelectedItem().toCharArray();

      for (int i = 0; i < temp.length; i++) {
        if (!Character.isLetter(temp[i])) {
          blockName.append(temp[i]);
        }
      }

      return Integer.parseInt(blockName.toString());
    }

    return 0;
  }

  /**
   * This method will initialize the UI with blank data.
   */
  public void blankBlock() {
    DecimalFormat df = new DecimalFormat("#.###");

    blockSize.setText(String.valueOf(""));
    blockGrade.setText(String.valueOf(""));
    blockElevation.setText(String.valueOf(""));
    blockCumElevation.setText(String.valueOf(""));
    blockSpeedLimit.setText(String.valueOf(""));
    blockSwitch.setText("None");
    railStatus.setFill(Color.WHITE);
    powerStatus.setFill(Color.WHITE);
    circuitStatus.setFill(Color.WHITE);
    beaconStatus.setFill(Color.WHITE);
    crossingStatus.setFill(Color.WHITE);
    undergroundStatus.setFill(Color.WHITE);
    occupiedStatus.setFill(Color.WHITE);
    trackHeating.setFill(Color.WHITE);
    stationStatus.setFill(Color.WHITE);

    beaconStatus.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertBeaconData);
    stationStatus.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertStationData);
    beaconLabel.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertBeaconData);
    stationLabel.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertStationData);

    this.updateOccupiedBlock();
    this.updateClosedBlocks();
  }

  /**
   * This method updates the User Interface to hold data about a selected block.
   * @param block The block that has been selected by the user
   */
  public void updateUi(Block block) {

    DecimalFormat df = new DecimalFormat("#.###");

    blockSize.setText(String.valueOf(df.format(block.getSize() * 1.0936)));
    blockGrade.setText(String.valueOf(block.getGrade()));
    blockElevation.setText(String.valueOf(df.format(block.getElevation() * 1.0936)));
    blockCumElevation.setText(String.valueOf(df.format(block.getCumElevation() * 1.0936)));
    blockSpeedLimit.setText(String.valueOf(df.format(block.getSpeedLimit() * 0.621371)));
    if (block.isSwitch()) {
      Switch s = (Switch)block;
      blockSwitch.setText(String.valueOf(s.getStatus()));
    } else {
      blockSwitch.setText("None");
    }

    if (block.getBrokenRailStatus()) {
      railStatus.setFill(Color.GREEN);
    } else {
      railStatus.setFill(Color.WHITE);
    }

    if (block.getPowerStatus()) {
      powerStatus.setFill(Color.GREEN);
    } else {
      powerStatus.setFill(Color.WHITE);
    }

    if (block.getTrackCircuitStatus()) {
      circuitStatus.setFill(Color.GREEN);
    } else {
      circuitStatus.setFill(Color.WHITE);
    }

    if (block.hasBeacon()) {
      beaconStatus.setFill(Color.GREEN);
      beaconStatus.addEventFilter(MouseEvent.MOUSE_CLICKED, alertBeaconData);
      beaconLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, alertBeaconData);
    } else {
      beaconStatus.setFill(Color.WHITE);
      beaconStatus.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertBeaconData);
      beaconLabel.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertBeaconData);
    }

    if (block.isCrossing()) {
      crossingStatus.setFill(Color.GREEN);
    } else {
      crossingStatus.setFill(Color.WHITE);
    }

    if (block.isUnderground()) {
      undergroundStatus.setFill(Color.GREEN);
    } else {
      undergroundStatus.setFill(Color.WHITE);
    }

    if (block.isOccupied()) {
      occupiedStatus.setFill(Color.GREEN);
    } else {
      occupiedStatus.setFill(Color.WHITE);
    }

    if (block.isHeated()) {
      trackHeating.setFill(Color.GREEN);
    } else  {
      trackHeating.setFill(Color.WHITE);
    }

    if ((block.getStationName().equals(""))) {
      stationStatus.setFill(Color.WHITE);
      stationStatus.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertStationData);
      stationLabel.removeEventFilter(MouseEvent.MOUSE_CLICKED, alertStationData);
    } else {
      stationStatus.setFill(Color.GREEN);
      stationStatus.addEventFilter(MouseEvent.MOUSE_CLICKED, alertStationData);
      stationLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, alertStationData);
    }

    this.updateOccupiedBlock();
    this.updateClosedBlocks();
  }

  /**
   * This method will make the block list for a track.
   * Used for the block list selection box
   * @param track The track selected by the user
   */
  public void makeBlockList(Track track) {
    blockList.clear();

    if (track != null) {
      blockList.addAll(track.getBlockList());
      blockList.remove("-1");
    }
  }

  /**
   * Run all instances of tracks.
   */
  public static void runAllInstances() {
    for (String key: listOfTracks.keySet()) {
      listOfTracks.get(key).run();
    }
  }

  /**
   * This method will update the track selection choice box upon initialization.
   */
  public void updateTracks() {
    ArrayList<String> track = new ArrayList<>();
    track.add("Select Track");
    track.addAll(Track.getListOfTracks().keySet());
    trackSelection.setItems(FXCollections.observableArrayList(track));

    trackSelection.setValue("Select Track");
  }

  /**
   * This method will be run on each clock tick to update information about the track.
   */
  public  void run() {
    if (currentTrack != null) {
      int blockId = extractBlock(blockSelection);
      Block block = currentTrack.getBlock(blockId);
      if (block != null) {
        updateUi(block);
      }
    }
  }

  /**
   * Toggle the failures.
   * @param event pass event.
   */
  public void toggleSelectedFailures(ActionEvent event) {
    Button btn = (Button) event.getSource();

    int blockId = extractBlock(blockSelection);
    Block block = currentTrack.getBlock(blockId);

    for (MenuItem item : failures.getItems()) {
      if (CheckMenuItem.class.isInstance(item) && CheckMenuItem.class.cast(item).isSelected()) {
        if (item.getId().equals(powerFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            currentTrack.toggleFailure(block, "POWER");
          }
        } else if (item.getId().equals(railFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            currentTrack.toggleFailure(block, "RAIL");
          }
        } else if (item.getId().equals(trackFailureSelect.getId())) {
          if (btn.getId().equals(start.getId())) {
            currentTrack.toggleFailure(block,  "CIRCUIT");
          }
        }
      }
    }

    updateUi(block);
  }

  private void updateOccupiedBlock() {
    if (currentTrack != null) {
      occupiedList.setText(currentTrack.getOccupiedBlocks());
    } else {
      occupiedList.setText("");
    }
  }

  private void updateClosedBlocks() {
    if (currentTrack != null) {
      closedList.setText(currentTrack.getClosedBlocks());
    } else {
      closedList.setText("");
    }
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

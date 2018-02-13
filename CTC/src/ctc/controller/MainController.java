package ctc.controller;

import ctc.model.CentralTrafficControl;
import ctc.model.TrainDispatchRow;
import ctc.model.TrainQueueRow;
import ctc.model.TrainStopRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.NumberStringConverter;
import mainmenu.Clock;

public class MainController {

  private CentralTrafficControl ctc = CentralTrafficControl.getInstance();
  private Clock clock = Clock.getInstance();

  /* MAIN COMPONENTS */
  @FXML private RadioButton fixedBlockRadio;
  @FXML private RadioButton movingBlockRadio;
  @FXML private Label throughput;
  @FXML private Label time;
  @FXML private Button startButton;
  @FXML private Button stopButton;
  @FXML private Button decrementButton;
  @FXML private Button incrementButton;
  @FXML private Label multiplier;
  @FXML private Button testGreenButton;
  @FXML private Button testRedButton;

  /* MAINTENANCE COMPONENTS */
  @FXML private ChoiceBox<String> maintenanceTracks;
  @FXML private ChoiceBox<String> maintenanceBlocks;
  @FXML private ChoiceBox<String> maintenanceActions;
  @FXML private Button submitMaintenance;

  /* ADD TRAIN COMPONENTS */
  @FXML private Button importScheduleButton;
  @FXML private TextField trainNameField;
  @FXML private TextField departingTimeField;
  @FXML private ChoiceBox<String> scheduleBlocks;
  @FXML private TableView<TrainStopRow> addTrainTable;
  @FXML private TableColumn<TrainStopRow, String> stopColumn;
  @FXML private TableColumn<TrainStopRow, String> dwellColumn;
  @FXML private TableColumn<TrainStopRow, String> timeColumn;
  @FXML private Button resetButton;
  @FXML private Button addTrainButton;

  /* QUEUE COMPONENTS */
  @FXML private TableView<TrainQueueRow> trainQueueTable;
  @FXML private TableColumn<TrainQueueRow, String> trainColumn;
  @FXML private TableColumn<TrainQueueRow, String> departureColumn;
  @FXML private TableView<TrainStopRow> selectedScheduleTable;
  @FXML private TableColumn<TrainStopRow, String> selectedStopColumn;
  @FXML private TableColumn<TrainStopRow, String> selectedDwellColumn;
  @FXML private TableColumn<TrainStopRow, String> selectedTimeColumn;
  @FXML private Button deleteButton;
  @FXML private Button dispatchButton;

  /* DISPATCH COMPONENTS */
  @FXML private TableView<TrainDispatchRow> dispatchTable;
  @FXML private TableColumn<TrainDispatchRow, String> dispatchTrainColumn;
  @FXML private TableColumn<TrainDispatchRow, String> dispatchLocationColumn;
  @FXML private TableColumn<TrainDispatchRow, String> dispatchAuthorityColumn;
  @FXML private TableColumn<TrainDispatchRow, String> dispatchSpeedColumn;
  @FXML private TableColumn<TrainDispatchRow, String> dispatchPassengersColumn;
  @FXML private TextField suggestedSpeedField;
  @FXML private Button setSuggestedSpeedButton;
  @FXML private ChoiceBox<String> setAuthorityBlocks;
  @FXML private Button setAuthorityButton;


  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  public void initialize() {
    connect();
  }

  private void connect() {

    connectMaintenance();
    connectAddTrain();
    connectQueue();
    connectDispatch();
    connectTime();

    // TODO: hook up buttons and text fields
  }

  private void connectMaintenance() {

    maintenanceTracks.setItems(ctc.getTrackList());
    maintenanceBlocks.setItems(ctc.getBlockList());
    maintenanceActions.setItems(ctc.getActionList());

    maintenanceTracks.setValue(ctc.getTrackList().get(0));
    maintenanceBlocks.setValue(ctc.getBlockList().get(0));
    maintenanceActions.setValue(ctc.getActionList().get(0));
  }

  private void connectAddTrain() {

    stopColumn.setCellValueFactory(new PropertyValueFactory<TrainStopRow, String>("stop"));
    dwellColumn.setCellValueFactory(new PropertyValueFactory<TrainStopRow, String>("dwell"));
    timeColumn.setCellValueFactory(new PropertyValueFactory<TrainStopRow, String>("time"));

    scheduleBlocks.setItems(ctc.getBlockList());
    scheduleBlocks.setValue(ctc.getBlockList().get(0));
  }

  private void connectQueue() {

    selectedDwellColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("dwell"));
    selectedStopColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("stop"));
    selectedTimeColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("time"));
    trainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainQueueRow, String>("train"));
    departureColumn.setCellValueFactory(
        new PropertyValueFactory<TrainQueueRow, String>("departure"));
  }

  private void connectDispatch() {

    dispatchTrainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainDispatchRow, String>("train"));
    dispatchLocationColumn.setCellValueFactory(
        new PropertyValueFactory<TrainDispatchRow, String>("location"));
    dispatchAuthorityColumn.setCellValueFactory(
        new PropertyValueFactory<TrainDispatchRow, String>("authority"));
    dispatchSpeedColumn.setCellValueFactory(
        new PropertyValueFactory<TrainDispatchRow, String>("speed"));
    dispatchPassengersColumn.setCellValueFactory(
        new PropertyValueFactory<TrainDispatchRow, String>("passengers"));

    setAuthorityBlocks.setItems(ctc.getBlockList());
    setAuthorityBlocks.setValue(ctc.getBlockList().get(0));
  }

  private void connectTime() {
    time.textProperty().bind(ctc.getDisplayTime());
    // multiplier.textProperty().bind(
    // ctc.getDisplayMultiplier().asString());
    incrementButton.setOnAction(this::incrementMultiplier);
    decrementButton.setOnAction(this::decrementMultiplier);
  }

  private void incrementMultiplier(ActionEvent event) {
    clock.setMultiplier(clock.getMultiplier() + 1);
    multiplier.textProperty().setValue(Integer.toString(clock.getMultiplier()).concat("x"));
  }

  private void decrementMultiplier(ActionEvent event) {
    clock.setMultiplier(clock.getMultiplier() - 1);
    multiplier.textProperty().setValue(Integer.toString(clock.getMultiplier()).concat("x"));
  }
}

package ctc.controller;

import ctc.model.CentralTrafficControl;
import ctc.model.TrainDispatchRow;
import ctc.model.TrainQueueRow;
import ctc.model.TrainStopRow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import mainmenu.Clock;

import java.util.ArrayList;
import java.util.List;

public class MainController {

  private CentralTrafficControl ctc = CentralTrafficControl.getInstance();
  private Clock clock = Clock.getInstance();

  /* MAIN COMPONENTS */
  @FXML private RadioButton fixedBlockRadio;
  @FXML private RadioButton movingBlockRadio;
  @FXML private ToggleGroup mode;
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
  @FXML private Button setSpeedButton;
  @FXML private ChoiceBox<String> setAuthorityBlocks;
  @FXML private Button setAuthorityButton;


  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  public void initialize() {
    connect();
  }

  private void connect() {

    bindClock();
    bindThroughput();
    connectDropdowns();
    connectTables();
    connectButtons();
    connectOthers();
  }

  private void connectDropdowns() {
    maintenanceTracks.setItems(ctc.getTrackList());
    maintenanceBlocks.setItems(ctc.getBlockList());
    maintenanceActions.setItems(ctc.getActionList());

    maintenanceTracks.setValue(ctc.getTrackList().get(0));
    maintenanceBlocks.setValue(ctc.getBlockList().get(0));
    maintenanceActions.setValue(ctc.getActionList().get(0));

    scheduleBlocks.setItems(ctc.getBlockList());
    scheduleBlocks.setValue(ctc.getBlockList().get(0));

    setAuthorityBlocks.setItems(ctc.getBlockList());
    setAuthorityBlocks.setValue(ctc.getBlockList().get(0));
  }

  private void connectTables() {
    stopColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("stop"));
    dwellColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("dwell"));
    timeColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("time"));

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

    stopColumn.setCellFactory(TextFieldTableCell.<TrainStopRow>forTableColumn());
    stopColumn.setOnEditCommit(
        (TableColumn.CellEditEvent<TrainStopRow, String> t) -> {
          ((TrainStopRow) t.getTableView().getItems().get(
              t.getTablePosition().getRow())
          ).setStop(t.getNewValue());
        });

    dwellColumn.setCellFactory(TextFieldTableCell.<TrainStopRow>forTableColumn());
    dwellColumn.setOnEditCommit(
        (TableColumn.CellEditEvent<TrainStopRow, String> t) -> {
          ((TrainStopRow) t.getTableView().getItems().get(
              t.getTablePosition().getRow())
          ).setDwell(t.getNewValue());
        });

    addTrainTable.setItems(ctc.getTrainTable());

  }

  private void connectButtons() {
    incrementButton.setOnAction(this::handleButtonPress);
    decrementButton.setOnAction(this::handleButtonPress);
    startButton.setOnAction(this::handleButtonPress);
    stopButton.setOnAction(this::handleButtonPress);
    submitMaintenance.setOnAction(this::handleButtonPress);
    testGreenButton.setOnAction(this::handleButtonPress);
    testRedButton.setOnAction(this::handleButtonPress);
    importScheduleButton.setOnAction(this::handleButtonPress);
    resetButton.setOnAction(this::handleButtonPress);
    addTrainButton.setOnAction(this::handleButtonPress);
    deleteButton.setOnAction(this::handleButtonPress);
    dispatchButton.setOnAction(this::handleButtonPress);
    setSpeedButton.setOnAction(this::handleButtonPress);
    setAuthorityButton.setOnAction(this::handleButtonPress);
  }

  private void connectOthers() {

    mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {

        if (mode.getSelectedToggle() != null) {
          changeMode(mode.getSelectedToggle().getUserData().toString());
          // Do something here with the userData of newly selected radioButton
        }
      }
    });
  }

  private void bindClock() {
    time.textProperty().bind(ctc.getDisplayTime());
    // multiplier.textProperty().bind(
    // ctc.getDisplayMultiplier().asString());
  }

  private void bindThroughput() {
    throughput.textProperty().bind(ctc.getThroughput());
  }

  private void handleButtonPress(ActionEvent event) {
    Button btn = (Button) event.getSource();
    switch (btn.getId()) {
      case "incrementButton":
        incrementMultiplier();
        break;
      case "decrementButton":
        decrementMultiplier();
        break;
      case "startButton":
        startClock();
        break;
      case "stopButton":
        stopClock();
        break;
      case "submitMaintenance":
        submitMaintenance();
        break;
      case "testGreenButton":
        testGreen();
        break;
      case "testRedButton":
        testRed();
        break;
      case "importScheduleButton":
        importSchedule();
        break;
      case "resetButton":
        resetSchedule();
        break;
      case "addTrainButton":
        addTrainToQueue();
        break;
      case "deleteButton":
        deleteTrainFromQueue();
        break;
      case "dispatchButton":
        dispatchTrain();
        break;
      case "setSpeedButton":
        setSuggestedSpeed();
        break;
      case "setAuthorityButton":
        setAuthority();
        break;
      default:
        break;
    }
  }

  private void incrementMultiplier() {
    clock.setMultiplier(clock.getMultiplier() + 1);
    multiplier.textProperty().setValue(Integer.toString(clock.getMultiplier()).concat("x"));
  }

  private void decrementMultiplier() {
    clock.setMultiplier(clock.getMultiplier() - 1);
    multiplier.textProperty().setValue(Integer.toString(clock.getMultiplier()).concat("x"));
  }

  // TODO: complete these functions
  private void startClock() {
    ctc.setActive(true);
  }

  private void stopClock() {
    ctc.setActive(false);
  }

  private void submitMaintenance(){}

  private void testGreen(){}

  private void testRed(){}

  private void importSchedule(){}

  private void resetSchedule() {
    ctc.clearTrainTable();
    ObservableList<TrainStopRow> blank = FXCollections.observableArrayList(
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","","")
    );
    addTrainTable.setItems(blank);
    trainNameField.setText("");
    departingTimeField.setText("");
    scheduleBlocks.setValue(ctc.getBlockList().get(0));
  }

  private void addTrainToQueue() {
    String name = trainNameField.getText();
    String departingTime = departingTimeField.getText();
    String block = scheduleBlocks.getSelectionModel().getSelectedItem();

    // TODO: add error handling if fields aren't filled

    // get train stop info
    List<String> stopData = new ArrayList<>();
    for (TrainStopRow item : addTrainTable.getItems()) {
      stopData.add(stopColumn.getCellObservableValue(item).getValue());
    }

    // get train dwell info
    List<String> dwellData = new ArrayList<>();
    for (TrainStopRow item : addTrainTable.getItems()) {
      dwellData.add(dwellColumn.getCellObservableValue(item).getValue());
    }

    // create schedule
    ObservableList<TrainStopRow> schedule =  FXCollections.observableArrayList();
    for (int i = 0; i < addTrainTable.getItems().size(); i++) {
      schedule.add(new TrainStopRow(stopData.get(i), dwellData.get(i), ""));
    }

    ObservableList<TrainQueueRow> queue = FXCollections.observableArrayList(
        new TrainQueueRow(name, departingTime, schedule)
    );

    // create item in queue
    ctc.setTrainQueueTable(queue);
    trainQueueTable.setItems(queue);
    resetSchedule();

    // create train
  }

  private void deleteTrainFromQueue() {
    trainQueueTable.setItems(FXCollections.observableArrayList());
  }

  private void dispatchTrain(){}

  private void setSuggestedSpeed(){}

  private void setAuthority(){}

  private void changeMode(String mode){}
}

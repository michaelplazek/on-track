package ctc.controller;

import ctc.model.TrainDispatchRow;
import ctc.model.TrainQueueRow;
import ctc.model.TrainStopRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class Controller {

  // DROPDOWNS
  @FXML
  private ChoiceBox tracks;
  @FXML
  private ChoiceBox maintenanceBlocks;
  @FXML
  private ChoiceBox scheduleBlocks;
  @FXML
  private ChoiceBox actions;

  // ADD TRAIN COMPONENTS
  @FXML
  private Button importButton;
  @FXML
  private TextField trainNameField;
  @FXML
  private TextField departingTimeField;
  @FXML
  private TableView<TrainStopRow> trainTable;
  @FXML
  private TableColumn<TrainStopRow, String> stopColumn;
  @FXML
  private TableColumn<TrainStopRow, String> dwellColumn;
  @FXML
  private Button addTrainButton;

  // QUEUE COMPONENTS
  @FXML
  private TableView<TrainQueueRow> trainQueueTable;
  @FXML
  private TableView<TrainStopRow> selectedScheduleTable;
  @FXML
  private TableColumn<TrainQueueRow, String> trainColumn;
  @FXML
  private TableColumn<TrainQueueRow, String> departureColumn;
  @FXML
  private TableColumn<TrainStopRow, String> selectedStopColumn;
  @FXML
  private TableColumn<TrainStopRow, String> selectedDwellColumn;
  @FXML
  private Button dispatchButton;

  // DISPATCH COMPONENTS
  @FXML
  private TableView<TrainDispatchRow> dispatchTable;
  @FXML
  private TableColumn<TrainDispatchRow, String> dispatchTrainColumn;
  @FXML
  private TableColumn<TrainDispatchRow, String> dispatchLocationColumn;
  @FXML
  private TableColumn<TrainDispatchRow, String> dispatchAuthorityColumn;
  @FXML
  private TableColumn<TrainDispatchRow, String> dispatchSpeedColumn;
  @FXML
  private TableColumn<TrainDispatchRow, String> dispatchPassengersColumn;
  @FXML
  private ChoiceBox setAuthority;

  // MOCK DATA FOR TABLES
  private ObservableList<TrainStopRow> trainList = FXCollections.observableArrayList(
      new TrainStopRow("Edgebrook", "00:02:00"),
      new TrainStopRow("Glenbury", "00:04:00"),
      new TrainStopRow("Dormont", "00:02:00")
  );

  private ObservableList<TrainQueueRow> queueList = FXCollections.observableArrayList(
      new TrainQueueRow("Train1", "2:00:00")
  );

  private ObservableList<TrainDispatchRow> dispatchList = FXCollections.observableArrayList(
      new TrainDispatchRow("Train1", "A1", 150.0, 0.0, 0)
  );

  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  public void initialize() {
    populateDropDowns();
    populateAddTrainTable();
    populateQueue();
    populateDispatch();
  }

  /**
   * This function handles button clicks from the Import Schedule button.
   * @param event from the click
   */
  private void importSchedule(ActionEvent event) {
    trainNameField.setText("Train1");
    departingTimeField.setText("2:00:00");
    scheduleBlocks.setValue("A3");
    trainTable.setItems(trainList);
  }

  /**
   * This function handles button clicks from the Add Train button.
   * @param event from the click
   */
  private void addTrain(ActionEvent event) {
    trainNameField.setText("");
    departingTimeField.setText("");
    scheduleBlocks.setValue("Block");
    trainTable.setItems(FXCollections.observableArrayList());
    selectedScheduleTable.setItems(trainList);
    trainQueueTable.setItems(queueList);
  }

  /**
   * This function handles button clicks from the Dispatch button.
   * @param event from the click
   */
  private void dispatchTrain(ActionEvent event) {
    trainQueueTable.setItems(FXCollections.observableArrayList());
    selectedScheduleTable.setItems(FXCollections.observableArrayList());
    dispatchTable.setItems(dispatchList);
  }

  /**
   * THIS IS SIMPLY A MOCK UP WITH FAKE DATA TO POPULATE THE TABLE FOR THE UI DEMO.
   */
  private void populateDropDowns() {
    ObservableList<String> track = FXCollections.observableArrayList(
        "Select track", "Green", "Red");
    tracks.setValue("Select track");
    tracks.setItems(track);

    ObservableList<String> block = FXCollections.observableArrayList(
        "Block",
        "A1", "A2", "A3",
        "B1", "B2", "B3",
        "C1", "C2", "C3");
    maintenanceBlocks.setValue("Block");
    maintenanceBlocks.setItems(block);
    scheduleBlocks.setValue("Block");
    scheduleBlocks.setItems(block);
    setAuthority.setValue("Block");
    setAuthority.setItems(block);

    ObservableList<String> action = FXCollections.observableArrayList(
        "Select action", "Close block", "Repair block", "Toggle switch");
    actions.setValue("Select action");
    actions.setItems(action);
  }

  /**
   * THIS IS SIMPLY A MOCK UP WITH FAKE DATA TO POPULATE THE TABLE FOR THE UI DEMO.
   */
  private void populateAddTrainTable() {
    stopColumn.setCellValueFactory(new PropertyValueFactory<TrainStopRow, String>("stop"));
    dwellColumn.setCellValueFactory(new PropertyValueFactory<TrainStopRow, String>("dwell"));

    importButton.setOnAction(this::importSchedule);
  }

  /**
   * THIS IS SIMPLY A MOCK UP WITH FAKE DATA TO POPULATE THE TABLE FOR THE UI DEMO.
   */
  private void populateQueue() {
    selectedDwellColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("dwell"));
    selectedStopColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("stop"));
    trainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainQueueRow, String>("train"));
    departureColumn.setCellValueFactory(
        new PropertyValueFactory<TrainQueueRow, String>("departure"));

    addTrainButton.setOnAction(this::addTrain);
  }

  /**
   * THIS IS SIMPLY A MOCK UP WITH FAKE DATA TO POPULATE THE TABLE FOR THE UI DEMO.
   */
  private void populateDispatch() {
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

    dispatchButton.setOnAction(this::dispatchTrain);
  }
}

package ctc.controller;

import ctc.model.CentralTrafficControl;
import ctc.model.TrainListItem;
import ctc.model.TrainStopRow;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import mainmenu.Clock;

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
  @FXML private TableView<TrainListItem> trainQueueTable;
  @FXML private TableColumn<TrainListItem, String> trainColumn;
  @FXML private TableColumn<TrainListItem, String> departureColumn;
  @FXML private TableView<TrainStopRow> selectedScheduleTable;
  @FXML private TableColumn<TrainStopRow, String> selectedStopColumn;
  @FXML private TableColumn<TrainStopRow, String> selectedDwellColumn;
  @FXML private TableColumn<TrainStopRow, String> selectedTimeColumn;
  @FXML private Button deleteButton;
  @FXML private Button dispatchButton;

  /* DISPATCH COMPONENTS */
  @FXML private TableView<TrainListItem> dispatchTable;
  @FXML private TableColumn<TrainListItem, String> dispatchTrainColumn;
  @FXML private TableColumn<TrainListItem, String> dispatchLocationColumn;
  @FXML private TableColumn<TrainListItem, String> dispatchAuthorityColumn;
  @FXML private TableColumn<TrainListItem, String> dispatchSpeedColumn;
  @FXML private TableColumn<TrainListItem, String> dispatchPassengersColumn;
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

    selectedDwellColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("dwell"));
    selectedStopColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("stop"));
    selectedTimeColumn.setCellValueFactory(
        new PropertyValueFactory<TrainStopRow, String>("time"));
    trainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("name"));
    departureColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("departure"));

    dispatchTrainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("name"));
    dispatchLocationColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("location"));
    dispatchAuthorityColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("authority"));
    dispatchSpeedColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("speed"));
    dispatchPassengersColumn.setCellValueFactory(
        new PropertyValueFactory<TrainListItem, String>("passengers"));

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

    TableView.TableViewSelectionModel<TrainStopRow> defaultModel =
        addTrainTable.getSelectionModel();

    // connect the toggle buttons for mode of operation
    mode.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(
          ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) {

        if (mode.getSelectedToggle() != null) {
          RadioButton btn = (RadioButton) newToggle.getToggleGroup().getSelectedToggle();
          changeMode(btn.getText(), defaultModel);
          // Do something here with the userData of newly selected radioButton
        }
      }
    });

    trainQueueTable.getSelectionModel().selectedItemProperty()
          .addListener((observableValue, oldValue, newValue) -> {
            if (trainQueueTable.getSelectionModel().getSelectedItem() != null) {
              TrainListItem selected = trainQueueTable.getSelectionModel().getSelectedItem();
              selectedScheduleTable.setItems(selected.getSchedule());
            }
          });
  }

  /**
   * This function deals with formatting for TextFields related to naming trains.
   * It gives a max length for the name.
   */
  public void formatTrainName() {

    trainNameField.textProperty().addListener(new ChangeListener<String>() {
      private boolean ignore;

      @Override
      public void changed(
          ObservableValue<? extends String> observableValue,
          String oldValue,
          String newValue) {

        // set the max length for the text field
        if (ignore || newValue == null) {
          return;
        }

        if (newValue.length() > 13) {
          ignore = true;
          trainNameField.setText(newValue.substring(0, 13));
          ignore = false;
        }
      }
    });
  }

  private boolean isInteger(char ch) {
    return Character.isDigit(ch) || ch == ':';
  }

  /**
   * This function deals with all formatting and error handling in the TextFields that are
   * responsible for inputting times given by the user.
   */
  public void formatTimeInput() {

    departingTimeField.textProperty().addListener(new ChangeListener<String>() {
      private boolean ignore;

      @Override
      public void changed(
          ObservableValue<? extends String> observableValue,
          String oldValue,
          String newValue) {

        // use Platform.runLater() hack to get around JavaFX being stupid
        if (ignore || newValue == null || oldValue == null) {
          return;
        } else if (!newValue.equals("") && !isInteger(newValue.charAt(newValue.length() - 1))) {
          Platform.runLater(() -> {
            departingTimeField.setText(oldValue);
            departingTimeField.positionCaret(newValue.length() + 1);
          });
        } else if (newValue.length() > 8) {
          ignore = true;
          departingTimeField.setText(newValue.substring(0, 8));
          ignore = false;
        } else if (oldValue.length() < newValue.length()) {
          if (newValue.length() == 2 || newValue.length() == 5) {
            departingTimeField.setText(newValue + ":");
          } else if (oldValue.length() == 5
              && newValue.length() == 6
              && newValue.charAt(newValue.length() - 1) != ':') {
            departingTimeField.setText(oldValue + ":" + newValue.charAt(newValue.length() - 1));
          } else if (oldValue.length() == 2
              && newValue.length() == 3
              && newValue.charAt(newValue.length() - 1) != ':') {
            departingTimeField.setText(oldValue + ":" + newValue.charAt(newValue.length() - 1));
          }
        } else if (oldValue.length() > newValue.length()) {
          if (newValue.length() == 5 && oldValue.charAt(oldValue.length() - 1) != ':') {
            Platform.runLater(() -> {
              departingTimeField.setText(newValue.substring(0, newValue.length() - 1));
              departingTimeField.positionCaret(newValue.length() + 1);
            });
          } else if (newValue.length() == 2 && oldValue.charAt(oldValue.length() - 1) != ':') {
            Platform.runLater(() -> {
              departingTimeField.setText(newValue.substring(0, newValue.length() - 1));
              departingTimeField.positionCaret(newValue.length() + 1);
            });
          } else if (newValue.length() == 2 && oldValue.charAt(oldValue.length() - 1) == ':') {
            Platform.runLater(() -> {
              departingTimeField.setText(newValue.substring(0, newValue.length() - 1));
              departingTimeField.positionCaret(newValue.length() + 1);
            });
          } else if (newValue.length() == 5 && oldValue.charAt(oldValue.length() - 1) == ':') {
            Platform.runLater(() -> {
              departingTimeField.setText(newValue.substring(0, newValue.length() - 1));
              departingTimeField.positionCaret(newValue.length() + 1);
            });
          }
        }
      }
    });
  }

  private void bindClock() {
    time.textProperty().bind(ctc.getDisplayTime());
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

    // TODO: add error handling if fields aren't filled
    // TODO: create route and add it to TrainListItem

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

    String block = scheduleBlocks.getSelectionModel().getSelectedItem();
    String name = trainNameField.getText();
    String departingTime = departingTimeField.getText();

    TrainListItem train = new TrainListItem(name, departingTime, "red", schedule);

    // create item in queue
    trainQueueTable.setItems(ctc.getTrainQueueTable());
    // trainQueueTable.getSelectionModel().select(0);

    resetSchedule();

    // create train
    ctc.addTrain(train);
  }

  private void deleteTrainFromQueue() {
    TrainListItem selected = trainQueueTable.getSelectionModel().getSelectedItem();
    for (int i = 0; i < ctc.getTrainQueueTable().size(); i++) {
      if (ctc.getTrainQueueTable().get(i).getName().equals(selected.getName())) {
        ctc.getTrainQueueTable().remove(i);
        ctc.getTrainList().remove(i);
      }
    }

    trainQueueTable.setItems(ctc.getTrainQueueTable());
    // selectedScheduleTable.setItems(FXCollections.observableArrayList());
  }

  private void dispatchTrain() {

    // remove selected train from queue
    TrainListItem selected = trainQueueTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
      for (int i = 0; i < ctc.getTrainQueueTable().size(); i++) {
        if (ctc.getTrainQueueTable().get(i).getName().equals(selected.getName())) {
          ctc.getTrainQueueTable().remove(i);
        }
      }

      ctc.getDispatchTable().add(selected);
      dispatchTable.setItems(ctc.getDispatchTable());
      if (ctc.getTrainQueueTable().size() == 0) {
        selectedScheduleTable.setItems(FXCollections.observableArrayList());
      }
    }
  }

  private void setSuggestedSpeed(){}

  private void setAuthority(){}

  private void changeMode(
      String mode,
      TableView.TableViewSelectionModel<TrainStopRow> defaultModel) {

    // disable buttons
    if (mode.equals("Moving Block Mode")) {
      resetButton.setDisable(true);
      addTrainButton.setDisable(true);
      deleteButton.setDisable(true);
      dispatchButton.setDisable(true);
      setAuthorityButton.setDisable(true);
      setSpeedButton.setDisable(true);
      startButton.setDisable(true);
      stopButton.setDisable(true);
      incrementButton.setDisable(true);
      decrementButton.setDisable(true);
      scheduleBlocks.setDisable(true);
      setAuthorityBlocks.setDisable(true);
      testRedButton.setDisable(true);
      testGreenButton.setDisable(true);
      addTrainTable.setSelectionModel(null);
    } else { // re-enable buttons
      resetButton.setDisable(false);
      addTrainButton.setDisable(false);
      deleteButton.setDisable(false);
      dispatchButton.setDisable(false);
      setAuthorityButton.setDisable(false);
      setSpeedButton.setDisable(false);
      startButton.setDisable(false);
      stopButton.setDisable(false);
      incrementButton.setDisable(false);
      decrementButton.setDisable(false);
      scheduleBlocks.setDisable(false);
      setAuthorityBlocks.setDisable(false);
      testRedButton.setDisable(false);
      testGreenButton.setDisable(false);
      addTrainTable.setSelectionModel(defaultModel);
    }
  }
}

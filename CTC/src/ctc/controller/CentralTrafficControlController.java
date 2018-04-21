package ctc.controller;

import ctc.model.CentralTrafficControl;
import ctc.model.Route;
import ctc.model.Schedule;
import ctc.model.ScheduleRow;
import ctc.model.TrackMaintenance;
import ctc.model.TrainTracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DefaultStringConverter;
import mainmenu.Clock;
import trackctrl.model.TrackControllerLineManager;
import trackctrl.model.TrackControllerLineManagerInterface;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerFactory;
import utils.alerts.AlertWindow;
import utils.general.Authority;
import utils.unitconversion.UnitConversions;

public class CentralTrafficControlController {

  private CentralTrafficControl ctc = CentralTrafficControl.getInstance();
  private TrackMaintenance trackMaintenance = TrackMaintenance.getInstance();
  private TrackControllerLineManager controller;
  private Clock clock = Clock.getInstance();

  /* MAIN COMPONENTS */
  @FXML private Label throughput;
  @FXML private Label time;
  @FXML private Button startButton;
  @FXML private Button stopButton;
  @FXML private Button decrementButton;
  @FXML private Button incrementButton;
  @FXML private Label multiplier;
  @FXML private ChoiceBox<String> trackSelect;
  @FXML private ImageView map;

  /* MAINTENANCE COMPONENTS */
  @FXML private ChoiceBox<String> maintenanceTracks;
  @FXML private ChoiceBox<String> maintenanceBlocks;
  @FXML private ChoiceBox<String> maintenanceActions;
  @FXML private Button submitMaintenance;
  @FXML private Circle statusLight;
  @FXML private Circle occupiedLight;
  @FXML private ImageView stateZero;
  @FXML private ImageView stateOne;
  @FXML private ImageView stateTwo;

  /* ADD TRAIN COMPONENTS */
  @FXML private Button importScheduleButton;
  @FXML private TextField trainNameField;
  @FXML private TextField departingTimeField;
  @FXML private ChoiceBox<String> scheduleBlocks;
  @FXML private TableView<ScheduleRow> addScheduleTable;
  @FXML private TableColumn<ScheduleRow, String> stopColumn;
  @FXML private TableColumn<ScheduleRow, String> dwellColumn;
  @FXML private Button resetButton;
  @FXML private Button addTrainButton;

  /* QUEUE COMPONENTS */
  @FXML private TableView<TrainTracker> trainQueueTable;
  @FXML private TableColumn<TrainTracker, String> trainColumn;
  @FXML private TableColumn<TrainTracker, String> departureColumn;
  @FXML private TableView<ScheduleRow> selectedScheduleTable;
  @FXML private TableColumn<ScheduleRow, String> selectedStopColumn;
  @FXML private TableColumn<ScheduleRow, String> selectedDwellColumn;
  @FXML private TableColumn<ScheduleRow, String> selectedTimeColumn;
  @FXML private Button deleteButton;
  @FXML private Button dispatchButton;

  /* DISPATCH COMPONENTS */
  @FXML private TableView<TrainTracker> dispatchTable;
  @FXML private TableColumn<TrainTracker, String> dispatchTrainColumn;
  @FXML private TableColumn<TrainTracker, String> dispatchLocationColumn;
  @FXML private TableColumn<TrainTracker, String> dispatchAuthorityColumn;
  @FXML private TableColumn<TrainTracker, String> dispatchSpeedColumn;
  @FXML private TableColumn<TrainTracker, String> dispatchPassengersColumn;
  @FXML private TextField suggestedSpeedField;
  @FXML private Button setAuthorityButton;
  @FXML private Button setSpeedButton;
  @FXML private ChoiceBox<String> setAuthorityBlocks;
  @FXML private Circle trainStatus;


  /**
   * This method will be automatically called upon the initialization of the MVC.
   */
  public void initialize() {
    connect();
    updateMaintenance();
  }

  /**
   * Main run function to be called the Runner. This will handle the operations
   * each tick of the clock.
   */
  public void run() {
    ctc.run();
    dispatch();
    updateDisplays();
    updateMaintenance();
  }

  private void updateDisplays() {

    // update train status light
    TrainTracker train = dispatchTable.getSelectionModel().getSelectedItem();
    if (train != null) {
      if (train.isStopped()) {
        trainStatus.setFill(Paint.valueOf("Red"));
      } else {
        trainStatus.setFill(Paint.valueOf("#24c51b"));
      }
    } else {
      trainStatus.setFill(Paint.valueOf("Grey"));
    }

    dispatchTable.refresh();
  }

  public CentralTrafficControl getCtc() {
    return ctc;
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
    maintenanceTracks.setItems(trackMaintenance.getTrackList());
    maintenanceBlocks.setItems(trackMaintenance.getBlockList());
    maintenanceActions.setItems(trackMaintenance.getActionsList());

    maintenanceTracks.setValue(trackMaintenance.getTrackList().get(0));
    maintenanceActions.setValue(trackMaintenance.getActionsList().get(0));

    trackSelect.setItems(ctc.getTrackList());

    if (ctc.getBlockList().size() > 0) {
      scheduleBlocks.setValue(ctc.getBlockList().get(0));
      setAuthorityBlocks.setValue(ctc.getBlockList().get(0));
    }

    if (trackMaintenance.getBlockList().size() > 0) {
      maintenanceBlocks.setValue(trackMaintenance.getBlockList().get(0));
    }

    trackSelect.setValue(ctc.getTrackList().get(0));
  }

  private void connectTables() {
    stopColumn.setCellValueFactory(
        new PropertyValueFactory<ScheduleRow, String>("stop"));
    dwellColumn.setCellValueFactory(
        new PropertyValueFactory<ScheduleRow, String>("dwell"));

    selectedDwellColumn.setCellValueFactory(
        new PropertyValueFactory<ScheduleRow, String>("dwell"));
    selectedStopColumn.setCellValueFactory(
        new PropertyValueFactory<ScheduleRow, String>("stop"));
    selectedTimeColumn.setCellValueFactory(
        new PropertyValueFactory<ScheduleRow, String>("time"));
    trainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("id"));
    departureColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("departure"));

    dispatchTrainColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("id"));
    dispatchLocationColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("locationId"));
    dispatchAuthorityColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("displayAuthority"));
    dispatchSpeedColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("displaySpeed"));
    dispatchPassengersColumn.setCellValueFactory(
        new PropertyValueFactory<TrainTracker, String>("passengers"));

    dispatchTable.setRowFactory(row -> new TableRow<TrainTracker>() {
      @Override
      public void updateItem(TrainTracker train, boolean empty) {
        super.updateItem(train, empty);
        TrainTracker tracker;
        for (int i = 0; i < row.getItems().size(); i++) {
          tracker = row.getItems().get(i);
          if (tracker == null) {
            setStyle("");
          } else {
            if (tracker.isStopped()) {
              row.setStyle("-fx-selection-bar-non-focused: salmon;"
                  + "-fx-selection-bar: salmon;");
            } else {
              row.setStyle("-fx-selection-bar-non-focused: #cdee83;"
                  + "-fx-selection-bar: #cdee83;");
            }
          }
        }
      }
    });

    // set dropdown menu for stations
    stopColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
        new DefaultStringConverter(), ctc.getStationList()));

    stopColumn.setOnEditCommit(
        (TableColumn.CellEditEvent<ScheduleRow, String> t) -> {
            ((ScheduleRow) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
            ).setStop(t.getNewValue());
        });

    dwellColumn.setCellFactory(TextFieldTableCell.<ScheduleRow>forTableColumn());
    dwellColumn.setOnEditCommit(
        (TableColumn.CellEditEvent<ScheduleRow, String> t) -> {

          String input = t.getNewValue();

          if (checkTimeFormat(input) || input.equals("")) {
            ((ScheduleRow) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
            ).setDwell(input);
          } else {
            AlertWindow alert = new AlertWindow();

            alert.setTitle("Error Submitting");
            alert.setHeader("Please Use Correct Format");
            alert.setContent("Please enter the time in the following format: "
                + "XX:XX:XX");

            alert.show();
          }
        });


    addScheduleTable.setItems(FXCollections.observableArrayList(
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","","")
    ));
  }

  private void connectButtons() {
    incrementButton.setOnAction(this::handleButtonPress);
    decrementButton.setOnAction(this::handleButtonPress);
    startButton.setOnAction(this::handleButtonPress);
    stopButton.setOnAction(this::handleButtonPress);
    submitMaintenance.setOnAction(this::handleButtonPress);
    importScheduleButton.setOnAction(this::handleButtonPress);
    resetButton.setOnAction(this::handleButtonPress);
    addTrainButton.setOnAction(this::handleButtonPress);
    deleteButton.setOnAction(this::handleButtonPress);
    dispatchButton.setOnAction(this::handleButtonPress);
    setSpeedButton.setOnAction(this::handleButtonPress);
    setAuthorityButton.setOnAction(this::handleButtonPress);
  }

  private void connectOthers() {

    trainQueueTable.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (trainQueueTable.getSelectionModel().getSelectedItem() != null) {
            TrainTracker selected = trainQueueTable.getSelectionModel().getSelectedItem();
            selectedScheduleTable.setItems(selected.getSchedule().getStops());
          }
        });

    trackSelect.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (!newValue.equals("Select track")) {

            ctc.setLine(newValue);
            ctc.makeStationList();
            ctc.makeBlockList();

            stopColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new DefaultStringConverter(), ctc.getStationList()));

            scheduleBlocks.setDisable(false);
            setAuthorityBlocks.setDisable(false);

            scheduleBlocks.setItems(ctc.getBlockList());
            setAuthorityBlocks.setItems(ctc.getBlockList());

            // only show trains that are on the selected line
            ctc.getTrainQueueTable().clear();
            ctc.getDispatchTable().clear();
            selectedScheduleTable.setItems(FXCollections.observableArrayList());

            TrainTracker item;
            ObservableList<TrainTracker> list = ctc.getTrainList();
            for (int i = 0; i < list.size(); i++) {

              item = list.get(i);
              if (item.getLine().equals(newValue)) {

                // first, set the queue table
                if (!item.isDispatched()) {
                  ctc.getTrainQueueTable().add(item);
                } else { // then get the dispatch table
                  ctc.getDispatchTable().add(item);
                }
              }
            }

            // get the line manager associated with that track
            this.controller = TrackControllerLineManager.getInstance(newValue);

            // load image of the track
            Image image = new Image(CentralTrafficControlController
                .class.getResourceAsStream("images/GREEN.jpg"));
            map.setImage(image);
            centerImage(image, map);

            // then set the user interface
            trainQueueTable.setItems(ctc.getTrainQueueTable());
            dispatchTable.setItems(ctc.getDispatchTable());

            if (ctc.getBlockList().size() > 0) {
              scheduleBlocks.setValue(ctc.getBlockList().get(0));
              setAuthorityBlocks.setValue(ctc.getBlockList().get(0));
            }
          } else {
            trackSelect.setValue(oldValue);
          }
        });

    maintenanceActions.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (newValue.equals("Select action")) {
            maintenanceActions.setValue(oldValue);
          } else {
            String action = maintenanceActions.getSelectionModel().getSelectedItem();
            String line = maintenanceTracks.getSelectionModel().getSelectedItem();

            int blockId = extractBlock(maintenanceBlocks);
            Block block = Track.getListOfTracks().get(line).getBlock(blockId);

            if (action.equals("Toggle switch") && !block.isSwitch()) {
              submitMaintenance.setDisable(true);
            } else {
              submitMaintenance.setDisable(false);
            }
          }
        });

    maintenanceBlocks.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {

          String action = maintenanceActions.getSelectionModel().getSelectedItem();
          String line = maintenanceTracks.getSelectionModel().getSelectedItem();

          int blockId = extractBlock(maintenanceBlocks);
          Block block = Track.getListOfTracks().get(line).getBlock(blockId);

          updateMaintenance();

          if (action.equals("Toggle switch") && !block.isSwitch()) {
            submitMaintenance.setDisable(true);
          } else {
            submitMaintenance.setDisable(false);
          }
        });

    maintenanceTracks.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (!newValue.equals("Select track")) {

            trackMaintenance.setLine(newValue);
            trackMaintenance.makeBlockList();

            maintenanceBlocks.setItems(trackMaintenance.getBlockList());

            if (ctc.getBlockList().size() > 0) {
              maintenanceBlocks.setValue(trackMaintenance.getBlockList().get(0));
            }
          } else {
            maintenanceTracks.setValue(oldValue);
          }
          updateMaintenance();
        });

    trainStatus.setFill(Paint.valueOf("Grey"));
  }

  private boolean checkTimeFormat(String input) {

    Pattern pattern = Pattern.compile("\\d\\d\\:\\d\\d\\:\\d\\d");
    Matcher m = pattern.matcher(input);

    return m.find();
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

        if (newValue.length() > 15) {
          ignore = true;
          trainNameField.setText(newValue.substring(0, 15));
          ignore = false;
        }
      }
    });
  }

  private boolean isValidCharacter(char ch) {
    return Character.isDigit(ch) || ch == ':';
  }

  /**
   * This function deals with all formatting and error handling in the TextFields that are
   * responsible for inputting times given by the user. It is very sloppy and embarrassing.
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
        } else if (!newValue.equals("")
            && !isValidCharacter(newValue.charAt(newValue.length() - 1))) {
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

  private void startClock() {
    ctc.setActive(true);
  }

  private void stopClock() {
    ctc.setActive(false);
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

  private void submitMaintenance() {

    String line = maintenanceTracks.getSelectionModel().getSelectedItem();
    TrackControllerLineManagerInterface manager = TrackControllerLineManager.getInstance(line);
    Track track = Track.getListOfTracks().get(line);
    int blockId = extractBlock(maintenanceBlocks);
    String action = maintenanceActions.getSelectionModel().getSelectedItem();

    // TODO: hook up Track Controller once it's ready
    switch (action) {
      case "Close block":
        manager.closeBlock(blockId);
        //track.setClosedForMaintenance(blockId,true);
        updateMaintenance();
//        manager.closeBlock(blockId);
        break;
      case "Repair block":
        manager.repairBlock(blockId);
        //track.setClosedForMaintenance(blockId,false);
        updateMaintenance();
//        manager.repairBlock(blockId);
        break;
      case "Toggle switch":
        //Switch sw = (Switch) track.getBlock(blockId);
        //sw.toggle();
        manager.toggleSwitch(blockId);
        break;
      default:
        break;
    }

    updateMaintenance();
  }

  private void updateMaintenance() {

    String line = maintenanceTracks.getSelectionModel().getSelectedItem();

    if (line.equals("Select track")) {
      statusLight.setFill(Paint.valueOf("Grey"));
      occupiedLight.setFill(Paint.valueOf("Grey"));
      maintenanceBlocks.setDisable(true);
      maintenanceActions.setDisable(true);
      submitMaintenance.setDisable(true);
    } else {
      maintenanceBlocks.setDisable(false);
      maintenanceActions.setDisable(false);
      submitMaintenance.setDisable(false);

      int blockId = extractBlock(maintenanceBlocks);
      Block block = Track.getListOfTracks().get(line).getBlock(blockId);

      if (block.isClosedForMaintenance()) {
        statusLight.setFill(Paint.valueOf("Red"));
      } else {
        statusLight.setFill(Paint.valueOf("#24c51b"));
      }

      if (block.isOccupied()) {
        occupiedLight.setFill(Paint.valueOf("#24c51b"));
      } else {
        occupiedLight.setFill(Paint.valueOf("Red"));
      }

      if (block.isSwitch()) {

        Switch sw = (Switch) block;

        if (sw.getSwitchState()) {
          stateOne.setOpacity(100);
          stateTwo.setOpacity(0);
          stateZero.setOpacity(0);
        } else {
          stateOne.setOpacity(0);
          stateTwo.setOpacity(100);
          stateZero.setOpacity(0);
        }
      } else {
        stateOne.setOpacity(0);
        stateTwo.setOpacity(0);
        stateZero.setOpacity(100);
      }
    }
  }

  private void importSchedule() {

    // create file chooser
    FileChooser fc = new FileChooser();
    fc.setTitle("Choose schedule");

    // open file and import
    File file = fc.showOpenDialog((Stage) importScheduleButton.getScene().getWindow());
    if (file != null) {
      openFile(file);
    }
  }

  private void openFile(File file) {

    TrainTracker train = new TrainTracker();

    BufferedReader br = null;
    ObservableList<ScheduleRow> list = FXCollections.observableArrayList();
    String line;
    String splitBy = ",";
    String[] word;

    try {

      br = new BufferedReader(new FileReader(file));
      while ((line = br.readLine()) != null) {

        // set line
        String[] row = line.split(splitBy);
        train.setLine(row[0]);
        trackSelect.setValue(row[0]);

        // new stop
        ScheduleRow trainStop = new ScheduleRow();

        // determine station
        word = row[1].split(":");
        trainStop.setStop(word[1]);

        // determine dwell
        trainStop.setDwell(row[2]);
        trainStop.setTime("");

        // add stop to train
        list.add(trainStop);
      }

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {

      // set the image
      Image image = new Image(CentralTrafficControlController
          .class.getResourceAsStream("images/GREEN.jpg"));
      map.setImage(image);
      centerImage(image, map);

      if (br != null) {
        try {
          br.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    addScheduleTable.setItems(list);
  }

  private void centerImage(Image image, ImageView map) {

    if (map != null) {
      double w;
      double h;

      double ratioX = map.getFitWidth() / image.getWidth();
      double ratioY = map.getFitHeight() / image.getHeight();

      double reducCoeff = 0;
      if (ratioX >= ratioY) {
        reducCoeff = ratioY;
      } else {
        reducCoeff = ratioX;
      }

      w = image.getWidth() * reducCoeff;
      h = image.getHeight() * reducCoeff;

      map.setX((map.getFitWidth() - w) / 2);
      map.setY((map.getFitHeight() - h) / 2);
    }
  }

  private void resetSchedule() {

    ObservableList<ScheduleRow> blank = FXCollections.observableArrayList(
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","","")
    );
    addScheduleTable.setItems(blank);
    trainNameField.setText("");
    departingTimeField.setText("");
    scheduleBlocks.setValue(ctc.getBlockList().get(0));
  }

  private void addTrainToQueue() {

    if (trackSelect.getSelectionModel().getSelectedItem().equals("Select track")) {

      AlertWindow alert = new AlertWindow();

      alert.setTitle("Error Submitting");
      alert.setHeader("No Track Selected");
      alert.setContent("Please select a track from the "
          + "Select Track dropdown menu before submitting.");

      alert.show();
    } else {

      // get train stop info
      List<String> stopData = new ArrayList<>();
      for (ScheduleRow item : addScheduleTable.getItems()) {
        stopData.add(stopColumn.getCellData(item));
      }

      // get train dwell info
      List<String> dwellData = new ArrayList<>();
      for (ScheduleRow item : addScheduleTable.getItems()) {
        dwellData.add(dwellColumn.getCellObservableValue(item).getValue());
      }

      // get line
      String line = trackSelect.getSelectionModel().getSelectedItem();

      // create schedule
      Schedule schedule =  new Schedule(line);
      int numberOfStops = 0;
      for (int i = 0; i < addScheduleTable.getItems().size(); i++) {
        schedule.addStop(new ScheduleRow(stopData.get(i), dwellData.get(i), ""));
        if (stopData.get(i).compareTo("") != 0) {
          numberOfStops++;
        }
      }

      String name = trainNameField.getText();
      String departingTime = departingTimeField.getText();



      if (numberOfStops == 1) {
        AlertWindow alert = new AlertWindow();

        alert.setTitle("Error Submitting");
        alert.setHeader("Invalid Number Of Stops");
        alert.setContent("Schedule needs to include more than one stop");

        alert.show();
      } else if (!(name.compareTo("") == 0) && departingTime.length() == 8) {

        TrainTracker train = new TrainTracker(name, departingTime, line, schedule);
        train.setLine(ctc.getLine()); // set the track that is current set

        // get last block
        String end = scheduleBlocks.getSelectionModel().getSelectedItem();
        Block lastBlock;
        if (end.compareTo("Yard") == 0) {
          lastBlock = Track.getListOfTracks().get(line).getBlock(-1);
        } else {
          lastBlock = Track.getListOfTracks().get(line).getBlock(extractBlock(scheduleBlocks));
        }

        // create route
        train.setRoute(new Route(lastBlock, line, train));

        // find predicted distance to each stop
        float distance = 0;
        float speedLimit = 0;
        float count = 0;
        float totalTime = 0;
        Block current;
        LinkedList<Block> path = train.getRoute().getPath();
        LinkedList<String> visited = new LinkedList<>();

        for (int i = 0; i < path.size(); i++) {

          current = path.get(i);
          distance += current.getSize();
          speedLimit += current.getSpeedLimit();
          count++;
          for (int j = 0; j < schedule.getStops().size(); j++) {
            if (current.getStationName().compareTo(schedule.getStops().get(j).getStop()) == 0
                && current.getStationName().compareTo("") != 0
                && !visited.contains(current.getStationName())) {
              double time = distance * (UnitConversions.MPS_TO_MPH) / (speedLimit / count);
              totalTime += time;
              schedule.getStops().get(j)
                  .setTime(String.format("%.2f", (totalTime / 60)));
              visited.add(current.getStationName());
              totalTime += (convertTimeToMilliseconds(schedule.getStops()
                  .get(j).getDwell()) / 1000);
              count = 0;
              speedLimit = 0;
              distance = 0;
            }
          }
        }

        // create item in queue
        trainQueueTable.setItems(ctc.getTrainQueueTable());

        resetSchedule();

        // create train
        ctc.addTrain(train);

        // automatically select the first item if one hasn't been selected
        TrainTracker queued = trainQueueTable.getSelectionModel().getSelectedItem();
        if (queued == null) {
          trainQueueTable.getSelectionModel().selectFirst();
        }
      } else {

        AlertWindow alert = new AlertWindow();

        alert.setTitle("Error Submitting");
        alert.setHeader("Empty Fields");
        alert.setContent("Please fill out all the fields before submitting.");

        alert.show();
      }
    }
  }

  private long convertTimeToMilliseconds(String time) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    try {
      Date date = sdf.parse("1970-01-01 " + time);
      long t = date.getTime();
      return t;
    } catch (ParseException e) {
      System.out.println(e);
      return 0;
    }
  }

  private void deleteTrainFromQueue() {
    TrainTracker selected = trainQueueTable.getSelectionModel().getSelectedItem();
    for (int i = 0; i < ctc.getTrainQueueTable().size(); i++) {
      if (ctc.getTrainQueueTable().get(i).getId().equals(selected.getId())) {
        ctc.getTrainQueueTable().remove(i);
        ctc.getTrainList().remove(i);
        TrainControllerFactory.delete(selected.getId());
      }
    }

    trainQueueTable.setItems(ctc.getTrainQueueTable());
    selectedScheduleTable.setItems(FXCollections.observableArrayList());

  }

  private void dispatchTrain() {

    // first check that initial block isn't occupied
    Track line = Track.getListOfTracks().get(trackSelect.getValue());
    if (!line.getStartBlock().isOccupied() && ctc.isActive()) {

    // TODO: hook this up once the Track Controller is ready
//    if (!controller.getOccupancy(line.getStartBlock().getNumber())) {

      // remove selected train from queue
      TrainTracker selected = trainQueueTable.getSelectionModel().getSelectedItem();
      if (selected != null) {
        for (int i = 0; i < ctc.getTrainQueueTable().size(); i++) {
          if (ctc.getTrainQueueTable().get(i).getId().equals(selected.getId())) {
            ctc.getTrainQueueTable().remove(i);
          }
        }

        ctc.getDispatchTable().add(selected);
        setAuthorityButton.setDisable(false);
        setSpeedButton.setDisable(false);

        TrainControllerFactory.start(selected.getId());
        selected.setDispatched(true);
        dispatchTable.setItems(ctc.getDispatchTable());
        if (ctc.getTrainQueueTable().size() == 0) {
          selectedScheduleTable.setItems(FXCollections.observableArrayList());
        }

        dispatchTable.getSelectionModel().select(selected);
      }
    } else if (ctc.isActive()) {

      AlertWindow alert = new AlertWindow();
      alert.setTitle("Error");
      alert.setHeader("Problem Dispatching Train");
      alert.setContent("Must wait until track is clear before dispatching.");
      alert.show();
    }
  }

  private void setAuthority() {

    // get selected train
    TrainTracker train = dispatchTable.getSelectionModel().getSelectedItem();

    if (train != null) {

      // get selected track
      String line = trackSelect.getSelectionModel().getSelectedItem();
      TrackControllerLineManager control = TrackControllerLineManager.getInstance(line);

      // get block of of authority
      Track track = Track.getListOfTracks().get(line);
      String blockId = setAuthorityBlocks.getSelectionModel().getSelectedItem();
      Block end = track.getBlock(Integer.parseInt(blockId.replaceAll("[\\D]", "")));
      Block location = train.getLocation();

      // make new route with the new authority
      Route route = new Route(location, end, line, train);
      train.setRoute(route);

      // let the TrainTracker know that it has a new authority
      train.setWaitingForAuthority(false);

      // get new authority that is set inside of setRoute
      Authority authority = train.getAuthority();

      // get current speed
      float speed = train.getSpeed();

      // send speed
      // TODO: set this once the Track Controller is ready
//    control.sendTrackSignals(train.getLocation().getNumber(),
//        authority, speed);

      train.getLocation().setAuthority(authority);
    }
  }

  private void setSuggestedSpeed() {

    // get selected train
    TrainTracker train = dispatchTable.getSelectionModel().getSelectedItem();

    if (train != null) {

      // get selected track
      String line = trackSelect.getSelectionModel().getSelectedItem();
      TrackControllerLineManager control = TrackControllerLineManager.getInstance(line);

      // get the signals
      float speed = Float.parseFloat(suggestedSpeedField.getText());
      speed  = speed * (float)UnitConversions.MPH_TO_KPH;
      Authority authority = train.getAuthority();

      // check the new speed
      int speedLimit = train.getLocation().getSpeedLimit();
      if (speed > speedLimit) {
        train.setSpeed(speedLimit);
      } else {
        train.setSpeed(speed);
      }

      // send signals
      // TODO: set this once the Track Controller is ready
//    control.sendTrackSignals(train.getLocation().getNumber(),
//        authority, speed);

      train.getLocation().setSetPointSpeed(speed);
    }
  }

  private void dispatch() {

    // TODO: change this to a call to the Track Controller ot check occupancy of the first block
    ObservableList<TrainTracker> trains = ctc.getTrainQueueTable();
    for (int i = 0; i < trains.size(); i++) {
      if (trains.get(i).getDeparture().equals(clock.getFormattedTime())
          && !ctc.getDispatchTable().contains(trains.get(i))
          && ctc.isActive()
          && !Track.getListOfTracks()
          .get(trackSelect.getSelectionModel().getSelectedItem()).getStartBlock().isOccupied()) {
        autoDispatchTrain(i);
      }
    }
  }

  private void autoDispatchTrain(int index) {

    TrainTracker train = ctc.getTrainQueueTable().get(index);

    // remove selected train from queue
    ctc.getTrainQueueTable().remove(index);
    ctc.getDispatchTable().add(train);

    setAuthorityButton.setDisable(false);
    setSpeedButton.setDisable(false);

    TrainControllerFactory.start(train.getId());
    train.setDispatched(true);
    dispatchTable.setItems(ctc.getDispatchTable());

    dispatchTable.setItems(ctc.getDispatchTable());
    if (ctc.getTrainQueueTable().size() == 0) {
      selectedScheduleTable.setItems(FXCollections.observableArrayList());
    }

    dispatchTable.getSelectionModel().select(train);
  }
}

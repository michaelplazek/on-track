package ctc.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;
import mainmenu.ClockInterface;
import trackmodel.model.Block;
import trackmodel.model.Track;

public class CentralTrafficControl implements CentralTrafficControlInterface {

  private static CentralTrafficControl instance = null;
  private ClockInterface clock;

  private boolean isActive = true;
  private ObservableList<TrainTracker> trainList;
  private StringProperty displayTime = new SimpleStringProperty();
  private String line;
  private long time;
  private double hours;
  private int refresh;
  private int totalPassengers;
  private double throughput;
  private String mode;
  private StringProperty displayThroughput = new SimpleStringProperty("0.00 passengers/hr");

  private ObservableList<ScheduleRow> scheduleTable;
  private ObservableList<TrainTracker> trainQueueTable;
  private ObservableList<TrainTracker> dispatchTable;

  private ObservableList<String> blockList = FXCollections.observableArrayList();
  private ObservableList<String> stationList = FXCollections.observableArrayList();
  private ObservableList<String> trackList = FXCollections.observableArrayList("Select track");
  private ObservableList<String> actionsList = FXCollections.observableArrayList(
      "Select action", "Close block", "Repair block", "Toggle switch");

  /**
   * Base constructor can only be access via the getInstance() method.
   */
  private CentralTrafficControl() {
    clock = Clock.getInstance();

    this.trainQueueTable = FXCollections.observableArrayList();
    this.dispatchTable = FXCollections.observableArrayList();
    this.scheduleTable = FXCollections.observableArrayList(
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","","")
    );

    HashMap<String, Track> track =  Track.getListOfTracks();

    this.trainList = FXCollections.observableArrayList();
    this.time = 0;
    this.refresh = 0;
    this.hours = 0.0001;
    this.totalPassengers = 0;
    this.mode = "Fixed Block Mode";

  }

  /**
   * This is the logic to maintain a single instance of a CTC object.
   * @return the single instance of the CTC
   */
  public static CentralTrafficControl getInstance() {
    if (instance == null) {
      instance = new CentralTrafficControl();
    }
    return instance;
  }

  /**
   * Initializes the CTC.
   */
  public void initialize() {
    makeTrackList();
    makeBlockList();
    makeStationList();
    updateDisplayTime();
  }

  /**
   * Display time of clock is updated.
   */
  public void updateDisplayTime() {

    displayTime.setValue(clock.getFormattedTime());
    time += clock.getChangeInTime();
  }

  public StringProperty getDisplayTime() {
    return displayTime;
  }

  public ObservableList<String> getTrackList() {
    return this.trackList;
  }

  public ObservableList<String> getBlockList() {
    return this.blockList;
  }

  public ObservableList<String> getActionList() {
    return this.actionsList;
  }

  public StringProperty getThroughput() {
    return displayThroughput;
  }

  public boolean isActive() {
    return isActive;
  }

  private void makeBlockList() {

    Track track = Track.getListOfTracks().get("blue");
    blockList.addAll(track.getBlockList());
  }

  private void makeStationList() {

    Track track = Track.getListOfTracks().get("blue");
    blockList.addAll(track.getStationList());
  }

  private void makeTrackList() {

    HashMap<String,Track> track = Track.getListOfTracks();
    for (Map.Entry<String, Track> entry : track.entrySet()) {
      String key = entry.getKey();
      trackList.add(key);
    }
  }

  /**
   * Track should call this method at a station to add passengers.
   * This will allow calculation of throughput.
   * @param block Block reference of station
   * @param passengers number of passengers
   */
  public void addPassengers(Block block, int passengers) {

    totalPassengers += passengers;

    for (int i = 0; i < trainList.size(); i++) {
      if (trainList.get(i).getLocation() == block) {
        trainList.get(i).updatePassengers(passengers);
      }
    }
  }

  /**
   * Called by controller to calculate the throughput each tick.
   */
  public void calculateThroughput() {

    hours += ((float) clock.getChangeInTime() / (3600 * 1000));
    throughput = (double) totalPassengers / hours;

    if (refresh++ > 500) {
      NumberFormat formatter = new DecimalFormat("#0.00");
      displayThroughput.setValue(formatter.format(throughput) + " passengers/hr");
      this.refresh = 0; // reset count
    }
  }

  /**
   * Sets the display time on and off.
   * @param active boolean to determine whether the clock is on or off.
   */
  public void setActive(boolean active) {

    if (active) {
      clock.unpause();
    } else {
      clock.pause();
    }
    isActive = active;
  }

  public ObservableList<ScheduleRow> getScheduleTable() {
    return scheduleTable;
  }

  public void setScheduleTable(ObservableList<ScheduleRow> table) {
    scheduleTable = table;
  }

  public ObservableList<TrainTracker> getTrainList() {
    return trainList;
  }

  public ObservableList<String> getStationList() {
    return stationList;
  }

  public void addTrain(TrainTracker train) {
    this.trainList.add(train);
    trainQueueTable.add(train);
  }

  /**
   * Use to clear the train table of stops.
   */
  public void clearScheduleTable() {
    scheduleTable = FXCollections.observableArrayList(
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","",""),
        new ScheduleRow("","","")
    );
  }

  public ObservableList<TrainTracker> getTrainQueueTable() {
    return trainQueueTable;
  }

  public ObservableList<TrainTracker> getDispatchTable() {
    return dispatchTable;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String track) {
    this.line = track;
  }

  /**
   * This is called to toggle between moving block and fixed block mode.
   * @return the new mode after the toggle
   */
  public String toggleMode() {

    if (this.mode.equals("Fixed Block Mode")) {
      this.mode = "Moving Block Mode";
      return this.mode;
    } else {
      this.mode = "Fixed Block Mode";
      return this.mode;
    }
  }
}

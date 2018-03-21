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
  private TrackMaintenance trackMaintenance = TrackMaintenance.getInstance();
  private ClockInterface clock;

  private StringProperty displayTime = new SimpleStringProperty();
  private StringProperty displayThroughput = new SimpleStringProperty("0.00 passengers/hr");

  private boolean isActive = false;
  private String line;
  private String mode;
  private long time;
  private double hours;
  private int refresh;
  private int totalPassengers;
  private double throughput;

  private ObservableList<TrainTracker> trainQueueTable;
  private ObservableList<TrainTracker> dispatchTable;
  private ObservableList<TrainTracker> trainList;

  private ObservableList<String> blockList;
  private ObservableList<String> stationList;
  private ObservableList<String> trackList;

  /**
   * Base constructor can only be access via the getInstance() method.
   */
  private CentralTrafficControl() {
    clock = Clock.getInstance();

    this.trainQueueTable = FXCollections.observableArrayList();
    this.dispatchTable = FXCollections.observableArrayList();
    this.trainList = FXCollections.observableArrayList();
    this.stationList = FXCollections.observableArrayList();
    this.blockList = FXCollections.observableArrayList();
    this.trackList = FXCollections.observableArrayList("Select track");


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
    updateDisplayTime();

    trackMaintenance.makeTrackList();
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

  public StringProperty getThroughput() {
    return displayThroughput;
  }

  public boolean isActive() {
    return isActive;
  }

  /**
   * Create the list of strings for the block dropdown.
   */
  public void makeBlockList() {

    Track track = Track.getListOfTracks().get(line);

    // clear the list before refreshing
    blockList.clear();

    if (track != null) {
      blockList.addAll(track.getBlockList());
    }
  }

  /**
   * Update the list of stations held by the CTC.
   */
  public void makeStationList() {

    Track track = Track.getListOfTracks().get(line);

    // clear the list before refreshing
    stationList.clear();

    if (track != null) {
      stationList.addAll(track.getStationList());
    }
  }

  private void makeTrackList() {

    HashMap<String,Track> track = Track.getListOfTracks();
    for (Map.Entry<String, Track> entry : track.entrySet()) {
      String key = entry.getKey();
      trackList.add(key);
    }

    if (trackList.size() > 1) {
      line = trackList.get(1);
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

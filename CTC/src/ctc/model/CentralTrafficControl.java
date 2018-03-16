package ctc.model;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;
import trackmodel.model.Track;

public class CentralTrafficControl {

  private static CentralTrafficControl instance = null;
  private Clock clock;

  private boolean isActive = false;
  private ObservableList<TrainWrapper> trainList;
  private double exactAuthority;
  private long exactTime;
  private StringProperty displayTime = new SimpleStringProperty();
  private String line;
  private String throughput = "17.4 passenger/s"; // TODO: calculate throughput

  private ObservableList<ScheduleRow> scheduleTable;
  private ObservableList<TrainWrapper> trainQueueTable;
  private ObservableList<TrainWrapper> dispatchTable;

  private ObservableList<String> blockList = FXCollections.observableArrayList();
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
    this.trainList = FXCollections.observableArrayList();
    // need Track to be loaded
    // this.line = Track.getListOfTracks().get(0).getLine();
    this.line = "Green";

    // makeBlockList();
    // makeTrackList();

    this.blockList = FXCollections.observableArrayList(
        "Block",
        "A1", "A2", "A3",
        "B1", "B2", "B3",
        "C1", "C2", "C3");

    this.trackList = FXCollections.observableArrayList(
    "Select track", "Green", "Red");
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

  public void initialize() {
    updateDisplayTime();
  }

  public void updateDisplayTime() {
    displayTime.setValue(clock.getFormattedTime());
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
    return new SimpleStringProperty(throughput);
  }

  public boolean isActive() {
    return isActive;
  }

  private void makeBlockList() {

    Track track = Track.getListOfTracks().get(line);
    // need getBlockList()
    blockList.addAll(track.getBlockList());
  }

  private void makeTrackList() {

    HashMap<String,Track> track = Track.getListOfTracks();
    for (Map.Entry<String, Track> entry : track.entrySet()) {
      String key = entry.getKey();
      trackList.add(key);
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

  public ObservableList<TrainWrapper> getTrainList() {
    return trainList;
  }

  public void addTrain(TrainWrapper train) {
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

  public ObservableList<TrainWrapper> getTrainQueueTable() {
    return trainQueueTable;
  }

  public ObservableList<TrainWrapper> getDispatchTable() {
    return dispatchTable;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String track) {
    this.line = track;
  }

}

package ctc.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;


public class CentralTrafficControl {

  private static CentralTrafficControl instance = null;

  private Maintenance maintenance;
  private Schedule schedule;
  private Clock clock;

  private boolean isActive = false;
  private ObservableList<TrainListItem> trainList;
  private double exactAuthority;
  private long exactTime;
  private StringProperty displayTime = new SimpleStringProperty();
  private String throughput = "17.4 passenger/s";


  /**
   * Base constructor can only be access via the getInstance() method.
   */
  private CentralTrafficControl() {
    clock = Clock.getInstance();
    maintenance = new Maintenance();
    schedule = new Schedule();
    trainList = FXCollections.observableArrayList();
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
    return this.maintenance.trackList;
  }

  public ObservableList<String> getBlockList() {
    return this.maintenance.blockList;
  }

  public ObservableList<String> getActionList() {
    return this.maintenance.actionsList;
  }

  public StringProperty getThroughput() {
    return new SimpleStringProperty(throughput);
  }

  public boolean isActive() {
    return isActive;
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

  public ObservableList<TrainStopRow> getTrainTable() {
    return schedule.trainTable;
  }

  public void setTrainTable(ObservableList<TrainStopRow> table) {
    schedule.trainTable = table;
  }

  public ObservableList<TrainListItem> getTrainList() {
    return trainList;
  }

  public void addTrain(TrainListItem train) {
    this.trainList.add(train);
    schedule.trainQueueTable.add(train);
  }

  /**
   * Use to clear the train table of stops.
   */
  public void clearTrainTable() {
    schedule.trainTable = FXCollections.observableArrayList(
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","",""),
        new TrainStopRow("","","")
    );
  }

  public ObservableList<TrainListItem> getTrainQueueTable() {
    return schedule.trainQueueTable;
  }

  public ObservableList<TrainListItem> getDispatchTable() {
    return schedule.dispatchTable;
  }

  /* ---- PRIVATE INNER CLASSES ---- */

  private class Maintenance {

    // private Block block;
    private ObservableList<String> trackList;
    private ObservableList<String> blockList;
    private ObservableList<String> actionsList;

    /**
     * Main constructor for the class.
     */
    public Maintenance() {

      // TODO: these should be passed in
      this.trackList = FXCollections.observableArrayList(
          "Select track", "Green", "Red");

      this.blockList = FXCollections.observableArrayList(
          "Block",
          "A1", "A2", "A3",
          "B1", "B2", "B3",
          "C1", "C2", "C3");

      this.actionsList = FXCollections.observableArrayList(
          "Select action", "Close block", "Repair block", "Toggle switch");
    }
  }

  private class Schedule {

    /* Add Train */
    private ObservableList<String> blockList;
    private ObservableList<TrainStopRow> trainTable;

    /* Queue */
    private ObservableList<TrainListItem> trainQueueTable;

    /* Dispatch */
    private ObservableList<TrainListItem> dispatchTable;

    /**
     * Base constructor.
     */
    public Schedule() {

      // TODO: these should be passed in
      this.blockList = FXCollections.observableArrayList(
          "Block",
          "A1", "A2", "A3",
          "B1", "B2", "B3",
          "C1", "C2", "C3");

      // TODO: get rid of this mock data
      this.trainTable = FXCollections.observableArrayList(
          new TrainStopRow("","",""),
          new TrainStopRow("","",""),
          new TrainStopRow("","",""),
          new TrainStopRow("","",""),
          new TrainStopRow("","",""),
          new TrainStopRow("","","")
          );

      this.trainQueueTable = FXCollections.observableArrayList();
      this.dispatchTable = FXCollections.observableArrayList();
    }
  }
}

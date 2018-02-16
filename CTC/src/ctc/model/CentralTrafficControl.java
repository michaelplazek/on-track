package ctc.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;

public class CentralTrafficControl {

  private static CentralTrafficControl instance = null;

  private Header header;
  private Maintenance maintenance;
  private Schedule schedule;
  private Clock clock;
  private boolean isActive = false;

  private double exactAuthority;
  private long exactTime;
  private StringProperty displayTime = new SimpleStringProperty();
  private String throughput = "17.4 passenger/s";

  /**
   * Base constructor can only be access via the getInstance() method.
   */
  private CentralTrafficControl() {
    clock = Clock.getInstance();
    header = new Header();
    maintenance = new Maintenance();
    schedule = new Schedule();
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

  /**
   * Main run function to be called the Runner. This will handle the operations
   * each tick of the clock.
   */
  public void run() {
    updateDisplayTime();
  }

  private void updateDisplayTime() {
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

  public void setActive(boolean active) {
    isActive = active;
  }

  public ObservableList<TrainStopRow> getTrainTable() {
    return schedule.trainTable;
  }

  public void setTrainTable(ObservableList<TrainStopRow> table) {
    schedule.trainTable = table;
  }

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

  public ObservableList<TrainQueueRow> getTrainQueueTable() {
    return schedule.trainQueueTable;
  }

  public void setTrainQueueTable(ObservableList<TrainQueueRow> table) {
    schedule.trainQueueTable = table;
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
    private ObservableList<TrainQueueRow> trainQueueTable;
    private ObservableList<TrainStopRow> selectedScheduleTable;

    /* Dispatch */
    private ObservableList<TrainDispatchRow> dispatchTable;

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
    }
  }

  private class Header {

  }
}

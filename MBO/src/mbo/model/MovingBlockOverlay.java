package mbo.model;

import javafx.collections.ObservableList;
import javafx.collections.*;
import java.util.*;


public class MovingBlockOverlay {

  private static MovingBlockOverlay instance = null;

  private Schedule schedule;
  private ObservableList<TrainInfoItem> trainInfoList;
  private ObservableList<DriverScheduleItem> driverScheduleList;
  private ObservableList<TrainScheduleItem> trainScheduleList;
  private String scheduleName;
  private String desiredThroughput;

  // MBO constructor
  private MovingBlockOverlay() {
    schedule = new Schedule();
    trainInfoList = FXCollections.observableArrayList();
    trainScheduleList = FXCollections.observableArrayList();
    driverScheduleList = FXCollections.observableArrayList();
  }

  /*
  * Maintain a single instance of a MBO object
  * @return the single instance of the MBO
  * */

  public static MovingBlockOverlay getInstance() {
    if(instance == null)
      instance = new MovingBlockOverlay();
    return instance;
  }

  public void initialize(){ };

  public void run(){};

  public String getDesiredThroughput() {
    return desiredThroughput;
  }

  public String getScheduleName() {
    return scheduleName;
  }

  public void setDesiredThroughput(String desiredThroughput){
    this.desiredThroughput = desiredThroughput;
  }

  public void setScheduleName(String scheduleName) {
    this.scheduleName = scheduleName;
  }

  public ObservableList<TrainScheduleItem> getTrainScheduleList() {
    return trainScheduleList;
  }

  public ObservableList<DriverScheduleItem> getDriverScheduleList() {
    return driverScheduleList;
  }

  public ObservableList<TrainInfoItem> getTrainInfoList() {
    return trainInfoList;
  }

  private class Schedule {
    private ObservableList<TrainScheduleItem> trainScheduleTable;
    private ObservableList<DriverScheduleItem> driverScheduleTable;
    private ObservableList<TrainInfoItem> trainInfoTable;

    public Schedule(){
      this.trainScheduleTable = FXCollections.observableArrayList();
      this.driverScheduleTable = FXCollections.observableArrayList();
      this.trainInfoTable = FXCollections.observableArrayList();
    }
  }
}

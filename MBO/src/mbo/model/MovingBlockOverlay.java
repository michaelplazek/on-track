package mbo.model;

import ctc.model.CentralTrafficControl;
import ctc.model.CentralTrafficControlInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import mainmenu.Clock;
import mainmenu.ClockInterface;

import mainmenu.controller.MainMenuController;
import trackctrl.model.TrackController;
import trackctrl.model.TrackControllerInterface;
import trackctrl.model.TrackControllerLineManager;
import trackctrl.model.TrackControllerLineManagerInterface;
import trackmodel.model.Block;
import trainmodel.model.TrainModel;
import trainmodel.model.TrainModelFactory;
import trainmodel.model.TrainModelInterface;

public class MovingBlockOverlay implements MovingBlockOverlayInterface {

  private static MovingBlockOverlay instance = null;

  private ClockInterface clock;
  private Schedule schedule = new Schedule();
  private ObservableList<TrainInfoItem> trainInfoList;
  private ObservableList<DriverScheduleItem> driverScheduleList;
  private ObservableList<TrainScheduleItem> trainScheduleList;
  private String scheduleName;
  private String desiredThroughput;


  // MBO constructor
  private MovingBlockOverlay() {
    trainInfoList = FXCollections.observableArrayList();
    trainScheduleList = FXCollections.observableArrayList();
    driverScheduleList = FXCollections.observableArrayList();
  }

  /**
   * Maintain a single instance of a MBO object.
   *
   * @return a single instance of MBO
   **/
  public static MovingBlockOverlay getInstance() {
    if (instance == null) {
      instance = new MovingBlockOverlay();
    }
    return instance;
  }

  public void initialize() {

  }

  public void run() {
  }

  public String getDesiredThroughput() {
    return desiredThroughput;
  }

  public String getScheduleName() {
    return scheduleName;
  }

  public void setDesiredThroughput(String desiredThroughput) {
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

    public Schedule() {
      this.trainScheduleTable = FXCollections.observableArrayList();
      this.driverScheduleTable = FXCollections.observableArrayList();
      this.trainInfoTable = FXCollections.observableArrayList();
    }
  }
}

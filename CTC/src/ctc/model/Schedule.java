package ctc.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Schedule {

  private String line;
  private String trainName;
  private String departingTime;
  private String destination;

  private ObservableList<ScheduleRow> stops;

  /**
   * Default constructor.
   * @param line String for name of the line
   */
  public Schedule(String line) {

    this.line = line;
    this.trainName = "";
    this.departingTime = "12:00:00";
    this.destination = "YARD";
    this.stops = FXCollections.observableArrayList();
  }

  /**
   * Constructor.
   * @param line String for the name of the line
   * @param trainName String for the name of the train
   * @param departingTime String for the departing time
   * @param destination String for the block name of the destination
   */
  public Schedule(String line,
                  String trainName,
                  String departingTime,
                  String destination) {

    this.line = line;
    this.trainName = trainName;
    this.departingTime = departingTime;
    this.destination = destination;
    this.stops = FXCollections.observableArrayList();
  }

  public void addStop(ScheduleRow item) {
    stops.add(item);
  }

  /**
   * Use to clear the table of stops.
   */
  public void clearStops() {
    stops.clear();
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public String getTrainName() {
    return trainName;
  }

  public void setTrainName(String trainName) {
    this.trainName = trainName;
  }

  public String getDepartingTime() {
    return departingTime;
  }

  public void setDepartingTime(String departingTime) {
    this.departingTime = departingTime;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public ObservableList<ScheduleRow> getStops() {
    return stops;
  }

  public void setStops(ObservableList<ScheduleRow> stops) {
    this.stops = stops;
  }
}

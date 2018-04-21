package ctc.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Schedule {

  private String line;
  private String destination;

  private ObservableList<ScheduleRow> stops;

  /**
   * Default constructor.
   * @param line String for name of the line
   */
  public Schedule(String line) {

    this.line = line;
    this.destination = "Yard";
    this.stops = FXCollections.observableArrayList();
  }

  /**
   * Constructor.
   * @param line String for the name of the line
   * @param destination String for the block name of the destination
   */
  public Schedule(String line,
                  String destination) {

    this.line = line;
    this.destination = destination;
    this.stops = FXCollections.observableArrayList();
  }

  public void addStop(ScheduleRow item) {
    stops.add(item);
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
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

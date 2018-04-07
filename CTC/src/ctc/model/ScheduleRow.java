package ctc.model;

public class ScheduleRow {

  private String stop;
  private String dwell;
  private String time;
  private int numberOfStops;

  /**
   * Default constructor.
   */
  public ScheduleRow() {}

  /**
   * Base constructor.
   * @param stop the name of the stop
   * @param dwell the time stayed at that stop
   */
  public ScheduleRow(String stop, String dwell, String time) {
    this.stop = stop;
    this.dwell = dwell;
    this.time = time;
    this.numberOfStops = 0;
  }

  public String getStop() {
    return stop;
  }

  public void setStop(String stop) {
    this.stop = stop;
  }

  public String getDwell() {
    return dwell;
  }

  public void setDwell(String dwell) {
    this.dwell = dwell;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }
}

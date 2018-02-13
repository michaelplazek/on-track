package ctc.model;

public class TrainStopRow {

  private String stop;
  private String dwell;
  private String time;

  /**
   * Base constructor.
   * @param stop the name of the stop
   * @param dwell the time stayed at that stop
   */
  public TrainStopRow(String stop, String dwell) {
    this.stop = stop;
    this.dwell = dwell;
    this.time = time;
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

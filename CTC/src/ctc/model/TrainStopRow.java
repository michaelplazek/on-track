package ctc.model;

public class TrainStopRow {

  private String stop;
  private String dwell;

  public TrainStopRow(String stop, String dwell) {
    this.stop = stop;
    this.dwell = dwell;
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
}

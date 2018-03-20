package mbo.model;

public class DriverScheduleItem {
  // constructor for DriverSchedule items.
  private String schedDriver;
  private String schedDriverTrainId;
  private String schedShiftStart;
  private String schedBreakStart;
  private String schedShiftEnd;
  private String schedBreakEnd;

  public DriverScheduleItem(String schedDriver, String schedDriverTrainId,
                            String schedShiftStart, String schedBreakStart,
                            String schedShiftEnd, String schedBreakEnd) {
    this.schedDriver = schedDriver;
    this.schedDriverTrainId = schedDriverTrainId;
    this.schedShiftStart = schedShiftStart;
    this.schedBreakStart = schedBreakStart;
    this.schedShiftEnd = schedShiftEnd;
    this.schedBreakEnd = schedBreakEnd;
  }

  public void setSchedBreakEnd(String schedBreakEnd) {
    this.schedBreakEnd = schedBreakEnd;
  }

  public void setSchedBreakStart(String schedBreakStart) {
    this.schedBreakStart = schedBreakStart;
  }

  public void setSchedDriver(String schedDriver) {
    this.schedDriver = schedDriver;
  }

  public void setSchedDriverTrainId(String schedDriverTrainId) {
    this.schedDriverTrainId = schedDriverTrainId;
  }

  public void setSchedShiftEnd(String schedShiftEnd) {
    this.schedShiftEnd = schedShiftEnd;
  }

  public void setSchedShiftStart(String schedShiftStart) {
    this.schedShiftStart = schedShiftStart;
  }

  public String getSchedBreakEnd() {
    return schedBreakEnd;
  }

  public String getSchedBreakStart() {
    return schedBreakStart;
  }

  public String getSchedShiftEnd() {
    return schedShiftEnd;
  }

  public String getSchedShiftStart() {
    return schedShiftStart;
  }

  public String getSchedDriver() {
    return schedDriver;
  }

  public String getSchedDriverTrainId() {

    return schedDriverTrainId;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}

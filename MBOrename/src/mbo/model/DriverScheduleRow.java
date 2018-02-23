package mbo.model;



public class DriverScheduleRow {
  private String schedDriver;
  private String schedDriverTrainID;
  private String schedShiftStart;
  private String schedBreakStart;
  private String schedShiftEnd;
  private String schedBreakEnd;

  public DriverScheduleRow(String schedDriver, String schedDriverTrainID, String schedShiftStart, String schedBreakStart, String schedShiftEnd, String schedBreakEnd) {
    this.schedDriver = schedDriver;
    this.schedDriverTrainID = schedDriverTrainID;
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

  public void setSchedDriverTrainID(String schedDriverTrainID) {
    this.schedDriverTrainID = schedDriverTrainID;
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

  public String getSchedDriverTrainID() {
    return schedDriverTrainID;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}

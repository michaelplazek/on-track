package mbo.model;

public class TrainScheduleRow {
  private String schedTrainID;
  private String schedLine;
  private String schedStation;
  private String schedTotalTime;
  private String schedArrival;

  public TrainScheduleRow(String schedTrainID,String schedLine,String schedStation,String schedTotalTime,String schedArrival) {
    this.schedTrainID = schedTrainID;
    this.schedLine = schedLine;
    this.schedStation = schedStation;
    this.schedTotalTime = schedTotalTime;
    this.schedArrival = schedArrival;
  }

  public void setSchedArrival(String schedArrival) {
    this.schedArrival = schedArrival;
  }

  public void setSchedLine(String schedLine) {
    this.schedLine = schedLine;
  }

  public void setSchedStation(String schedStation) {
    this.schedStation = schedStation;
  }

  public void setSchedTotalTime(String schedTotalTime) {
    this.schedTotalTime = schedTotalTime;
  }

  public void setSchedTrainID(String schedTrainID) {
    this.schedTrainID = schedTrainID;
  }

  public String getSchedArrival() {
    return schedArrival;
  }

  public String getSchedTotalTime() {
    return schedTotalTime;
  }

  public String getSchedLine() {
    return schedLine;
  }

  public String getSchedStation() {
    return schedStation;
  }

  public String getSchedTrainID() {
    return schedTrainID;
  }

  @Override
  public String toString() {
    return super.toString();
  }
}

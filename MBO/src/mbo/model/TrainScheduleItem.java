package mbo.model;

public class TrainScheduleItem {
  private String schedTrainId;
  private String schedLine;
  private String schedStation;
  private String schedTotalTime;
  private String schedArrival;

  public TrainScheduleItem(String schedTrainId, String schedLine,
                           String schedStation, String schedTotalTime, String schedArrival) {
    this.schedTrainId = schedTrainId;
    this.schedLine = schedLine;
    this.schedStation = schedStation;
    this.schedTotalTime = schedTotalTime;
    this.schedArrival = schedArrival;
  }

  public String getSchedTrainId() {
    return schedTrainId;
  }

  public void setSchedTrainId(String schedTrainId) {
    this.schedTrainId = schedTrainId;
  }

  public String getSchedLine() {
    return schedLine;
  }

  public void setSchedLine(String schedLine) {
    this.schedLine = schedLine;
  }

  public String getSchedStation() {
    return schedStation;
  }

  public void setSchedStation(String schedStation) {
    this.schedStation = schedStation;
  }

  public String getSchedTotalTime() {
    return schedTotalTime;
  }

  public void setSchedTotalTime(String schedTotalTime) {
    this.schedTotalTime = schedTotalTime;
  }

  public String getSchedArrival() {
    return schedArrival;
  }

  public void setSchedArrival(String schedArrival) {
    this.schedArrival = schedArrival;
  }
}

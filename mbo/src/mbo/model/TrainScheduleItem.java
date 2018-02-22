package mbo.model;

import javafx.collections.ObservableList;

public class TrainScheduleItem {
  private String schedTrainID;
  private String schedLine;
  private String schedStation;
  private String schedTotalTime;
  private String schedArrival;

  public TrainScheduleItem(String schedTrainID,String schedLine,String schedStation,String schedTotalTime,String schedArrival) {
    this.schedTrainID = schedTrainID;
    this.schedLine = schedLine;
    this.schedStation = schedStation;
    this.schedTotalTime = schedTotalTime;
    this.schedArrival = schedArrival;
  }

  public String getSchedTrainID() {
    return schedTrainID;
  }

  public void setSchedTrainID(String schedTrainID) {
    this.schedTrainID = schedTrainID;
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

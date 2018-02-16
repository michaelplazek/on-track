package ctc.model;

import javafx.collections.ObservableList;

public class TrainQueueRow {
  private String train;
  private String departure;
  private ObservableList<TrainStopRow> selectedSchedule;

  public TrainQueueRow(String train, String departure, ObservableList<TrainStopRow> schedule) {
    this.train = train;
    this.departure = departure;
    this.selectedSchedule = schedule;
  }

  public String getTrain() {
    return train;
  }

  public void setTrain(String train) {
    this.train = train;
  }

  public String getDeparture() {
    return departure;
  }

  public void setDeparture(String departure) {
    this.departure = departure;
  }

  public ObservableList<TrainStopRow> getSelectedSchedule() {
    return selectedSchedule;
  }

  public void setSelectedSchedule(ObservableList<TrainStopRow> selectedSchedule) {
    this.selectedSchedule = selectedSchedule;
  }
}

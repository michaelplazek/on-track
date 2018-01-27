package ctc.model;

public class TrainQueueRow {
  private String train;
  private String departure;

  public TrainQueueRow(String train, String departure) {
    this.train = train;
    this.departure = departure;
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
}

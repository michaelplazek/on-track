package ctc.model;

import javafx.collections.ObservableList;
import traincontroller.model.TrainController;
import traincontroller.model.TrainControllerFactory;

/**
 * This class is used to map the train instances to their routes.
 */
public class TrainListItem {

  private TrainController train;
  private ObservableList<TrainStopRow> schedule;
  private String name;
  private String departure;
  // add Block authority here
  // add Route here

  /**
   * Base constructor for the TrainList items.
   * @param departure String for initial departure time of train
   * @param id String for name of train
   * @param line String for name of line train is running on
   */
  public TrainListItem(
      String id,
      String departure,
      String line,
      ObservableList<TrainStopRow> schedule) {
    this.train = (TrainController) TrainControllerFactory.createTrainController(id, line);
    this.name = id;
    this.departure = departure;
    this.schedule = schedule;
  }

  public TrainController getTrain() {
    return train;
  }

  public ObservableList<TrainStopRow> getSchedule() {
    return schedule;
  }

  public void setSchedule(ObservableList<TrainStopRow> schedule) {
    this.schedule = schedule;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDeparture() {
    return departure;
  }

  public void setDeparture(String departure) {
    this.departure = departure;
  }
}

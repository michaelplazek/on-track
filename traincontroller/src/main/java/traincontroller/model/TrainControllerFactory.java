package traincontroller.model;

public class TrainControllerFactory {
  /** Use this function to create new train controllers.
   * @param id Id of the new train.
   * @return Returns a new train controller with the given id.
   * */
  public static TrainControllerInterface createTrainController(String id, String line) {
    TrainController trainController = new TrainController(id, line);
    TrainController.addTrain(trainController);
    return trainController;
  }
}

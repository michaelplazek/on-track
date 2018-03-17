package traincontroller.model;

public class TrainControllerFactory {
  /** Use this function to create new train controllers.
   * @param id Id of the new train.
   * @return Returns a new train controller with the given id.
   * */
  public static TrainControllerInterface create(String id, String line) {
    if (TrainController.getTrainControllers().get(id) != null) {
      return null;
    }
    TrainController trainController = new TrainController(id, line);
    TrainController.addTrain(trainController);
    return trainController;
  }

  public static boolean start(String id) {
    return TrainController.start(id);
  }

  public static boolean delete(String id) {
    return TrainController.delete(id);
  }
}

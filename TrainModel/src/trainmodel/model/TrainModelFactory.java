package trainmodel.model;

import traincontroller.model.TrainControllerInterface;
import trainmodel.TrainModelInterface;

/**
 * Created by jeremyzang on 3/2/18.
 * This class will be used to create a TrainModel instance.
 */
public class TrainModelFactory {

  /**
   * Use this funciton to create a new TrainModel
   * @param trainControllerInterface an instance of a trainController.
   * @param id id of the new train.
   * @param line line the train will be on.
   * @return the created instance.
   */
  public static TrainModelInterface createTrainModel(TrainControllerInterface trainControllerInterface, String id, String line){
    TrainModel trainModel = new TrainModel(trainControllerInterface, id, line);
    return trainModel;
  }




}

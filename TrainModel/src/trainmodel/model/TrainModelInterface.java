package trainmodel.model;

import utils.train.DoorStatus;
import utils.train.Failure;
import utils.train.OnOffStatus;


/**
 * Created by jeremyzang on 2/11/18.
 * This Interface is used for interaction with a TrainModel.
 */
public interface TrainModelInterface {

  /**
   * The following methods are getters/setters for data going
   * to the Train MovingBlockOverlayController.
   */
  //Getters
  double getCurrentSpeed();

  OnOffStatus getEmergencyBrakeStatus();

  OnOffStatus getServiceBrakeStatus();

  Failure getTrackLineFailureStatus();

  Failure getEngineFailureStatus();

  Failure getBrakeFailureStatus();

  DoorStatus getLeftDoorStatus();

  DoorStatus getRightDoorStatus();

  OnOffStatus getLightStatus();

  double getCurrentTemp();

  //Setters

  void start();

  void startEngine();

  void setEmergencyBrakeStatus(OnOffStatus brakeStatus);

  void setServiceBrakeStatus(OnOffStatus brakeStatus);

  void setTrackLineFailureStatus(Failure trackLineFailureStatus);

  void setLeftDoorStatus(DoorStatus leftDoorStatus);

  void setRightDoorStatus(DoorStatus rightDoorStatus);

  void setLightStatus(OnOffStatus lightStatus);

  void setHeaterStatus(OnOffStatus heaterStatus);

  void setAcStatus(OnOffStatus acStatus);

  void setPowerCommand(double powerCommand);

}

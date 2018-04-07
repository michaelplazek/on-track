package trainmodel.model;

import utils.train.TrainModelEnums.DoorStatus;
import utils.train.TrainModelEnums.OnOffStatus;
import utils.train.TrainModelEnums.TrackLineStatus;


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

  TrackLineStatus getTrackLineStatus();

  DoorStatus getLeftDoorStatus();

  DoorStatus getRightDoorStatus();

  OnOffStatus getLightStatus();

  double getCurrentTemp();

  //Setters

  void setEmergencyBrakeStatus(OnOffStatus brakeStatus);

  void setServiceBrakeStatus(OnOffStatus brakeStatus);

  void setTrackLineStatus(TrackLineStatus trackLineStatus);

  void setLeftDoorStatus(DoorStatus leftDoorStatus);

  void setRightDoorStatus(DoorStatus rightDoorStatus);

  void setLightStatus(OnOffStatus lightStatus);

  void setHeaterStatus(OnOffStatus heaterStatus);

  void setAcStatus(OnOffStatus acStatus);

  void setPowerCommand(double powerCommand);

}

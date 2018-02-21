package trainmodel;

import utils.TrainModelEnums.AntennaStatus;
import utils.TrainModelEnums.BrakeStatus;
import utils.TrainModelEnums.DoorStatus;
import utils.TrainModelEnums.LightStatus;
import utils.TrainModelEnums.TrackLineStatus;


/**
 * Created by jeremyzang on 2/11/18.
 * This Interface is used for interaction with a TrainModel.
 */
public interface TrainModelInterface {

  /**
   * The following methods are getters/setters for data going to the Train Controller.
   */
  //Getters
  double getCurrentSpeed();

  BrakeStatus getEmergencyBrakeStatus();

  BrakeStatus getServiceBrakeStatus();

  TrackLineStatus getTrackLineStatus();

  AntennaStatus getAntennaStatus();

  DoorStatus getLeftDoorStatus();

  DoorStatus getRightDoorStatus();

  LightStatus getLightStatus();

  double getCurrentTemp();

  //Setters
  void setCurrentSpeed(double currentSpeed);

  void setEmergencyBrakeStatus(BrakeStatus brakeStatus);

  void setServiceBrakeStatus(BrakeStatus brakeStatus);

  void setTrackLineStatus(TrackLineStatus trackLineStatus);

  void setAntennaStatus(AntennaStatus antennaStatus);

  void setLeftDoorStatus(DoorStatus leftDoorStatus);

  void setRightDoorStatus(DoorStatus rightDoorStatus);

  void setLightStatus(LightStatus lightStatus);

  void setCurrentTemp(double currentTemp);

  /**
   * The following methods are getters/setters for data going to the Moving Block Overlay.
   */
  double getGpsLocation();



}

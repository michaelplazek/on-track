package trainmodel;

import trainmodel.TrainModelEnums;

/**
 * Created by jeremyzang on 2/11/18.
 */
public interface Train {

  //Getters
  double getCurrentSpeed();
  TrainModelEnums.BrakeStatus getEmergencyBrakeStatus();
  TrainModelEnums.BrakeStatus getServiceBrakeStatus();
  TrainModelEnums.TrackLineStatus getTrackLineStatus();
  TrainModelEnums.AntennaStatus getAntennaStatus();
  TrainModelEnums.DoorStatus getLeftDoorStatus();
  TrainModelEnums.DoorStatus getRightDoorStatus();
  TrainModelEnums.LightStatus getLightStatus();
  double getCurrentTemp();

  //Setters
  void setCurrentSpeed(double currentSpeed);
  void setEmergencyBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus);
  void setServiceBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus);
  void setTrackLineStatus(TrainModelEnums.TrackLineStatus trackLineStatus);
  void setAntennaStatus(TrainModelEnums.AntennaStatus antennaStatus);
  void setLeftDoorStatus(TrainModelEnums.DoorStatus leftDoorStatus);
  void setRightDoorStatus(TrainModelEnums.DoorStatus rightDoorStatus);
  void setLightStatus(TrainModelEnums.LightStatus lightStatus);
  void setCurrentTemp(double currentTemp);


}

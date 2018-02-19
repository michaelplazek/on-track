package trainmodel;

import trainmodel.model.GpsLocation;
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

//  void setCurrentSpeed(double currentSpeed); shouldn't be able to set current speed 2/19/18

  void setEmergencyBrakeStatus(BrakeStatus brakeStatus);

  void setServiceBrakeStatus(BrakeStatus brakeStatus);

  void setTrackLineStatus(TrackLineStatus trackLineStatus);

  void setAntennaStatus(AntennaStatus antennaStatus);

  void setLeftDoorStatus(DoorStatus leftDoorStatus);

  void setRightDoorStatus(DoorStatus rightDoorStatus);

  void setLightStatus(LightStatus lightStatus);

  void setCurrentTemp(double currentTemp);

  void setPowerCommand(double powerCommand);

  void setAntennaSignal(Byte[] speedAuth);

  void setBeaconSignal(Byte[] beaconSignal);

  /**
   * The following methods are getters/setters for data going to the Moving Block Overlay.
   */
  GpsLocation getGpsLocation();



}

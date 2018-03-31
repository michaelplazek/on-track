package trainmodel.model;

import trainmodel.model.GpsLocation;
import utils.train.TrainModelEnums.AntennaStatus;
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

  AntennaStatus getAntennaStatus();

  DoorStatus getLeftDoorStatus();

  DoorStatus getRightDoorStatus();

  OnOffStatus getLightStatus();

  double getCurrentTemp();

  //Setters

  //  void setCurrentSpeed(double currentSpeed); shouldn't be able to set current speed 2/19/18

  void setEmergencyBrakeStatus(OnOffStatus brakeStatus);

  void setServiceBrakeStatus(OnOffStatus brakeStatus);

  void setTrackLineStatus(TrackLineStatus trackLineStatus);

  void setAntennaStatus(AntennaStatus antennaStatus);

  void setLeftDoorStatus(DoorStatus leftDoorStatus);

  void setRightDoorStatus(DoorStatus rightDoorStatus);

  void setLightStatus(OnOffStatus lightStatus);

  void setHeaterStatus(OnOffStatus heaterStatus);

  void setAcStatus(OnOffStatus acStatus);

  void setCurrentTemp(double currentTemp);

  void setPowerCommand(double powerCommand);

  void setBeaconSignal(Byte[] beaconSignal);

  /**
   * The following methods are getters/setters for data going to the Moving Block Overlay.
   */
  GpsLocation getGpsLocation();



}

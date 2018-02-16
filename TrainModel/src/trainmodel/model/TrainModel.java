package trainmodel.model;

import trainmodel.TrainModelInterface;
import utils.TrainModelEnums;

/**
 * Created by jeremyzang on 2/16/18.
 */
public class TrainModel implements TrainModelInterface {

  

  private double currentSpeed;
  private TrainModelEnums.BrakeStatus emergencyBrakeStatus;
  private TrainModelEnums.BrakeStatus serviceBrakeStatus;
  private TrainModelEnums.TrackLineStatus trackLineStatus;
  private TrainModelEnums.AntennaStatus antennaStatus;
  private TrainModelEnums.DoorStatus leftDoorStatus;
  private TrainModelEnums.DoorStatus rightDoorStatus;
  private TrainModelEnums.LightStatus lightStatus;

  @Override
  public double getCurrentSpeed() {
    return 0;
  }

  @Override
  public TrainModelEnums.BrakeStatus getEmergencyBrakeStatus() {
    return null;
  }

  @Override
  public TrainModelEnums.BrakeStatus getServiceBrakeStatus() {
    return null;
  }

  @Override
  public TrainModelEnums.TrackLineStatus getTrackLineStatus() {
    return null;
  }

  @Override
  public TrainModelEnums.AntennaStatus getAntennaStatus() {
    return null;
  }

  @Override
  public TrainModelEnums.DoorStatus getLeftDoorStatus() {
    return null;
  }

  @Override
  public TrainModelEnums.DoorStatus getRightDoorStatus() {
    return null;
  }

  @Override
  public TrainModelEnums.LightStatus getLightStatus() {
    return null;
  }

  @Override
  public double getCurrentTemp() {
    return 0;
  }

  @Override
  public void setCurrentSpeed(double currentSpeed) {

  }

  @Override
  public void setEmergencyBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus) {

  }

  @Override
  public void setServiceBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus) {

  }

  @Override
  public void setTrackLineStatus(TrainModelEnums.TrackLineStatus trackLineStatus) {

  }

  @Override
  public void setAntennaStatus(TrainModelEnums.AntennaStatus antennaStatus) {

  }

  @Override
  public void setLeftDoorStatus(TrainModelEnums.DoorStatus leftDoorStatus) {

  }

  @Override
  public void setRightDoorStatus(TrainModelEnums.DoorStatus rightDoorStatus) {

  }

  @Override
  public void setLightStatus(TrainModelEnums.LightStatus lightStatus) {

  }

  @Override
  public void setCurrentTemp(double currentTemp) {

  }

  @Override
  public double getGPSLocation() {
    return 0;
  }
}

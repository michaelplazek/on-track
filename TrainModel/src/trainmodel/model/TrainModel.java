package trainmodel.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import mainmenu.Clock;
import traincontroller.model.TrainControllerInterface;
import trainmodel.TrainModelInterface;
import trainmodel.controller.Constants;
import utils.TrainModelEnums;

/**
 * Created by jeremyzang on 2/16/18.
 */
public class TrainModel implements TrainModelInterface {

  //======================================================================================
  //Physics Parameters
  //Actual Speed in mph. Will be bound with  UI and Calculated from the power command.
  private DoubleProperty actualSpeed = new SimpleDoubleProperty(0);

  //Current temp inside the train.
  private DoubleProperty currentTemp = new SimpleDoubleProperty();

  private Clock clock = Clock.getInstance();

  //===========================================
  //SPEC SHEET INFO:
  //Max Speed = 70km/hr
  //Assumed w/ 2/3 load:
  //  Medium acceleration 0.5 m/s^2
  //  Service brake deceleration 1.2 m/s^2
  //  Emergency brake deceleration 2.73 m/s^2
  //Empty Car Weight = 40.9tonnes (Metric ton) 1 metric ton = 1000kg
  //          40.9t = 40900kg
  //============================================

  private double mass = 40900; //in kg
  private double acceleration = 0; //in m/s^2
  private double force = 0; //in N
  private double velocity = 0; //in m/s

  //Should kp/ki be in train model?
  private double kp = 1;
  private double ki = 1;

  //set by TrainController.
  private double powerCommand = 0; //In kilo Watts.
  private final int maxPower = 120; //(Max motor power = 120kW)

  private int numPassengers = 0;

  private TrainModelEnums.BrakeStatus emergencyBrakeStatus;
  private TrainModelEnums.BrakeStatus serviceBrakeStatus;
  private TrainModelEnums.TrackLineStatus trackLineStatus;
  private TrainModelEnums.AntennaStatus antennaStatus;
  private TrainModelEnums.DoorStatus leftDoorStatus;
  private TrainModelEnums.DoorStatus rightDoorStatus;
  private TrainModelEnums.LightStatus lightStatus;

  private GpsLocation gpsLocation;

  //Speed and Authority from MBO gets passed to TrainController in MBO mode.
  private Byte[] mboSpeedAuth;

  //Speed and Authority from trackModel gets passed to TrainController in Manual Mode
  private Byte[] trackModelSpeedAuth;
  private Byte[] beaconSignal;

  private TrainControllerInterface controller;
  private String id; //Train id
  private String line; //Train line (green or red)

  /**
   * Constructor that takes an id, line and TrainControllerInterface.
   * Factory Class will call this constructor in the createTrain() method. 2/21/19.
   * @param controller the TrainController for the created instance.
   * @param id id for the train must be unique.
   * @param line the line this train will be on at time of construction.
   */
  public TrainModel(TrainControllerInterface controller, String id, String line) {
    this.controller = controller;
    this.id = id;
    this.line = line;
  }


  @Override
  public double getCurrentSpeed() {
    return actualSpeed.getValue();
  }

  @Override
  public TrainModelEnums.BrakeStatus getEmergencyBrakeStatus() {
    return emergencyBrakeStatus;
  }

  @Override
  public TrainModelEnums.BrakeStatus getServiceBrakeStatus() {
    return serviceBrakeStatus;
  }

  @Override
  public TrainModelEnums.TrackLineStatus getTrackLineStatus() {
    return trackLineStatus;
  }

  @Override
  public TrainModelEnums.AntennaStatus getAntennaStatus() {
    return antennaStatus;
  }

  @Override
  public TrainModelEnums.DoorStatus getLeftDoorStatus() {
    return leftDoorStatus;
  }

  @Override
  public TrainModelEnums.DoorStatus getRightDoorStatus() {
    return rightDoorStatus;
  }

  @Override
  public TrainModelEnums.LightStatus getLightStatus() {
    return lightStatus;
  }

  @Override
  public double getCurrentTemp() {
    return currentTemp.getValue();
  }

  @Override
  public void setEmergencyBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus) {
    this.emergencyBrakeStatus = brakeStatus;
  }

  @Override
  public void setServiceBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus) {
    this.serviceBrakeStatus = brakeStatus;
  }

  @Override
  public void setTrackLineStatus(TrainModelEnums.TrackLineStatus trackLineStatus) {
    this.trackLineStatus = trackLineStatus;
  }

  @Override
  public void setAntennaStatus(TrainModelEnums.AntennaStatus antennaStatus) {
    this.antennaStatus = antennaStatus;
  }

  @Override
  public void setLeftDoorStatus(TrainModelEnums.DoorStatus leftDoorStatus) {
    this.leftDoorStatus = leftDoorStatus;
  }

  @Override
  public void setRightDoorStatus(TrainModelEnums.DoorStatus rightDoorStatus) {
    this.rightDoorStatus = rightDoorStatus;
  }

  @Override
  public void setLightStatus(TrainModelEnums.LightStatus lightStatus) {
    this.lightStatus = lightStatus;
  }

  @Override
  public void setCurrentTemp(double currentTemp) {
    this.currentTemp.setValue(currentTemp);
  }

  //Will be called by TrainController to give TrainModel the power command.
  @Override
  public void setPowerCommand(double powerCommand) {
    this.powerCommand = powerCommand;
  }

  //Will get called by MBO (or TrackModel?)
  // - to send speed and authority over antenna and pass to TrainController.
  @Override
  public void setAntennaSignal(Byte[] speedAuth) {

    //if Manual Mode call this
    this.controller.setAntennaSignal(speedAuth);

    //if MBO mode call this
    // this.controller.setAntennaSignal(mboSpeedAuth);
  }

  @Override
  public void setBeaconSignal(Byte[] beaconSignal) {
    //When in manual mode Speed/Auth and Beacon signal comes from track model.
    //when in manual mode call this.
    this.controller.setBeaconSignal(beaconSignal);
  }

  @Override
  public GpsLocation getGpsLocation() {
    return gpsLocation;
  }

  public int getMaxPower() {
    return maxPower;
  }

  public double getMass() {
    return mass;
  }

  public void addPassengers(int numberOfPassengers){
    this.numPassengers = numPassengers + numberOfPassengers;
    this.mass = mass + (Constants.passengerAvgMassKg * numberOfPassengers);
  }

  public void removePassengers(int numberOfPassengers){
    this.numPassengers = numPassengers - numberOfPassengers;
    this.mass = mass - (Constants.passengerAvgMassKg * numberOfPassengers);
  }
}

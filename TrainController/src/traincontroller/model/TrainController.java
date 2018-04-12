
package traincontroller.model;

import java.util.HashMap;

import javafx.beans.property.*;
import trackmodel.model.Beacon;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.enums.Mode;
import trainmodel.model.TrainModelFactory;
import trainmodel.model.TrainModelInterface;
import utils.general.Authority;
import utils.train.DoorStatus;
import utils.train.OnOffStatus;
import utils.train.TrainData;
import utils.unitconversion.UnitConversions;



public class TrainController implements TrainControllerInterface {

  public static final double FORCE_BRAKE_TRAIN_EMPTY = 123436.2;

  private SimpleBooleanProperty automatic;
  private Mode mode;
  private boolean underground;
  private Block currentBlock;
  private Block lastBlock;
  private double integral;
  private double distanceIntoCurrentBlock;
  private double distanceToStation;
  private Beacon beacon;
  private HashMap<Integer, Beacon> beacons;
  private double weight;

  private TrainModelInterface trainModel;
  private SimpleStringProperty id;
  private SimpleStringProperty line;
  private boolean running;
  private SimpleDoubleProperty currentSpeedMph;
  private double currentSpeed;
  private SimpleDoubleProperty setSpeedMph;
  private double setSpeed;
  private ObjectProperty<Authority> authority;
  private ObjectProperty<OnOffStatus> serviceBrakeStatus;
  private ObjectProperty<OnOffStatus> emergencyBrakeStatus;
  private ObjectProperty<OnOffStatus> lightStatus;
  private SimpleDoubleProperty powerCommand;
  private SimpleDoubleProperty driverSetSpeedMph;
  private double driverSetSpeed;
  private SimpleDoubleProperty setTemperature;
  private SimpleDoubleProperty currentTemperature;
  private SimpleDoubleProperty kp;
  private SimpleDoubleProperty ki;
  private SimpleStringProperty currentStation;
  private SimpleStringProperty nextStation;
  private ObjectProperty<DoorStatus> rightDoorStatus;
  private ObjectProperty<DoorStatus> leftDoorStatus;

  /**
   * Base constructor for TrainController.
   * @param id String for name of train
   * @param line String for line that train is running on
   */
  TrainController(String id, String line) {
    this.trainModel = TrainModelFactory.createTrainModel(this, id, line);
    this.automatic = new SimpleBooleanProperty(true);
    this.underground = false;

    this.id = new SimpleStringProperty(id);
    this.line = new SimpleStringProperty(line);
    this.currentSpeedMph = new SimpleDoubleProperty(0);
    this.currentSpeed = 0;
    this.authority = new SimpleObjectProperty<>(Authority.SERVICE_BRAKE_STOP);
    this.serviceBrakeStatus = new SimpleObjectProperty<>(trainModel.getServiceBrakeStatus());
    this.emergencyBrakeStatus = new SimpleObjectProperty<>(trainModel.getEmergencyBrakeStatus());
    this.setSpeedMph = new SimpleDoubleProperty(0);
    this.setSpeed = 0;
    this.powerCommand = new SimpleDoubleProperty(0);
    this.driverSetSpeedMph = new SimpleDoubleProperty(0);
    this.driverSetSpeed = 0;
    this.setTemperature = new SimpleDoubleProperty(68);
    this.currentTemperature = new SimpleDoubleProperty(trainModel.getCurrentTemp());
    this.kp = new SimpleDoubleProperty(50);
    this.ki = new SimpleDoubleProperty(50);
    this.currentStation = new SimpleStringProperty("N/A");
    this.nextStation = new SimpleStringProperty("N/A");
    this.integral = 0;
    this.running = false;
    this.currentBlock = Track.getListOfTracks().get(line).getStartBlock();
    this.lastBlock = Track.getTrack(line).getBlock(-1);
    this.beacons = new HashMap<>();
    this.weight = TrainData.EMPTY_WEIGHT * TrainData.NUMBER_OF_CARS
        + TrainData.MAX_PASSENGERS * 2 * 150 * UnitConversions.LBS_TO_KGS;
    this.rightDoorStatus = new SimpleObjectProperty<>(trainModel.getRightDoorStatus());
    this.leftDoorStatus = new SimpleObjectProperty<>(trainModel.getLeftDoorStatus());
    this.lightStatus = new SimpleObjectProperty<>(trainModel.getLightStatus());
  }

  /**
   * Pass beacon signal to the train controller.
   * @param signal Beacon signal from track.
   */
  public void setBeaconSignal(Beacon signal) {
    if (signal.isUnderground()) {
      this.underground = !this.underground;
    }
    if (beacons.get(signal.getBlockId()) == null) {
      beacon = new Beacon(signal);
      beacons.put(signal.getBlockId(), beacon);
      if (signal.getStationId() >= 0 && authority.getValue() == Authority.STOP_AT_NEXT_STATION) {
        distanceToStation = signal.getDistance();
        setCurrentStation(Track.getListOfTracks().get(getLine())
            .getStationList().get(signal.getStationId()));
      }
    } else {
      beacons.remove(signal.getBlockId());
    }
  }

  /**
   * Sets set speed and authority received through track circuit,
   * which causes the integral to reset.
   * @param setSpeed set speed that the train should aim for
   * @param authority authority of the train
   */
  public void setTrackCircuitSignal(float setSpeed, Authority authority) {
    double speed = setSpeed * UnitConversions.MPH_TO_KPH * 1000.0 / 3600.0;
    if (speed != this.getSetSpeed() && speed != 0) {
      setSetSpeed(speed);
      if (speed < driverSetSpeed) {
        setDriverSetSpeed(speed);
      }
    }
    if (authority != null && getAuthority() != authority) {
      this.authority.set(authority);
      switch (authority) {
        case SERVICE_BRAKE_STOP:
          setMode(Mode.CTC_BRAKE);
          break;
        case SEND_POWER:
          setMode(Mode.NORMAL);
          break;
        case STOP_AT_NEXT_STATION:
          setMode(Mode.STATION_BRAKE);
          break;
        case EMERGENCY_BRAKE_STOP:
        default:
          setMode(Mode.CTC_EMERGENCY_BRAKE);
          break;
      }
    }
  }

  protected String getId() {
    return id.getValue();
  }

  protected String getLine() {
    return line.getValue();
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
  }

  public TrainModelInterface getTrainModel() {
    return trainModel;
  }

  public boolean isRunning() {
    return running;
  }

  public ObjectProperty<Authority> getAuthorityProperty() {
    return authority;
  }

  public Authority getAuthority() {
    return authority.getValue();
  }

  public void setAuthority(Authority authority) {
    this.authority.set(authority);
  }

  public SimpleDoubleProperty getCurrentSpeedProperty() {
    return currentSpeedMph;
  }

  public Double getCurrentSpeed() {
    return currentSpeed;
  }

  public void setCurrentSpeed(Double currentSpeed) {
    this.currentSpeed = currentSpeed;
    this.currentSpeedMph.set(currentSpeed * UnitConversions.MPS_TO_MPH);
  }

  public SimpleDoubleProperty getPowerCommandProperty() {
    return powerCommand;
  }

  public Double getPowerCommand() {
    return powerCommand.getValue();
  }

  public void setPowerCommand(double powerCommand) {
    trainModel.setPowerCommand(powerCommand);
    this.powerCommand.set(powerCommand);
  }

  public SimpleDoubleProperty getSetSpeedProperty() {
    return setSpeedMph;
  }

  public Double getSetSpeed() {
    return setSpeed;
  }

  public void setSetSpeed(Double setSpeed) {
    this.setSpeed = setSpeed;
    this.setSpeedMph.set(setSpeed * UnitConversions.MPS_TO_MPH);
  }

  public SimpleDoubleProperty getDriverSetSpeedProperty() {
    return driverSetSpeedMph;
  }

  public Double getDriverSetSpeed() {
    return driverSetSpeed;
  }

  public void setDriverSetSpeed(Double driverSetSpeed) {
    this.driverSetSpeed = driverSetSpeed;
    this.driverSetSpeedMph.set(driverSetSpeed * UnitConversions.MPS_TO_MPH);
  }

  public SimpleDoubleProperty getSetTemperatureProperty() {
    return setTemperature;
  }

  public Double getSetTemperature() {
    return setTemperature.getValue();
  }

  public void setSetTemperature(Double setTemperature) {
    this.setTemperature.set(setTemperature);
  }

  public SimpleDoubleProperty getCurrentTemperatureProperty() {
    return currentTemperature;
  }

  public Double getCurrentTemperature() {
    return currentTemperature.getValue();
  }

  public void setCurrentTemperature(Double currentTemperature) {
    this.currentTemperature.set(currentTemperature);
  }

  public SimpleDoubleProperty getKpProperty() {
    return kp;
  }

  public Double getKp() {
    return kp.getValue();
  }

  public void setKp(Double kp) {
    this.kp.set(kp);
  }

  public SimpleDoubleProperty getKiProperty() {
    return ki;
  }

  public Double getKi() {
    return ki.getValue();
  }

  public void setKi(Double ki) {
    this.ki.set(ki);
  }

  public SimpleStringProperty getCurrentStationProperty() {
    return currentStation;
  }

  public String getCurrentStation() {
    return currentStation.getValue();
  }

  public void setCurrentStation(String currentStation) {
    this.currentStation.set(currentStation);
  }

  public SimpleStringProperty getNextStationProperty() {
    return nextStation;
  }

  public String getNextStation() {
    return nextStation.getValue();
  }

  public void setNextStation(String nextStation) {
    this.nextStation.set(nextStation);
  }

  public boolean isAutomatic() {
    return automatic.getValue();
  }

  public void setAutomatic(boolean automatic) {
    this.automatic.setValue(automatic);
    if (automatic) {
      if (mode == Mode.DRIVER_BRAKE) {
        setTrackCircuitSignal(0, authority.getValue());
      }
    } else {
      setDriverSetSpeed(getSetSpeed());
    }
  }

  public SimpleBooleanProperty automaticProperty() {
    return automatic;
  }

  public Block getCurrentBlock() {
    return currentBlock;
  }

  public void setCurrentBlock(Block currentBlock) {
    this.currentBlock = currentBlock;
  }

  public Block getLastBlock() {
    return this.lastBlock;
  }

  public void setLastBlock(Block lastBlock) {
    this.lastBlock = lastBlock;
  }

  @Override
  public void activateEmergencyBrake() {
    setPowerCommand(0);
    setAutomatic(false);
    this.mode = Mode.CTC_EMERGENCY_BRAKE;
    this.setEmergencyBrake(OnOffStatus.ON);
  }

  public void setRightDoorStatus(DoorStatus doorStatus) {
    trainModel.setRightDoorStatus(doorStatus);
    rightDoorStatus.setValue(doorStatus);
  }

  public DoorStatus getRightDoorStatus() {
    return trainModel.getRightDoorStatus();
  }

  public void setLeftDoorStatus(DoorStatus doorStatus) {
    trainModel.setLeftDoorStatus(doorStatus);
    leftDoorStatus.setValue(doorStatus);
  }

  public DoorStatus getLeftDoorStatus() {
    return leftDoorStatus.get();
  }

  public ObjectProperty<DoorStatus> rightDoorStatusProperty() {
    return rightDoorStatus;
  }

  public ObjectProperty<DoorStatus> leftDoorStatusProperty() {
    return leftDoorStatus;
  }

  public void setLightStatus(OnOffStatus lightStatus) {
    trainModel.setLightStatus(lightStatus);
    this.lightStatus.setValue(lightStatus);
  }

  public ObjectProperty<OnOffStatus> lightStatusProperty() {
    return lightStatus;
  }

  public OnOffStatus getLightStatus() {
    return trainModel.getLightStatus();
  }

  public void setIntegral(double integral) {
    this.integral = integral;
  }

  public double getIntegral() {
    return integral;
  }

  public void setDistanceIntoCurrentBlock(double distanceIntoCurrentBlock) {
    this.distanceIntoCurrentBlock = distanceIntoCurrentBlock;
  }

  public double getDistanceIntoCurrentBlock() {
    return distanceIntoCurrentBlock;
  }

  protected void start() {
    running = true;
  }

  public boolean isUnderground() {
    return underground;
  }

  public void setUnderground(boolean underground) {
    this.underground = underground;
  }

  public Beacon getBeacon() {
    return beacon;
  }

  public void setBeacon(Beacon beacon) {
    this.beacon = beacon;
  }

  public double getDistanceToStation() {
    return distanceToStation;
  }

  public void setDistanceToStation(double distanceToStation) {
    this.distanceToStation = distanceToStation;
  }

  public HashMap<Integer, Beacon> getBeacons() {
    return beacons;
  }

  public double getWeight() {
    return weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public OnOffStatus getServiceBrakeStatus() {
    return serviceBrakeStatus.get();
  }

  public ObjectProperty<OnOffStatus> serviceBrakeStatusProperty() {
    return serviceBrakeStatus;
  }

  public OnOffStatus getEmergencyBrakeStatus() {
    return emergencyBrakeStatus.get();
  }

  public ObjectProperty<OnOffStatus> emergencyBrakeStatusProperty() {
    return emergencyBrakeStatus;
  }

  public void setServiceBrake(OnOffStatus brakeStatus) {
    this.integral = 0;
    trainModel.setServiceBrakeStatus(brakeStatus);
    this.serviceBrakeStatus.set(brakeStatus);
  }

  public void setEmergencyBrake(OnOffStatus brakeStatus) {
    this.integral = 0;
    trainModel.setEmergencyBrakeStatus(brakeStatus);
    this.emergencyBrakeStatus.set(brakeStatus);
  }
}

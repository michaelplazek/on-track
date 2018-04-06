package trainmodel.model;

import java.util.HashMap;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;
import mainmenu.controller.MainMenuController;
import sun.tools.jconsole.Plotter;
import trackmodel.model.Beacon;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerInterface;
import utils.train.TrainData;
import utils.train.TrainModelEnums.AntennaStatus;
import utils.train.TrainModelEnums.DoorStatus;
import utils.train.TrainModelEnums.OnOffStatus;
import utils.train.TrainModelEnums.TrackLineStatus;
import utils.unitconversion.UnitConversions;




/**
 * Created by jeremyzang on 2/16/18.
 */
public class TrainModel implements TrainModelInterface {


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
  //Train Dimentions
  private DoubleProperty height = new SimpleDoubleProperty(TrainData.HEIGHT_OF_TRAIN * UnitConversions.METERS_TO_FT);
  private DoubleProperty width = new SimpleDoubleProperty(TrainData.WIDTH_OF_TRAIN * UnitConversions.METERS_TO_FT);
  private DoubleProperty lengthOfTrain = new SimpleDoubleProperty(TrainData.LENGTH_OF_TRAIN * UnitConversions.METERS_TO_FT);
  private DoubleProperty numberOfCars = new SimpleDoubleProperty(TrainData.NUMBER_OF_CARS);

  //String Properties to be bound with UI.
  private DoubleProperty mass = new SimpleDoubleProperty(TrainData.EMPTY_WEIGHT);
  private DoubleProperty mass_lbs = new SimpleDoubleProperty(mass.get() * UnitConversions.KGS_TO_LBS);

  private DoubleProperty velocity = new SimpleDoubleProperty(0); //in m/s
  private DoubleProperty velocity_mph = new SimpleDoubleProperty(velocity.get() * UnitConversions.MPS_TO_MPH);

  private DoubleProperty currentTemp
      = new SimpleDoubleProperty(70); //Current temp inside the train.
  private DoubleProperty setTemp
      = new SimpleDoubleProperty(70); //Set temp (will be set by TrainController)
  private DoubleProperty setSpeed = new SimpleDoubleProperty(0); //To link UI w/ TrainController
  private DoubleProperty setAuthority = new SimpleDoubleProperty(0); //To link UI w/ TrainController
  private StringProperty nextStation = new SimpleStringProperty("N/A"); //To link w/ UI
  private StringProperty trainStatus = new SimpleStringProperty("N/A"); //To link w/ UI


  //set by TrainController.
  private DoubleProperty powerCommand = new SimpleDoubleProperty(0); //In kilo Watts.
  private IntegerProperty numPassengers = new SimpleIntegerProperty(0);

  private ObjectProperty<OnOffStatus> lightStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<DoorStatus> rightDoorStatus
      = new SimpleObjectProperty<>(DoorStatus.CLOSED);
  private ObjectProperty<DoorStatus> leftDoorStatus
      = new SimpleObjectProperty<>(DoorStatus.CLOSED);
  private ObjectProperty<AntennaStatus> antennaStatus
      = new SimpleObjectProperty<>(AntennaStatus.CONNECTED);
  private ObjectProperty<TrackLineStatus> trackLineStatus
      = new SimpleObjectProperty<>(TrackLineStatus.CONNECTED);
  private ObjectProperty<OnOffStatus> serviceBrakeStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<OnOffStatus> emergencyBrakeStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<OnOffStatus> heaterStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<OnOffStatus> acStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);

  private GpsLocation gpsLocation; //future development? 3/5/18
  private double acceleration = 0.0000001; //in m/s^2
  private double force = 0; //in N
  private boolean isMoving = false;
  private final int capacityOfTrain = TrainData.MAX_PASSENGERS * TrainData.NUMBER_OF_CARS;
  private double positionInBlock = 0; //The number of meters from the border of the current block.
  // Measured from the previous boarder to front of train.

  private Track activeTrack;
  private Block currentBlock; //where the head of the train is.
  private StringProperty currentBlockName = new SimpleStringProperty("Yard");
  private StringProperty activeTrackName = new SimpleStringProperty("");
  private Block previousBlock;
  
  private Block trailingBlock; // used when train spans over 2 blocks. Maybe?

  private boolean isMovingBlockMode = false;

  //Speed and Authority from MBO gets passed to TrainController in MBO mode.
  // private float mboSpeed;
  // private float mboAuthority;

  //Speed and Authority from trackModel gets passed to TrainController in Manual Mode
  // private float trackModelSpeed;
  // private float trackModelAuthority;

  private Byte[] beaconSignal;

  private static HashMap<String, TrainModel> listOfTrainModels = new HashMap<>();

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

  /**
   * Adds a number of passengers to the train.
   * @param numberOfPassengers The number of passengers to add.
   */
  public void addPassengers(int numberOfPassengers) {
    int availableSeats = TrainData.MAX_PASSENGERS - this.numPassengers.get();

    if (numberOfPassengers <= availableSeats) {
      this.numPassengers.set(numPassengers.get() + numberOfPassengers);
      this.mass.set(mass.get() + (TrainData.PASSENGER_WEIGHT * numberOfPassengers));
      this.mass_lbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    } else {
      //If numberOfPassengers is >= available seats as the most you can.
      int passengersTotal = this.numPassengers.get() + availableSeats;
      this.numPassengers.set(passengersTotal);
      this.mass.set(TrainData.EMPTY_WEIGHT + (TrainData.PASSENGER_WEIGHT * passengersTotal));
      this.mass_lbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    }
  }

  /**
   * Removes a number of passengers from the train.
   * @param numberOfPassengers The number of passengers to be removed.
   */
  public void removePassengers(int numberOfPassengers) {
    if ((this.numPassengers.get() - numberOfPassengers) >= 0) {
      this.numPassengers.set(numPassengers.get() - numberOfPassengers);
      this.mass.set(mass.get() - (TrainData.PASSENGER_WEIGHT * numberOfPassengers));
      this.mass_lbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    } else {
      this.numPassengers.set(0);
      this.mass.set(
          (TrainData.EMPTY_WEIGHT + (TrainData.MAX_PASSENGERS * TrainData.PASSENGER_WEIGHT))
          - (TrainData.MAX_PASSENGERS * TrainData.PASSENGER_AVG_MASS_KG));
      this.mass_lbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    }
  }
  
  private void setAndConvertMassToImperial(Double mass) {
    this.mass.setValue(mass * UnitConversions.KGS_TO_LBS);
  }

  /**
   * This will start the movement of the train.
   */
  private void start() {
    isMoving = true;
    if (velocity.get() == 0) {
      acceleration = .000001;
    } else {
      acceleration = powerCommand.get() / (mass.get() * velocity.get());
    }
  }

  /**
   * Updates position in current block.
   * If change in distance causes a block change then this method should update the current block
   *  and the current position in the updated block.
   */
  private void updatePosition() {
    double changeInDist = changeInDist();
    if (isCrossingBlock(changeInDist)) {
      positionInBlock = (positionInBlock + changeInDist) - currentBlock.getSize();
      updateCurrentBlock();
      updatePreviousBlock();
    } else {
      positionInBlock = positionInBlock + changeInDist;
    }

  }

  /**
   * Updates current block to next block.
   */
  private void updateCurrentBlock() {
    Block next = activeTrack.getBlock(currentBlock.getNextBlock1());
    currentBlock = next;
    updateBeacon();
  }

  /**
   * Updates previous block.
   */
  private void updatePreviousBlock() {
    Block previous = activeTrack.getBlock(currentBlock.getPreviousBlock());
    previousBlock = previous;
  }

  /**
   * Updates occupancy of a block when train changes blocks.
   */
  private void updateOccupancy() {
    currentBlock.setOccupied(true);
    previousBlock.setOccupied(false);
  }

  /**
   * Updates velocity.
   */
  private void updateVelocity() {
    velocity.set(powerCommand.get() / (mass.get() * acceleration));
    velocity_mph.set(velocity.getValue() * UnitConversions.MPS_TO_MPH);
  }

  /**
   * Updates acceleration.
   */
  private void updateAcceleration() {
    acceleration = powerCommand.get() / (mass.get() * velocity.get());
  }

  /**
   * Updates force.
   */
  private void updateForce() {
    force = mass.get() * acceleration;
  }

  /**
   * Calculates the change in distance from the velocity and change in time.
   * @return the change in distance based on velocity of train and change in time.
   */
  private double changeInDist() {
    return velocity.get() / clock.getChangeInTime();
  }

  /**
   * Runs simulation. This will be called from main.
   */
  public void run() {
    if (!isMoving) {
      start();
    } else {
      updateAcceleration();
      updateVelocity();
      updateForce();
      updatePosition();
      updateSpeedAuth();
      brake();
      updateOccupancy();
      changeTemperature();

    }
  }

  /**
   * Runs all instances of Trains.
   */
  public static void runAllInstances() {
    for (TrainModel train : listOfTrainModels.values()) {
      train.run();
    }
  }

  private void updateSpeedAuth() {
    this.controller.setTrackCircuitSignal(currentBlock.getSetPointSpeed(),
        currentBlock.getAuthority());
  }

  private void updateBeacon() {
    if (currentBlock.getBeacon() != null) {
      this.controller.setBeaconSignal(currentBlock.getBeacon());
    }
  }

  /**
   * Slows train down if brakes are engaged.
   */
  private void brake() {
    double deceleration = 0;
    if (emergencyBrakeStatus.toString().equals(OnOffStatus.ON.toString())) {
      deceleration = TrainData.EMERGENCY_BRAKE_ACCELERATION * clock.getChangeInTime();
      velocity.set(velocity.get() - deceleration);
    } else if (emergencyBrakeStatus.toString().equals(OnOffStatus.OFF.toString())
        && serviceBrakeStatus.toString().equals(OnOffStatus.ON.toString())) {
      deceleration = TrainData.SERVICE_BRAKE_ACCELERATION * clock.getChangeInTime();
      velocity.set(velocity.get() - deceleration);
    }
  }

  /**
   * Helper method to return true if a change in distance crosses block boarders.
   * @param distChange The distance the train moved.
   * @return true if train crosses block boarder, false otherwise.
   */
  private boolean isCrossingBlock(Double distChange) {
    return ((positionInBlock + distChange) > currentBlock.getSize());
  }

  /**
   * Adds Train Model to the list.
   * @param train the TrainModel to be added.
   */
  protected static void add(TrainModel train) {
    listOfTrainModels.put(train.getId(), train);
    MainMenuController.getInstance().updateTrainModelDropdown();
  }

  /**
   * Removes a trainModel from the list.
   * */
  public static boolean delete(String trainId) {
    TrainModel temp = listOfTrainModels.get(trainId);
    if (temp == null) {
      return false;
    }
    listOfTrainModels.remove(trainId);
    MainMenuController.getInstance().updateTrainModelDropdown();
    return true;
  }

  /**
   * Toggles status of trains heater.
   */
  public void toggleHeater() {
    if (heaterStatus.get().equals(OnOffStatus.ON)) {
      heaterStatus.setValue(OnOffStatus.OFF);
    } else {
      heaterStatus.setValue(OnOffStatus.ON);
      acStatus.setValue(OnOffStatus.OFF);
    }
  }

  /**
   * Toggles status of trains A/C.
   */
  public void toggleAc() {
    if (acStatus.get().equals(OnOffStatus.ON)) {
      acStatus.setValue(OnOffStatus.OFF);
    } else {
      acStatus.setValue(OnOffStatus.ON);
      heaterStatus.setValue(OnOffStatus.OFF);
    }
  }

  /**
   * Called in Run() method to change temperature if needed.
   */
  private void changeTemperature() {
    if (this.needsCooled()) {
      coolTrain();
    } else if (this.needsHeated()) {
      heatTrain();
    }
  }

  private void coolTrain() {
    heaterStatus.set(OnOffStatus.OFF);
    acStatus.setValue(OnOffStatus.ON);

    currentTemp.setValue(currentTemp.getValue()
        - (TrainData.TEMPERATURE_RATE_OF_CHANGE * clock.getChangeInTime()));
  }

  private void heatTrain() {
    heaterStatus.set(OnOffStatus.ON);
    acStatus.setValue(OnOffStatus.OFF);

    currentTemp.setValue(currentTemp.getValue()
        + (TrainData.TEMPERATURE_RATE_OF_CHANGE * clock.getChangeInTime()));
  }

  private boolean needsCooled() {
    if (currentTemp.getValue() > setTemp.getValue()) {
      return true;
    } else {
      return false;
    }
  }

  private boolean needsHeated() {
    if (currentTemp.getValue() < setTemp.getValue()) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Setters.
   */
  @Override
  public void setEmergencyBrakeStatus(OnOffStatus brakeStatus) {
    this.emergencyBrakeStatus.set(brakeStatus);
  }

  @Override
  public void setServiceBrakeStatus(OnOffStatus brakeStatus) {
    this.serviceBrakeStatus.set(brakeStatus);
  }

  @Override
  public void setTrackLineStatus(TrackLineStatus trackLineStatus) {
    this.trackLineStatus.set(trackLineStatus);
  }

  @Override
  public void setAntennaStatus(AntennaStatus antennaStatus) {
    this.antennaStatus.set(antennaStatus);
  }

  @Override
  public void setLeftDoorStatus(DoorStatus leftDoorStatus) {
    this.leftDoorStatus.set(leftDoorStatus);
  }

  @Override
  public void setRightDoorStatus(DoorStatus rightDoorStatus) {
    this.rightDoorStatus.set(rightDoorStatus);
  }

  @Override
  public void setLightStatus(OnOffStatus lightStatus) {
    this.lightStatus.set(lightStatus);
  }

  @Override
  public void setHeaterStatus(OnOffStatus heaterStatus) {
    this.heaterStatus.set(heaterStatus);
  }

  @Override
  public void setAcStatus(OnOffStatus acStatus) {
    this.acStatus.set(acStatus);
  }

  @Override
  public void setCurrentTemp(double currentTemp) {
    this.currentTemp.setValue(currentTemp);
  }

  //Will be called by TrainController to give TrainModel the power command.
  @Override
  public void setPowerCommand(double powerCommand) {
    this.powerCommand.set(powerCommand);
  }

  public void setMovingBlockMode(boolean movingBlockMode) {
    this.isMovingBlockMode = movingBlockMode;
  }

  public void setController(TrainControllerInterface controller) {
    this.controller = controller;
  }

  public void setActiveTrack(Track activeTrack) {
    this.activeTrack = activeTrack;
  }

  public void cutEnginePower() {
    this.powerCommand.set(0);
  }

  /**
   * Getters.
   */
  @Override
  public GpsLocation getGpsLocation() {
    return gpsLocation;
  }

  @Override
  public double getCurrentSpeed() {
    return velocity.getValue();
  }

  @Override
  public OnOffStatus getEmergencyBrakeStatus() {
    return emergencyBrakeStatus.get();
  }

  @Override
  public OnOffStatus getServiceBrakeStatus() {
    return serviceBrakeStatus.get();
  }

  @Override
  public TrackLineStatus getTrackLineStatus() {
    return trackLineStatus.get();
  }

  @Override
  public AntennaStatus getAntennaStatus() {
    return antennaStatus.get();
  }

  @Override
  public DoorStatus getLeftDoorStatus() {
    return leftDoorStatus.get();
  }

  @Override
  public DoorStatus getRightDoorStatus() {
    return rightDoorStatus.get();
  }

  @Override
  public OnOffStatus getLightStatus() {
    return lightStatus.get();
  }

  @Override
  public double getCurrentTemp() {
    return currentTemp.getValue();
  }



  public int getMaxPower() {
    return TrainData.MAX_POWER;
  }

  public double getMass() {
    return mass.get();
  }

  public double getAcceleration() {
    return acceleration;
  }

  public double getForce() {
    return force;
  }

  public double getVelocity() {
    return velocity.get();
  }

  public String getId() {
    return id;
  }

  public String getLine() {
    return line;
  }

  public double getPowerCommand() {
    return powerCommand.get();
  }

  public Track getActiveTrack() {
    return activeTrack;
  }

  public StringProperty activeTrackProperty() {
    return activeTrackName;
  }

  public Block getCurrentBlock() {
    return currentBlock;
  }

  public StringProperty currentBlockProperty() {
    return currentBlockName;
  }

  public Block getPreviousBlock() {
    return previousBlock;
  }

  public DoubleProperty currentTempProperty() {
    return currentTemp;
  }

  public DoubleProperty massProperty() {
    return mass;
  }

  public DoubleProperty mass_lbsProperty() {
    return mass_lbs;
  }

  public DoubleProperty velocityProperty() {
    return velocity;
  }

  public DoubleProperty velocity_mphProperty() {
    return velocity_mph;
  }

  public DoubleProperty powerCommandProperty() {
    return powerCommand;
  }

  public IntegerProperty numPassengersProperty() {
    return numPassengers;
  }

  public DoubleProperty heightProperty() {
    return height;
  }

  public DoubleProperty widthProperty() {
    return width;
  }

  public DoubleProperty lengthOfTrainProperty() {
    return lengthOfTrain;
  }

  public DoubleProperty numberOfCarsProperty() {
    return numberOfCars;
  }

  public ObjectProperty<OnOffStatus> emergencyBrakeStatusProperty() {
    return emergencyBrakeStatus;
  }

  public ObjectProperty<OnOffStatus> lightStatusProperty() {
    return lightStatus;
  }

  public ObjectProperty<DoorStatus> rightDoorStatusProperty() {
    return rightDoorStatus;
  }

  public ObjectProperty<DoorStatus> leftDoorStatusProperty() {
    return leftDoorStatus;
  }

  public ObjectProperty<AntennaStatus> antennaStatusProperty() {
    return antennaStatus;
  }

  public ObjectProperty<TrackLineStatus> trackLineStatusProperty() {
    return trackLineStatus;
  }

  public ObjectProperty<OnOffStatus> serviceBrakeStatusProperty() {
    return serviceBrakeStatus;
  }

  public ObjectProperty<OnOffStatus> heaterStatusProperty() {
    return heaterStatus;
  }

  public ObjectProperty<OnOffStatus> acStatusProperty() {
    return acStatus;
  }

  public int getCapacityOfTrain() {
    return capacityOfTrain;
  }

  public TrainControllerInterface getController() {
    return controller;
  }

  public static ObservableList<String> getObservableListOfTrainModels() {
    return FXCollections.observableArrayList(listOfTrainModels.keySet());
  }

  public boolean isMovingBlockMode() {
    return isMovingBlockMode;
  }

  public static TrainModel getTrainModel(String id) {
    return listOfTrainModels.get(id);
  }

  public static HashMap<String, TrainModel> getTrainModels() {
    return listOfTrainModels;
  }

}

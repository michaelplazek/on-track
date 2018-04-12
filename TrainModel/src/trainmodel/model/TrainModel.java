package trainmodel.model;

import java.util.HashMap;
import java.util.Random;

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
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerInterface;
import utils.general.Constants;
import utils.train.DoorStatus;
import utils.train.Failure;
import utils.train.OnOffStatus;
import utils.train.TrainData;
import utils.unitconversion.UnitConversions;


public class TrainModel implements TrainModelInterface {


  private Clock clock = Clock.getInstance();

  /* ===========================================
    SPEC SHEET INFO:
    Max Speed = 70km/hr
    Assumed w/ 2/3 load:
    Medium acceleration 0.5 m/s^2
    Service Brake deceleration 1.2 m/s^2
    Emergency Brake deceleration 2.73 m/s^2
    Empty Car Weight = 40.9tonnes (Metric ton) 1 metric ton = 1000kg
            40.9t = 40900kg
  ============================================ */

  // Train Dimensions
  private DoubleProperty height
      = new SimpleDoubleProperty(TrainData.HEIGHT_OF_TRAIN * UnitConversions.METERS_TO_FT);
  private DoubleProperty width
      = new SimpleDoubleProperty(TrainData.WIDTH_OF_TRAIN * UnitConversions.METERS_TO_FT);
  private DoubleProperty lengthOfTrain
      = new SimpleDoubleProperty(TrainData.LENGTH_OF_TRAIN * UnitConversions.METERS_TO_FT);
  private IntegerProperty numberOfCars = new SimpleIntegerProperty(TrainData.NUMBER_OF_CARS);

  //String Properties to be bound with UI.
  private DoubleProperty mass = new SimpleDoubleProperty(TrainData.EMPTY_WEIGHT);
  private DoubleProperty massLbs
      = new SimpleDoubleProperty(mass.get() * UnitConversions.KGS_TO_LBS);

  private DoubleProperty velocity = new SimpleDoubleProperty(0); //in m/s
  private DoubleProperty velocityMph
      = new SimpleDoubleProperty(velocity.get() * UnitConversions.MPS_TO_MPH);

  private DoubleProperty currentTemp
      = new SimpleDoubleProperty(70); //Current temp inside the train.

  //set by TrainController.
  private DoubleProperty powerCommand = new SimpleDoubleProperty(0); //In kilo Watts.
  private IntegerProperty numPassengers = new SimpleIntegerProperty(0);

  private ObjectProperty<OnOffStatus> lightStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<DoorStatus> rightDoorStatus
      = new SimpleObjectProperty<>(DoorStatus.CLOSED);
  private ObjectProperty<DoorStatus> leftDoorStatus
      = new SimpleObjectProperty<>(DoorStatus.CLOSED);
  private ObjectProperty<OnOffStatus> serviceBrakeStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<OnOffStatus> emergencyBrakeStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<OnOffStatus> heaterStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);
  private ObjectProperty<OnOffStatus> acStatus
      = new SimpleObjectProperty<>(OnOffStatus.OFF);

  //Failure Statuses
  private ObjectProperty<Failure> trackLineFailureStatus
      = new SimpleObjectProperty<>(Failure.WORKING);
  private ObjectProperty<Failure> engineFailureStatus
      = new SimpleObjectProperty<>(Failure.WORKING);
  private ObjectProperty<Failure> brakeFailureStatus
      = new SimpleObjectProperty<>(Failure.WORKING);


  private double acceleration = 0; //in m/s^2
  private double force = 0; //in N
  private double frictionForce = mass.get() * Constants.STEEL_FRICTION * Constants.GRAVITY;
  private boolean isMoving = false;
  private boolean isDispatched = false;
  private final int capacityOfTrain = TrainData.MAX_PASSENGERS * TrainData.NUMBER_OF_CARS;
  private double positionInBlock = 0; //The number of meters from the border of the current block.
  // Measured from the previous boarder to front of train.

  private Track activeTrack;
  private Block currentBlock; //where the head of the train is.
  private StringProperty currentBlockName = new SimpleStringProperty("Yard");
  private StringProperty activeTrackName = new SimpleStringProperty("");
  private Block previousBlock;
  
  private Block trailingBlock; // used when train spans over 2 blocks. Maybe?

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
  TrainModel(TrainControllerInterface controller, String id, String line) {
    this.controller = controller;
    this.id = id;
    this.line = line;
    this.activeTrack = Track.getTrack(line);
    this.activeTrackName.set(line);
    this.currentBlock = activeTrack.getStartBlock();
    this.previousBlock = activeTrack.getBlock(-1);
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
      this.massLbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    } else {
      //If numberOfPassengers is >= available seats as the most you can.
      int passengersTotal = this.numPassengers.get() + availableSeats;
      this.numPassengers.set(passengersTotal);
      this.mass.set(TrainData.EMPTY_WEIGHT + (TrainData.PASSENGER_WEIGHT * passengersTotal));
      this.massLbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
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
      this.massLbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    } else {
      this.numPassengers.set(0);
      this.mass.set(
          (TrainData.EMPTY_WEIGHT + (TrainData.MAX_PASSENGERS * TrainData.PASSENGER_WEIGHT))
          - (TrainData.MAX_PASSENGERS * TrainData.PASSENGER_AVG_MASS_KG));
      this.massLbs.set(mass.get() * UnitConversions.KGS_TO_LBS);
    }
  }

  /**
   * Called when train stops at a station.
   * When train is at a station a random number of passengers leave the train.
   */
  private void randomPassengersLeave() {
    Random randomPassengerNum = new Random();
    int numPassengers = randomPassengerNum.nextInt(TrainData.MAX_PASSENGERS);
    removePassengers(numPassengers);
  }

  /**
   * This will start the movement of the train.
   */
  @Override
  public void start() {
    isDispatched = true;
    isMoving = true;
    startEngine();
  }

  /**
   * Starts a train engine.
   */
  @Override
  public void startEngine() {
    isMoving = true;
    if (velocity.get() == 0) {
      velocity.set(0.001);
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
      positionInBlock = 0;
      updateCurrentBlock();
    } else {
      positionInBlock = positionInBlock + changeInDist;
    }

    if (currentBlock != null) {
      currentBlockName.set(currentBlock.getSection() + currentBlock.getNumber());
    }

    //    System.out.println("Change in position: " + changeInDist);
    //    System.out.println("Location in block: " + positionInBlock);
    //    System.out.println("Current block: " + currentBlock.getSize());
  }

  /**
   * Updates current block to next block.
   */
  private void updateCurrentBlock() {

    if (currentBlock.isSwitch()) {
      Switch sw = (Switch) currentBlock;

      if (activeTrack.getBlock(sw.getNextBlock1()) == previousBlock
          || activeTrack.getBlock(sw.getNextBlock2()) == previousBlock) {
        previousBlock = currentBlock;
        currentBlock = activeTrack.getBlock(sw.getPreviousBlock());
      } else {
        previousBlock = currentBlock;
        currentBlock = activeTrack.getBlock(sw.getStatus());
      }
    } else {
      Block temp = currentBlock;
      currentBlock = activeTrack.getNextBlock(currentBlock.getNumber(), previousBlock.getNumber());
      previousBlock = temp;
    }

    if (currentBlock == null) {
      isDispatched = false;
    } else {
      updateBeacon();
    }
  }

  /**
   * Updates occupancy of a block when train changes blocks.
   */
  private void updateOccupancy() {
    if (currentBlock != null) {
      currentBlock.setOccupied(true);
    }

  }

  /**
   * Updates velocity.
   */
  private void updateVelocity() {

    double v = velocity.get() + (acceleration * (clock.getChangeInTime() / 1000.0));

    if (v > 0) {
      velocity.set(v);
    } else {
      velocity.set(0);
    }

    velocityMph.set(velocity.getValue() * UnitConversions.MPS_TO_MPH);
  }

  /**
   * Updates acceleration.
   */
  private void updateAcceleration() {
    if (emergencyBrakeStatus.getValue() == OnOffStatus.ON) {
      acceleration = TrainData.EMERGENCY_BRAKE_ACCELERATION;
    } else if (serviceBrakeStatus.getValue() == OnOffStatus.ON) {
      acceleration = TrainData.SERVICE_BRAKE_ACCELERATION;
    } else {
      acceleration = force / mass.get();
    }
  }

  /**
   * Updates force.
   */
  private void updateForce() {
    double tempForce;
    if (velocity.get() == 0) {
      tempForce = powerCommand.get() * 1000 / 0.001;
    } else {
      tempForce = powerCommand.get() * 1000 / velocity.get();
    }

    force = tempForce;

    //    if (tempForce - frictionForce < 0) {
    //      force = 0;
    //    } else {
    //      force = tempForce - frictionForce;
    //    }
  }

  /**
   * Calculates the change in distance from the velocity and change in time.
   * @return the change in distance based on velocity of train and change in time.
   */
  private double changeInDist() {
    double time = clock.getChangeInTime() / 1000.0; // time in seconds
    return (velocity.get() * time) + (0.5 * acceleration * time * time);
  }

  /**
   * Runs simulation. This will be called from main.
   */
  void run() {

    if (isDispatched) {

      updateForce();
      updateAcceleration();
      updateVelocity();

      updatePosition();
      updateOccupancy();
      updateSpeedAuth();
//      checkBrakes();
      changeTemperature();

      //      System.out.println("Block: " + currentBlock.getSection() + currentBlock.getNumber());
      //      System.out.println("Acceleration: " + acceleration);
      //      System.out.println("Velocity: " + velocity.get());
      //      System.out.println("Force: " + force);
      //      System.out.println("Power: " + powerCommand.get());
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
    if (currentBlock != null) {
      this.controller.setTrackCircuitSignal(currentBlock.getSetPointSpeed(),
          currentBlock.getAuthority());
    }
  }

  private void updateBeacon() {
    if (currentBlock.getBeacon() != null) {
      this.controller.setBeaconSignal(currentBlock.getBeacon());
    }
  }

  /**
   * Slows train down if brakes are engaged.
   */
  private void checkBrakes() {
    double deceleration;
    if (emergencyBrakeStatus.getValue() == OnOffStatus.ON) {
      deceleration = TrainData.EMERGENCY_BRAKE_ACCELERATION * (clock.getChangeInTime() / 1000);
      velocity.set(velocity.get() + deceleration);
    } else if (serviceBrakeStatus.getValue() == OnOffStatus.ON) {
      deceleration = TrainData.SERVICE_BRAKE_ACCELERATION * (clock.getChangeInTime() / 1000);
      velocity.set(velocity.get() + deceleration);
    }
  }

  /**
   * Helper method to return true if a change in distance crosses block boarders.
   * @param distChange The distance the train moved.
   * @return true if train crosses block boarder, false otherwise.
   */
  private boolean isCrossingBlock(double distChange) {
    previousBlock.setOccupied(false);
    return ((positionInBlock + distChange) > currentBlock.getSize());
  }

  /**
   * Adds Train Model to the list.
   * @param train the TrainModel to be added.
   */
  static void add(TrainModel train) {
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
   * Opens the left doors when at a station and stopped.
   */
  private void openLeftDoors() {
    leftDoorStatus.set(DoorStatus.OPEN);
    randomPassengersLeave();
    if (currentBlock != null) {
      addPassengers(currentBlock.getPassengers(TrainData.MAX_PASSENGERS - numPassengers.get()));
    }
  }

  /**
   * Opens the right doors when at a station and stopped.
   */
  private void openRightDoors() {
    rightDoorStatus.setValue(DoorStatus.OPEN);
    randomPassengersLeave();
    if (currentBlock != null) {
      addPassengers(currentBlock.getPassengers(TrainData.MAX_PASSENGERS - numPassengers.get()));
    }
  }

  private void closeRightDoors() {
    rightDoorStatus.setValue(DoorStatus.CLOSED);
  }

  private void closeLeftDoors() {
    leftDoorStatus.setValue(DoorStatus.CLOSED);
  }

  /**
   * Called in Run() method to change temperature if needed.
   */
  private void changeTemperature() {
    if (this.acStatus.getValue() == OnOffStatus.ON) {
      coolTrain();
    } else if (this.heaterStatus.getValue() == OnOffStatus.ON) {
      heatTrain();
    }
  }

  private void coolTrain() {
    long deltaTime = clock.getChangeInTime();
    double deltaTemp = deltaTime * TrainData.TEMPERATURE_RATE_OF_CHANGE;
    currentTemp.setValue(currentTemp.getValue() - deltaTemp);
  }

  private void heatTrain() {
    long deltaTime = clock.getChangeInTime();
    double deltaTemp = deltaTime * TrainData.TEMPERATURE_RATE_OF_CHANGE;
    currentTemp.setValue(currentTemp.getValue() + deltaTemp);
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
  public void setTrackLineFailureStatus(Failure trackLineFailureStatus) {
    this.trackLineFailureStatus.set(trackLineFailureStatus);
  }

  @Override
  public void setLeftDoorStatus(DoorStatus leftDoorStatus) {
    if (leftDoorStatus.equals(DoorStatus.CLOSED)) {
      closeLeftDoors();
    } else if (leftDoorStatus.equals(DoorStatus.OPEN)) {
      openLeftDoors();
    }
  }

  @Override
  public void setRightDoorStatus(DoorStatus rightDoorStatus) {
    if (rightDoorStatus.equals(DoorStatus.CLOSED)) {
      closeRightDoors();
    } else if (rightDoorStatus.equals(DoorStatus.OPEN)) {
      openRightDoors();
    }
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

  // Will be called by TrainController to give TrainModel the power command.
  @Override
  public void setPowerCommand(double powerCommand) {
    this.powerCommand.set(powerCommand);
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
  public Failure getTrackLineFailureStatus() {
    return trackLineFailureStatus.get();
  }

  @Override
  public Failure getEngineFailureStatus() {
    return engineFailureStatus.get();
  }

  @Override
  public Failure getBrakeFailureStatus() {
    return brakeFailureStatus.get();
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

  public DoubleProperty massLbsProperty() {
    return massLbs;
  }

  public DoubleProperty velocityProperty() {
    return velocity;
  }

  public DoubleProperty velocityMphProperty() {
    return velocityMph;
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

  public IntegerProperty numberOfCarsProperty() {
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

  public ObjectProperty<Failure> trackLineFailureStatusProperty() {
    return trackLineFailureStatus;
  }

  public ObjectProperty<Failure> engineFailureStatusProperty() {
    return engineFailureStatus;
  }

  public ObjectProperty<Failure> brakeFailureStatusProperty() {
    return brakeFailureStatus;
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

  public static TrainModel getTrainModel(String id) {
    return listOfTrainModels.get(id);
  }

  public static HashMap<String, TrainModel> getTrainModels() {
    return listOfTrainModels;
  }

}

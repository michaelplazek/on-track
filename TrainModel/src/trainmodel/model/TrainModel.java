package trainmodel.model;

import java.util.HashMap;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;
import mainmenu.controller.MainMenuController;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.model.TrainControllerInterface;
import trainmodel.controller.Constants;
import utils.train.TrainData;
import utils.train.TrainModelEnums;
import utils.unitconversion.UnitConversions;



/**
 * Created by jeremyzang on 2/16/18.
 */
public class TrainModel implements TrainModelInterface {

  //Current temp inside the train.
  private DoubleProperty currentTemp = new SimpleDoubleProperty(70);
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
  private SimpleDoubleProperty height = new SimpleDoubleProperty(TrainData.HEIGHT_OF_TRAIN);
  private SimpleDoubleProperty width = new SimpleDoubleProperty(TrainData.WIDTH_OF_TRAIN);
  private SimpleDoubleProperty lengthOfTrain = new SimpleDoubleProperty(TrainData.LENGTH_OF_TRAIN);
  private SimpleDoubleProperty numberOfCars = new SimpleDoubleProperty(TrainData.NUMBER_OF_CARS);

  //String Properties to be bound with UI.
  private SimpleDoubleProperty mass = new SimpleDoubleProperty(TrainData.EMPTY_WEIGHT);
  private SimpleDoubleProperty velocity = new SimpleDoubleProperty(0); //in m/s

  //set by TrainController.
  private SimpleDoubleProperty powerCommand = new SimpleDoubleProperty(0); //In kilo Watts.
  private SimpleIntegerProperty numPassengers = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty capacity
      = new SimpleIntegerProperty(TrainData.MAX_PASSENGERS); //passenger capacity of train.

  private double acceleration = 0.0000001; //in m/s^2
  private double force = 0; //in N

  private boolean started = false;

  private ObjectProperty<TrainModelEnums.LightStatus> lightStatus
      = new SimpleObjectProperty<>(TrainModelEnums.LightStatus.OFF);
  private ObjectProperty<TrainModelEnums.DoorStatus> rightDoorStatus
      = new SimpleObjectProperty<>(TrainModelEnums.DoorStatus.CLOSED);
  private ObjectProperty<TrainModelEnums.DoorStatus> leftDoorStatus
      = new SimpleObjectProperty<>(TrainModelEnums.DoorStatus.CLOSED);
  private ObjectProperty<TrainModelEnums.AntennaStatus> antennaStatus
      = new SimpleObjectProperty<>(TrainModelEnums.AntennaStatus.CONNECTED);
  private ObjectProperty<TrainModelEnums.TrackLineStatus> trackLineStatus
      = new SimpleObjectProperty<>(TrainModelEnums.TrackLineStatus.CONNECTED);
  private ObjectProperty<TrainModelEnums.BrakeStatus> serviceBrakeStatus
      = new SimpleObjectProperty<>(TrainModelEnums.BrakeStatus.OFF);
  private ObjectProperty<TrainModelEnums.BrakeStatus> emergencyBrakeStatus
      = new SimpleObjectProperty<>(TrainModelEnums.BrakeStatus.OFF);

  private GpsLocation gpsLocation; //future development? 3/5/18

  private double positionInBlock = 0; //The number of meters from the border of the current block.
  // Measured from the previous boarder to front of train.

  private Track activeTrack;
  private Block currentBlock;
  private Block previousBlock;

  private boolean isMovingBlockMode = false;

  //Speed and Authority from MBO gets passed to TrainController in MBO mode.
//  private float mboSpeed;
//  private float mboAuthority;

  //Speed and Authority from trackModel gets passed to TrainController in Manual Mode
//  private float trackModelSpeed;
//  private float trackModelAuthority;

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
      this.capacity.set(capacity.get() - numberOfPassengers);
      this.numPassengers.set(numPassengers.get() + numberOfPassengers);
      this.mass.set(mass.get() + (TrainData.PASSENGER_WEIGHT * numberOfPassengers));
    } else {
      //If numberOfPassengers is >= available seats as the most you can.
      int passengersTotal = this.numPassengers.get() + availableSeats;
      this.numPassengers.set(passengersTotal);
      this.capacity.set(TrainData.MAX_PASSENGERS - passengersTotal); //This should be zero
      this.mass.set(TrainData.EMPTY_WEIGHT + (TrainData.PASSENGER_WEIGHT * passengersTotal));
    }
  }

  /**
   * Removes a number of passengers from the train.
   * @param numberOfPassengers The number of passengers to be removed.
   */
  public void removePassengers(int numberOfPassengers) {
    if ((this.numPassengers.get() - numberOfPassengers) >= 0) {
      this.capacity.set(capacity.get() + numberOfPassengers);
      this.numPassengers.set(numPassengers.get() - numberOfPassengers);
      this.mass.set(mass.get() - (TrainData.PASSENGER_WEIGHT * numberOfPassengers));
    } else {
      this.capacity.set(TrainData.MAX_PASSENGERS);
      this.numPassengers.set(0);
      this.mass.set(
          (TrainData.EMPTY_WEIGHT + (TrainData.MAX_PASSENGERS * TrainData.PASSENGER_WEIGHT))
          - (TrainData.MAX_PASSENGERS * Constants.passengerAvgMassKg));

    }
  }
  
  private void setAndConvertMassToImperial(Double mass) {
    this.mass.setValue(mass * UnitConversions.KGS_TO_LBS);
  }

  /**
   * This will start the movement of the train.
   */
  private void start() {
    started = true;
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
    if (crossingBlock(changeInDist)) {
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
    if (!started) {
      start();
    } else {
      updateAcceleration();
      updateVelocity();
      updateForce();
      updatePosition();
      brake();
      updateOccupancy();
    }
  }

  /**
   * Slows train down if brakes are engaged.
   */
  private void brake() {
    double deceleration = 0;
    if (emergencyBrakeStatus.toString().equals(TrainModelEnums.BrakeStatus.ON.toString())) {
      deceleration = TrainData.EMERGENCY_BRAKE_ACCELERATION * clock.getChangeInTime();
      velocity.set(velocity.get() - deceleration);
    } else if (emergencyBrakeStatus.toString().equals(TrainModelEnums.BrakeStatus.OFF.toString())
        && serviceBrakeStatus.toString().equals(TrainModelEnums.BrakeStatus.ON.toString())) {
      deceleration = TrainData.SERVICE_BRAKE_ACCELERATION * clock.getChangeInTime();
      velocity.set(velocity.get() - deceleration);
    }
  }

  /**
   * Helper method to return true if a change in distance crosses block boarders.
   * @param distChange The distance the train moved.
   * @return true if train crosses block boarder, false otherwise.
   */
  private boolean crossingBlock(Double distChange) {
    return ((positionInBlock + distChange) > currentBlock.getSize());
  }

  /**
   * Adds Train Model to the list.
   * @param train the TrainModel to be added.
   */
  protected static void addTrain(TrainModel train) {
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
   * Setters.
   */
  @Override
  public void setEmergencyBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus) {
    this.emergencyBrakeStatus.set(brakeStatus);
  }

  @Override
  public void setServiceBrakeStatus(TrainModelEnums.BrakeStatus brakeStatus) {
    this.serviceBrakeStatus.set(brakeStatus);
  }

  @Override
  public void setTrackLineStatus(TrainModelEnums.TrackLineStatus trackLineStatus) {
    this.trackLineStatus.set(trackLineStatus);
  }

  @Override
  public void setAntennaStatus(TrainModelEnums.AntennaStatus antennaStatus) {
    this.antennaStatus.set(antennaStatus);
  }

  @Override
  public void setLeftDoorStatus(TrainModelEnums.DoorStatus leftDoorStatus) {
    this.leftDoorStatus.set(leftDoorStatus);
  }

  @Override
  public void setRightDoorStatus(TrainModelEnums.DoorStatus rightDoorStatus) {
    this.rightDoorStatus.set(rightDoorStatus);
  }

  @Override
  public void setLightStatus(TrainModelEnums.LightStatus lightStatus) {
    this.lightStatus.set(lightStatus);
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

  //Will get called by MBO (or TrackModel?)
  // - to send speed and authority over antenna and pass to TrainController.
  @Override
  public void setAntennaSignal(float speed, float authority) {

    //if Manual Mode call this
    if (isMovingBlockMode) {
      this.controller.setAntennaSignal(speed, authority);
    } else {

    }

    //if MBO mode call this
    // this.controller.setAntennaSignal(mboSpeed);
  }

  @Override
  public void setBeaconSignal(Byte[] beaconSignal) {
    //When in manual mode Speed/Auth and Beacon signal comes from track model.
    //when in manual mode call this.
    this.controller.setBeaconSignal(beaconSignal);
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
  public TrainModelEnums.BrakeStatus getEmergencyBrakeStatus() {
    return emergencyBrakeStatus.get();
  }

  @Override
  public TrainModelEnums.BrakeStatus getServiceBrakeStatus() {
    return serviceBrakeStatus.get();
  }

  @Override
  public TrainModelEnums.TrackLineStatus getTrackLineStatus() {
    return trackLineStatus.get();
  }

  @Override
  public TrainModelEnums.AntennaStatus getAntennaStatus() {
    return antennaStatus.get();
  }

  @Override
  public TrainModelEnums.DoorStatus getLeftDoorStatus() {
    return leftDoorStatus.get();
  }

  @Override
  public TrainModelEnums.DoorStatus getRightDoorStatus() {
    return rightDoorStatus.get();
  }

  @Override
  public TrainModelEnums.LightStatus getLightStatus() {
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

  public Block getCurrentBlock() {
    return currentBlock;
  }

  public Block getPreviousBlock() {
    return previousBlock;
  }

  public DoubleProperty currentTempProperty() {
    return currentTemp;
  }

  public SimpleDoubleProperty massProperty() {
    return mass;
  }

  public SimpleDoubleProperty velocityProperty() {
    return velocity;
  }

  public SimpleDoubleProperty powerCommandProperty() {
    return powerCommand;
  }

  public SimpleIntegerProperty numPassengersProperty() {
    return numPassengers;
  }

  public SimpleIntegerProperty capacityProperty() {
    return capacity;
  }

  public SimpleDoubleProperty heightProperty() {
    return height;
  }

  public SimpleDoubleProperty widthProperty() {
    return width;
  }

  public SimpleDoubleProperty lengthOfTrainProperty() {
    return lengthOfTrain;
  }

  public SimpleDoubleProperty numberOfCarsProperty() {
    return numberOfCars;
  }

  public ObjectProperty<TrainModelEnums.BrakeStatus> emergencyBrakeStatusProperty() {
    return emergencyBrakeStatus;
  }

  public ObjectProperty<TrainModelEnums.LightStatus> lightStatusProperty() {
    return lightStatus;
  }

  public ObjectProperty<TrainModelEnums.DoorStatus> rightDoorStatusProperty() {
    return rightDoorStatus;
  }

  public ObjectProperty<TrainModelEnums.DoorStatus> leftDoorStatusProperty() {
    return leftDoorStatus;
  }

  public ObjectProperty<TrainModelEnums.AntennaStatus> antennaStatusProperty() {
    return antennaStatus;
  }

  public ObjectProperty<TrainModelEnums.TrackLineStatus> trackLineStatusProperty() {
    return trackLineStatus;
  }

  public ObjectProperty<TrainModelEnums.BrakeStatus> serviceBrakeStatusProperty() {
    return serviceBrakeStatus;
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

package traincontroller.model;

import java.util.HashMap;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mainmenu.Clock;
import mainmenu.ClockInterface;
import mainmenu.controller.MainMenuController;
import trainmodel.model.TrainModel;
import trainmodel.model.TrainModelFactory;
import trainmodel.model.TrainModelInterface;
import utils.train.TrainModelEnums;

public class TrainController implements TrainControllerInterface {
  private static ClockInterface clock = Clock.getInstance();

  private boolean automatic;

  private TrainModelInterface trainModel;
  private SimpleStringProperty id;
  private SimpleStringProperty line;
  private boolean running;
  private SimpleDoubleProperty currentSpeed;
  private SimpleDoubleProperty setSpeed;
  private SimpleDoubleProperty authority;
  private SimpleDoubleProperty powerCommand;
  private SimpleDoubleProperty driverSetSpeed;
  private SimpleDoubleProperty setTemperature;
  private SimpleDoubleProperty currentTemperature;
  private SimpleDoubleProperty kp;
  private SimpleDoubleProperty ki;
  private SimpleStringProperty currentStation;
  private SimpleStringProperty nextStation;

  private static HashMap<String, TrainController> listOfTrains = new HashMap<>();

  /**
   * Base constructor for TrainController.
   * @param id String for name of train
   * @param line String for line that train is running on
   */
  TrainController(String id, String line) {
    this.id = new SimpleStringProperty(id);
    this.line = new SimpleStringProperty(line);
    this.currentSpeed = new SimpleDoubleProperty(0);
    this.authority = new SimpleDoubleProperty(0);
    this.setSpeed = new SimpleDoubleProperty(0);
    this.powerCommand = new SimpleDoubleProperty(0);
    this.driverSetSpeed = new SimpleDoubleProperty(0);
    this.setTemperature = new SimpleDoubleProperty(68);
    this.currentTemperature = new SimpleDoubleProperty(68);
    this.kp = new SimpleDoubleProperty(5);
    this.ki = new SimpleDoubleProperty(5);
    this.currentStation = new SimpleStringProperty("N/A");
    this.nextStation = new SimpleStringProperty("N/A");

    this.trainModel = (TrainModel) TrainModelFactory.createTrainModel(this, id, line);
  }

  public void setAntennaSignal(Byte[] signal) {

  }

  public void setBeaconSignal(Byte[] signal) {

  }

  public void setTrackCircuitSignal(Byte[] signal) {

  }

  private String getId() {
    return id.getValue();
  }

  private void setId(String id) {
    this.id.set(id);
  }

  private String getLine() {
    return line.getValue();
  }

  private void setLine(String line) {
    this.line.set(line);
  }

  public TrainModelInterface getTrainModel() {
    return trainModel;
  }

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public SimpleDoubleProperty getAuthorityProperty() {
    return authority;
  }

  private Double getAuthority() {
    return authority.getValue();
  }

  private void setAuthority(Double authority) {
    this.authority.set(authority);
  }

  public SimpleDoubleProperty getCurrentSpeedProperty() {
    return currentSpeed;
  }

  private Double getCurrentSpeedValue() {
    return currentSpeed.getValue();
  }

  private void setCurrentSpeed(Double currentSpeed) {
    this.currentSpeed.set(currentSpeed);
  }

  public SimpleDoubleProperty getPowerCommandProperty() {
    return powerCommand;
  }

  private Double getPowerCommand() {
    return powerCommand.getValue();
  }

  private void setPowerCommand(Double powerCommand) {
    this.powerCommand.set(powerCommand);
  }

  public SimpleDoubleProperty getSetSpeedProperty() {
    return setSpeed;
  }

  private Double getSetSpeed() {
    return setSpeed.getValue();
  }

  private void setSetSpeed(Double setSpeed) {
    this.setSpeed.set(setSpeed);
  }

  public SimpleDoubleProperty getDriverSetSpeedProperty() {
    return driverSetSpeed;
  }

  private Double getDriverSetSpeed() {
    return driverSetSpeed.getValue();
  }

  private void setDriverSetSpeed(Double driverSetSpeed) {
    this.driverSetSpeed.set(driverSetSpeed);
  }

  public SimpleDoubleProperty getSetTemperatureProperty() {
    return setTemperature;
  }

  private Double getSetTemperature() {
    return setTemperature.getValue();
  }

  private void setSetTemperature(Double setTemperature) {
    this.setTemperature.set(setTemperature);
  }

  public SimpleDoubleProperty getCurrentTemperatureProperty() {
    return currentTemperature;
  }

  private Double getCurrentTemperature() {
    return currentTemperature.getValue();
  }

  private void setCurrentTemperature(Double currentTemperature) {
    this.currentTemperature.set(currentTemperature);
  }

  public SimpleDoubleProperty getKpProperty() {
    return kp;
  }

  private Double getKp() {
    return kp.getValue();
  }

  private void setKp(Double kp) {
    this.kp.set(kp);
  }

  public SimpleDoubleProperty getKiProperty() {
    return ki;
  }

  private Double getKi() {
    return ki.getValue();
  }

  private void setKi(Double ki) {
    this.ki.set(ki);
  }

  public SimpleStringProperty getCurrentStationProperty() {
    return currentStation;
  }

  private String getCurrentStation() {
    return currentStation.getValue();
  }

  private void setCurrentStation(String currentStation) {
    this.currentStation.set(currentStation);
  }

  public SimpleStringProperty getNextStationProperty() {
    return nextStation;
  }

  private String getNextStation() {
    return nextStation.getValue();
  }

  private void setNextStation(String nextStation) {
    this.nextStation.set(nextStation);
  }

  public boolean isAutomatic() {
    return automatic;
  }

  public void setAutomatic(boolean automatic) {
    this.automatic = automatic;
  }

  public void setServiceBrake(TrainModelEnums.BrakeStatus brakeStatus) {
    trainModel.setServiceBrakeStatus(brakeStatus);
  }

  public void setEmergencyBrake(TrainModelEnums.BrakeStatus brakeStatus) {
    trainModel.setEmergencyBrakeStatus(brakeStatus);
  }

  @Override
  public void activateEmergencyBrake() {
    this.setEmergencyBrake(TrainModelEnums.BrakeStatus.ON);
  }

  private void start() {
    trainModel = new TrainModel(this, id.getValue(), line.getValue());
    running = true;
  }

  /**
   * Starts a train.
   * */
  public static boolean start(String trainId) {
    TrainController temp = listOfTrains.get(trainId);
    if (temp == null) {
      return false;
    }
    temp.start();
    return true;
  }

  /**
   * Removes a train from the list.
   * */
  public static boolean delete(String trainId) {
    TrainController temp = listOfTrains.get(trainId);
    if (temp == null) {
      return false;
    }
    listOfTrains.remove(trainId);
    MainMenuController.getInstance().updateTrainControllerDropdown();
    return true;
  }

  /**
   * Updates the current train controller.
   */
  private void run() {
    if (running) {
      takeAction();
      updateFields();
    }
  }

  private void takeAction() {

  }

  private void updateFields() {
    setCurrentTemperature(trainModel.getCurrentTemp());
    setCurrentSpeed(trainModel.getCurrentSpeed());

  }

  protected static void addTrain(TrainController train) {
    listOfTrains.put(train.getId(), train);
    MainMenuController.getInstance().updateTrainControllerDropdown();
  }

  /** Run all instances of the train controller. */
  public static void runAllInstances() {
    for (String key : listOfTrains.keySet()) {
      listOfTrains.get(key).run();
    }
  }

  public static ObservableList<String> getListOfTrains() {
    return FXCollections.observableArrayList(listOfTrains.keySet());
  }

  public static HashMap<String, TrainController> getTrainControllers() {
    return listOfTrains;
  }

  public static TrainController getTrainController(String id) {
    return listOfTrains.get(id);
  }
}

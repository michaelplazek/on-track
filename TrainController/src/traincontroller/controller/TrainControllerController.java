package traincontroller.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import traincontroller.enums.Mode;
import traincontroller.model.TrainController;
import traincontroller.model.TrainControllerManager;
import utils.alerts.AlertWindow;
import utils.train.DoorStatus;
import utils.train.OnOffStatus;
import utils.unitconversion.UnitConversions;


public class TrainControllerController implements Initializable {

  @FXML
  private Button setSpeedButton;
  @FXML
  private Button setTemperatureButton;
  @FXML
  private TextField setSpeedField;
  @FXML
  private TextField setTemperatureField;

  @FXML
  private ToggleButton emergencyBrakeButton;
  @FXML
  private ToggleButton lightsButton;
  @FXML
  private ToggleButton rightDoorButton;
  @FXML
  private ToggleButton leftDoorButton;
  @FXML
  private ToggleButton serviceBrakeButton;
  @FXML
  private RadioButton automatic;
  @FXML
  private RadioButton manual;

  @FXML
  private Label currentSpeed;
  @FXML
  private Label setSpeed;
  @FXML
  private Label authority;
  @FXML
  private Label driverSetSpeed;
  @FXML
  private Label temperature;
  @FXML
  private Label setTemperature;
  @FXML
  private Label currentStation;
  @FXML
  private Label nextStation;
  @FXML
  private Label powerCommand;
  @FXML
  private Label kp;
  @FXML
  private Label ki;

  private TrainController trainController;

  public TrainControllerController(String trainId) {
    trainController = TrainControllerManager.getTrainController(trainId);
  }

  @FXML
  private void setSpeedAction(ActionEvent event) {
    try {
      double newSetSpeed = Double.parseDouble(setSpeedField.getText());
      if (newSetSpeed <= trainController.getSetSpeed()) {
        trainController.setDriverSetSpeed(newSetSpeed * UnitConversions.MPH_TO_KPH * 1000 / 3600);
      } else {

        AlertWindow alert = new AlertWindow();

        alert.setTitle("Error Submitting");
        alert.setHeader("Please Enter a Proper Value");
        alert.setContent("Please enter a value less than or equal to CTC Set Speed.");

        alert.show();
      }
      setSpeedField.setText("");
    } catch (Exception e) {
      setSpeedField.setText("");
      validNumber();
    }
  }

  @FXML
  private void setTemperatureAction(ActionEvent event) {
    try {
      double newTemperature = Double.parseDouble(setTemperatureField.getText());
      if(newTemperature >= 60 && newTemperature <= 8) {
        trainController.setSetTemperature(newTemperature);
      } else {

        AlertWindow alert = new AlertWindow();

        alert.setTitle("Error Submitting");
        alert.setHeader("Please Enter a Proper Value");
        alert.setContent("Please enter a number between 60 and 80.");

        alert.show();

      }
      setTemperatureField.setText("");
    } catch (Exception e) {
      setTemperatureField.setText("");
      validNumber();
    }
  }

  private void validNumber() {

    AlertWindow alert = new AlertWindow();

    alert.setTitle("Error Submitting");
    alert.setHeader("Please Enter a Proper Value");
    alert.setContent("Please enter a valid number.");

    alert.show();

  }

  @FXML
  private void toggleEmergencyBrakes(ActionEvent event) {
    if (!emergencyBrakeButton.isSelected()) {
      trainController.setEmergencyBrake(OnOffStatus.OFF);
    } else {
      trainController.setEmergencyBrake(OnOffStatus.ON);
    }
  }

  @FXML
  private void toggleServiceBrakes(ActionEvent event) {
    if (!serviceBrakeButton.isSelected()) {
      trainController.setServiceBrake(OnOffStatus.OFF);
      trainController.setTrackCircuitSignal(0, trainController.getAuthority());
    } else {
      trainController.setServiceBrake(OnOffStatus.ON);
      if (trainController.getMode() == Mode.NORMAL
          || trainController.getMode() == Mode.STATION_BRAKE) {
        trainController.setMode(Mode.DRIVER_BRAKE);
      }
    }
  }

  @FXML
  private void toggleLights(ActionEvent event) {
    if (!lightsButton.isSelected()) {
      trainController.setLightStatus(OnOffStatus.OFF);
    } else {
      trainController.setLightStatus(OnOffStatus.ON);
    }
  }

  @FXML
  private void toggleRightDoors(ActionEvent event) {
    if (trainController.getCurrentSpeed() != 0
        || trainController.getCurrentBlock().getStationName() == null) {
      rightDoorButton.setSelected(false);
      errorDoorOpen();
      return;
    }
    if (trainController.getTrainModel().getRightDoorStatus() == DoorStatus.OPEN) {
      trainController.setRightDoorStatus(DoorStatus.CLOSED);
    } else {
      trainController.setRightDoorStatus(DoorStatus.OPEN);
    }
  }

  @FXML
  private void toggleLeftDoors(ActionEvent event) {
    if (trainController.getCurrentSpeed() != 0
        || trainController.getCurrentBlock().getStationName() == null) {
      leftDoorButton.setSelected(false);
      errorDoorOpen();
      return;
    }
    if (!leftDoorButton.isSelected()) {
      trainController.setLeftDoorStatus(DoorStatus.CLOSED);
    } else {
      trainController.setLeftDoorStatus(DoorStatus.OPEN);
    }
  }

  private void errorDoorOpen() {
    AlertWindow alert = new AlertWindow();

    alert.setTitle("Error Submitting");
    alert.setHeader("Invalid Action");
    alert.setContent("Train must be stopped and at a station to open doors.");

    alert.show();
  }

  @FXML
  private void toggleMode(ActionEvent event) {
    if (automatic.isSelected()) {
      trainController.setAutomatic(true);
      setSpeedButton.setDisable(true);
      setSpeedField.setDisable(true);
      serviceBrakeButton.setDisable(true);
      rightDoorButton.setDisable(true);
      leftDoorButton.setDisable(true);
      lightsButton.setDisable(true);
    } else {
      trainController.setAutomatic(false);
      setSpeedButton.setDisable(false);
      setSpeedField.setDisable(false);
      trainController.setDriverSetSpeed(trainController.getSetSpeed());
      if (trainController.isRunning() && !trainController.isAutomatic()) {
        serviceBrakeButton.setDisable(false);
        rightDoorButton.setDisable(false);
        leftDoorButton.setDisable(false);
        lightsButton.setDisable(false);
      }
    }
  }

  /**
   * This function initalizes Status Labels on UI.
   */
  private void initializeStatusLabels() {
    authority.textProperty().bind(trainController.getAuthorityProperty().asString());
    powerCommand.textProperty().bindBidirectional(trainController.getPowerCommandProperty(),
        new DecimalFormat("#0.00"));
    currentSpeed.textProperty().bindBidirectional(trainController.getCurrentSpeedProperty(),
        new DecimalFormat("0.00"));
    setSpeed.textProperty().bindBidirectional(trainController.getSetSpeedProperty(),
        new DecimalFormat("#0.00"));
    driverSetSpeed.textProperty().bindBidirectional(trainController.getDriverSetSpeedProperty(),
        new DecimalFormat("#0.00"));
    setTemperature.textProperty().bindBidirectional(trainController.getSetTemperatureProperty(),
        new DecimalFormat("#0.00"));
    temperature.textProperty().bindBidirectional(trainController.getCurrentTemperatureProperty(),
        new DecimalFormat("#0.00"));
    kp.textProperty().bindBidirectional(trainController.getKpProperty(),
        new DecimalFormat("#0.00"));
    ki.textProperty().bindBidirectional(trainController.getKiProperty(),
        new DecimalFormat("#0.00"));
    currentStation.textProperty().bindBidirectional(trainController.getCurrentStationProperty());
    nextStation.textProperty().bindBidirectional(trainController.getNextStationProperty());
    serviceBrakeButton.textProperty().bind(trainController.serviceBrakeStatusProperty().asString());
    serviceBrakeButton.setSelected(trainController.getServiceBrakeStatus() == OnOffStatus.ON);
    emergencyBrakeButton.textProperty().bind(Bindings.concat("Emergency Brake ",
        trainController.emergencyBrakeStatusProperty()));
    emergencyBrakeButton.setSelected(trainController.getEmergencyBrakeStatus() == OnOffStatus.ON);
    rightDoorButton.textProperty().bind(trainController.rightDoorStatusProperty().asString());
    rightDoorButton.setSelected(trainController.getRightDoorStatus() == DoorStatus.OPEN);
    leftDoorButton.textProperty().bind(trainController.leftDoorStatusProperty().asString());
    leftDoorButton.setSelected(trainController.getLeftDoorStatus() == DoorStatus.OPEN);
  }

  private void checkRunning() {
    if (trainController.isRunning()) {
      rightDoorButton.setDisable(false);
      leftDoorButton.setDisable(false);
      emergencyBrakeButton.setDisable(false);
      serviceBrakeButton.setDisable(false);
      lightsButton.setDisable(false);
    } else {
      rightDoorButton.setDisable(true);
      leftDoorButton.setDisable(true);
      emergencyBrakeButton.setDisable(true);
      serviceBrakeButton.setDisable(true);
      lightsButton.setDisable(true);
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeStatusLabels();
    checkRunning();
  }
}

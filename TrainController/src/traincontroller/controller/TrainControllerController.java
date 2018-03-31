package traincontroller.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import traincontroller.model.TrainController;
import traincontroller.model.TrainControllerManager;
import utils.train.TrainModelEnums;

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
        trainController.setDriverSetSpeed(newSetSpeed);
      }
      setSpeedField.setText("");
    } catch (Exception e) {
      setSpeedField.setText("");
    }
  }

  @FXML
  private void setTemperatureAction(ActionEvent event) {
    try {
      double newTemperature = Double.parseDouble(setTemperatureField.getText());
      trainController.setSetTemperature(newTemperature);
      setTemperatureField.setText("");
    } catch (Exception e) {
      setTemperatureField.setText("");
    }
  }

  @FXML
  private void toggleEmergencyBrakes(ActionEvent event) {
    if (!emergencyBrakeButton.isSelected()) {
      emergencyBrakeButton.textProperty().setValue("EMERGENCY BRAKE OFF");
      trainController.setEmergencyBrake(TrainModelEnums.BrakeStatus.OFF);
    } else {
      emergencyBrakeButton.textProperty().setValue("EMERGENCY BRAKE ON");
      trainController.setEmergencyBrake(TrainModelEnums.BrakeStatus.ON);
    }
  }

  @FXML
  private void toggleServiceBrakes(ActionEvent event) {
    if (!serviceBrakeButton.isSelected()) {
      serviceBrakeButton.textProperty().setValue("OFF");
      trainController.setServiceBrake(TrainModelEnums.BrakeStatus.OFF);
    } else {
      serviceBrakeButton.textProperty().setValue("ON");
      trainController.setServiceBrake(TrainModelEnums.BrakeStatus.ON);
    }
  }

  @FXML
  private void toggleLights(ActionEvent event) {
    if (!lightsButton.isSelected()) {
      lightsButton.textProperty().setValue("OFF");
      trainController.setLightStatus(TrainModelEnums.LightStatus.OFF);
    } else {
      lightsButton.textProperty().setValue("ON");
      trainController.setLightStatus(TrainModelEnums.LightStatus.ON);
    }
  }

  @FXML
  private void toggleRightDoors(ActionEvent event) {
    if (trainController.getCurrentSpeed() != 0) {
      rightDoorButton.setSelected(false);
      return;
    }
    if (trainController.getTrainModel().getRightDoorStatus() == TrainModelEnums.DoorStatus.OPEN) {
      rightDoorButton.textProperty().setValue("CLOSE");
      trainController.setRightDoorStatus(TrainModelEnums.DoorStatus.CLOSED);
    } else {
      rightDoorButton.textProperty().setValue("OPEN");
      trainController.setRightDoorStatus(TrainModelEnums.DoorStatus.OPEN);
    }
  }

  @FXML
  private void toggleLeftDoors(ActionEvent event) {
    if (trainController.getCurrentSpeed() != 0) {
      leftDoorButton.setSelected(false);
      return;
    }
    if (!leftDoorButton.isSelected()) {
      leftDoorButton.textProperty().setValue("CLOSE");
      trainController.setLeftDoorStatus(TrainModelEnums.DoorStatus.CLOSED);
    } else {
      leftDoorButton.textProperty().setValue("OPEN");
      trainController.setLeftDoorStatus(TrainModelEnums.DoorStatus.OPEN);
    }
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
    authority.textProperty().bindBidirectional(trainController.getAuthorityProperty(),
        new DecimalFormat("#0.00"));
    currentSpeed.textProperty().bindBidirectional(trainController.getAuthorityProperty(),
        new DecimalFormat("#0.00"));
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

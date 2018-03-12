package traincontroller.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import traincontroller.model.TrainController;
import utils.TrainModelEnums;

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
  private ToggleGroup mode;

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
    trainController = TrainController.getTrainController(trainId);
  }

  @FXML
  private void setSpeedAction(ActionEvent event) {
    try {
      int newSpeed = Integer.parseInt(setSpeedField.getText());
      if (newSpeed > Integer.parseInt(setSpeed.getText())) {
        return;
      }
      driverSetSpeed.setText(Integer.toString(newSpeed));
      setSpeedField.setText("");
    } catch (Exception e) {
      setSpeedField.setText("");
    }
  }

  @FXML
  private void setTemperatureAction(ActionEvent event) {
    try {
      int newTemperature = Integer.parseInt(setTemperatureField.getText());
      setTemperature.setText(Integer.toString(newTemperature));
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

    } else {
      lightsButton.textProperty().setValue("ON");
    }
  }

  @FXML
  private void toggleRightDoors(ActionEvent event) {
    if (Integer.parseInt(currentSpeed.getText()) != 0) {
      rightDoorButton.setSelected(false);
      return;
    }
    if (!rightDoorButton.isSelected()) {
      rightDoorButton.textProperty().setValue("CLOSE");
      trainController.getTrainModel().setRightDoorStatus(TrainModelEnums.DoorStatus.CLOSED);
    } else {
      rightDoorButton.textProperty().setValue("OPEN");
      trainController.getTrainModel().setRightDoorStatus(TrainModelEnums.DoorStatus.OPEN);
    }
  }

  @FXML
  private void toggleLeftDoors(ActionEvent event) {
    if (Integer.parseInt(currentSpeed.getText()) != 0) {
      leftDoorButton.setSelected(false);
      return;
    }
    if (!leftDoorButton.isSelected()) {
      leftDoorButton.textProperty().setValue("CLOSE");
      trainController.getTrainModel().setLeftDoorStatus(TrainModelEnums.DoorStatus.CLOSED);
    } else {
      leftDoorButton.textProperty().setValue("OPEN");
      trainController.getTrainModel().setLeftDoorStatus(TrainModelEnums.DoorStatus.OPEN);
    }
  }

  @FXML
  private void toggleMode(ActionEvent event) {
    ObservableMap<Object, Object> map = mode.getProperties();
    for(Object k : map.keySet()) {
      System.out.println(k + ":" + map.get(k));
    }
  }

  /**
   * This function initalizes Status Labels on UI.
   */
  private void initializeStatusLabels() {
    currentStation.textProperty().setValue("N/A");
    nextStation.textProperty().setValue("N/A");
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeStatusLabels();
  }
}

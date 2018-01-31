package trainmodel.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.shape.Circle;
import trainmodel.controller.Constants;


public class TrainModelController implements Initializable {

  //Main Buttons
  @FXML
  private Button emergencyBrakeButton;
  @FXML
  private Button startButton;
  @FXML
  private Button endButton;

  //Status Icons
  @FXML
  private Circle engineFailureStatusIcon;
  @FXML
  private Circle brakeFailureStatusIcon;
  @FXML
  private Circle signalFailureStatusIcon;

  //CheckBoxes
  @FXML
  private Label brakeFailureStatus;
  @FXML
  private Label engineFailureStatus;
  @FXML
  private Label signalFailureStatus;


  //Velocity Group
  @FXML
  private Label setSpeedStatus;
  @FXML
  private Label currentSpeedStatus;
  @FXML
  private Label setAuthorityStatus;
  @FXML
  private Label powerOutputStatus;
  @FXML
  private Label emergencyBrakeStatus;
  @FXML
  private Label serviceStatus;
  @FXML
  private Label currentBlockStatus;
  @FXML
  private Label currentTrackStatus;

  //Train Spec Group
  @FXML
  private Label weight;
  @FXML
  private Label length;
  @FXML
  private Label width;
  @FXML
  private Label height;
  @FXML
  private Label capacity;
  @FXML
  private Label numberOfCars;

  //Station Group
  @FXML
  private Label numberOfPassengers;
  @FXML
  private Label nextStation;
  @FXML
  private Label time;
  @FXML
  private Label stationStatus;

  //Operation Group
  @FXML
  private Label leftDoorStatus;
  @FXML
  private Label rightDoorStatus;
  @FXML
  private Label lightStatus;
  @FXML
  private Label beaconStatus;
  @FXML
  private Label gpsAntenaStatus;
  @FXML
  private Label mboAntenaStatus;
  @FXML
  private Label cabinTemp;


  @FXML
  private void emergency_Brake_Engaged() {
    emergencyBrakeStatus.textProperty().setValue("ENGAGED");
    //Also change Brake failure image to a red light.
  }

  @FXML
  private void end_failure_mode() {
    System.out.println("end failure called");
    if (emergencyBrakeStatus.textProperty().getValue().equals("ENGAGED")) {
      emergencyBrakeStatus.textProperty().setValue("NOT ENGAGED");
      System.out.println("end failure if statement called");
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeStatusLabels();
  }

  /**
   * This function initalizes Status Labels on UI.
   */
  public void initializeStatusLabels() {
    //Initialize status labels. If connection fails use default ("N/A").
    //TODO: Initialize labels with real data
    weight.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    setSpeedStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    currentSpeedStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    setAuthorityStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    powerOutputStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    serviceStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    currentBlockStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    currentTrackStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    numberOfPassengers.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    nextStation.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    time.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    stationStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    leftDoorStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    rightDoorStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    lightStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    beaconStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    gpsAntenaStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    mboAntenaStatus.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    cabinTemp.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
    emergencyBrakeStatus.textProperty().setValue(Constants.LABEL_EMERGENCY_BRAKE_NOT_ENGAGED);
  }
}

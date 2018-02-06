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
    weight.textProperty().setValue("56,234 lbs");
    setSpeedStatus.textProperty().setValue("45 MPH");
    currentSpeedStatus.textProperty().setValue("43 MPH");
    setAuthorityStatus.textProperty().setValue("1000 yds");
    powerOutputStatus.textProperty().setValue("24,596 Watts");
    serviceStatus.textProperty().setValue("OK");
    currentBlockStatus.textProperty().setValue("WAITING");
    currentTrackStatus.textProperty().setValue("OK");
    numberOfPassengers.textProperty().setValue("63");
    nextStation.textProperty().setValue("Downtown");
    time.textProperty().setValue("12:45 PM");
    stationStatus.textProperty().setValue("IN ROUTE");
    leftDoorStatus.textProperty().setValue("CLOSED");
    rightDoorStatus.textProperty().setValue("CLOSED");
    lightStatus.textProperty().setValue("OFF");
    beaconStatus.textProperty().setValue("ON");
    gpsAntenaStatus.textProperty().setValue("ON");
    mboAntenaStatus.textProperty().setValue("ON");
    cabinTemp.textProperty().setValue("72 degrees");
    emergencyBrakeStatus.textProperty().setValue("ENGAGED");
    length.textProperty().setValue("105.64 ft");
    width.textProperty().setValue("8.69 ft");
    height.textProperty().set("11.22 ft");
    capacity.textProperty().setValue("148"); //148 capacity
    numberOfCars.textProperty().setValue("1");
  }
}

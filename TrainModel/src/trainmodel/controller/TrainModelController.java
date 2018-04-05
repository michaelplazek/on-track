package trainmodel.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import mainmenu.Clock;
import traincontroller.model.TrainControllerInterface;
import trainmodel.model.TrainModel;
import utils.general.Constants;
import utils.train.TrainModelEnums.DoorStatus;
import utils.train.TrainModelEnums.OnOffStatus;


public class TrainModelController implements Initializable {

  //Demo Buttons
  @FXML
  private Button demoButton;
  @FXML
  private Button toggleLights;
  @FXML
  private Button toggleLeftDoor;
  @FXML
  private Button toggleRightDoor;
  @FXML
  private Button addPassenger;
  @FXML
  private Button removePassenger;


  //Main Buttons
  @FXML
  private Button emergencyBrakeButton;
  @FXML
  private Button startButton;
  @FXML
  private Button endButton;
  @FXML
  private MenuButton failures;

  @FXML
  private CheckMenuItem engineFailure;
  @FXML
  private CheckMenuItem brakeFailure;
  @FXML
  private CheckMenuItem signalFailure;

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
  private Label serviceBrakeStatus;
  @FXML
  private Label currentBlockStatus;
  @FXML
  private Label currentTrack;

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
  private Label heaterStatus;
  @FXML
  private Label acStatus;

  /**
   * Train model && controller associated with UI (use for testing as of 3/11/18).
   */
  private TrainModel trainModel;
  private TrainControllerInterface trainController;

  /**
   * Constructs a TrainModelController instance.
   * @param trainId the ID of the train.
   */
  public TrainModelController(String trainId) {
    trainModel = TrainModel.getTrainModel(trainId);
    trainController = trainModel.getController();
    trainModel.setController(trainController);
  }

  @FXML
  private void toggleSelectedFailures(ActionEvent event) {
    Button btn = (Button) event.getSource();

    for (MenuItem item : failures.getItems()) {
      if (CheckMenuItem.class.isInstance(item) && CheckMenuItem.class.cast(item).isSelected()) {
        if (item.getId().equals(brakeFailure.getId())) {
          if (btn.getId().equals(startButton.getId())) {
            startBrakeFailure();
          } else {
            endBrakeFailure();
          }
        } else if (item.getId().equals(engineFailure.getId())) {
          if (btn.getId().equals(startButton.getId())) {
            startEngineFailure();
          } else {
            endEngineFailure();
          }
        } else if (item.getId().equals(signalFailure.getId())) {
          if (btn.getId().equals(startButton.getId())) {
            startSignalFailure();
          } else {
            endSignalFailure();
          }
        }
      }
    }


  }

  @FXML
  private void emergency_Brake_Engaged() {
    if (emergencyBrakeStatus.textProperty().getValue().equals(Constants.ON)) {
      trainModel.setEmergencyBrakeStatus(OnOffStatus.OFF);
    } else {
      trainModel.setEmergencyBrakeStatus(OnOffStatus.ON);
    }

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
    initializeStatusIcons();
    initializeButtonHandlers();
  }

  /**
   * This function initalizes Status Labels on UI.
   */
  private void initializeStatusLabels() {
    //Initialize status labels. If connection fails use default ("N/A").
    //TODO: Initialize labels with real data
    StringConverter<Number> numberStringConverter = new NumberStringConverter();
    NumberFormat formatter = new DecimalFormat("#0.00");
    Bindings.bindBidirectional(numberOfPassengers.textProperty(),
        trainModel.numPassengersProperty(), numberStringConverter);
    Bindings.bindBidirectional(weight.textProperty(),
        trainModel.massProperty(), formatter);
    Bindings.bindBidirectional(powerOutputStatus.textProperty(),
        trainModel.powerCommandProperty(), formatter);
    Bindings.bindBidirectional(currentSpeedStatus.textProperty(),
        trainModel.velocityProperty(), formatter);
    Bindings.bindBidirectional(cabinTemp.textProperty(),
        trainModel.currentTempProperty(), formatter);
    Bindings.bindBidirectional(length.textProperty(),
        trainModel.lengthOfTrainProperty(), formatter);
    Bindings.bindBidirectional(width.textProperty(),
        trainModel.widthProperty(), formatter);
    Bindings.bindBidirectional(height.textProperty(),
        trainModel.heightProperty(), formatter);
    Bindings.bindBidirectional(numberOfCars.textProperty(),
        trainModel.numberOfCarsProperty(), formatter);

    currentTrack.textProperty().bind(trainModel.activeTrackProperty());
    currentBlockStatus.textProperty().bind(trainModel.currentBlockProperty());
    capacity.setText(String.valueOf(trainModel.getCapacityOfTrain()));


    setSpeedStatus.textProperty().setValue("45"); //Will be received from TrainController
    setAuthorityStatus.textProperty().setValue("1000");
    //currentTrack.textProperty().set(trainModel.getActiveTrack().getLine());
    nextStation.textProperty().setValue("Downtown");
    stationStatus.textProperty().setValue("IN ROUTE");

    time.textProperty().setValue(Clock.getInstance().getFormattedTime());
    beaconStatus.textProperty().bind(trainModel.trackLineStatusProperty().asString());
    gpsAntenaStatus.textProperty().bind(trainModel.antennaStatusProperty().asString());
    leftDoorStatus.textProperty().bind(trainModel.leftDoorStatusProperty().asString());
    rightDoorStatus.textProperty().bind(trainModel.rightDoorStatusProperty().asString());
    lightStatus.textProperty().bind(trainModel.lightStatusProperty().asString());
    mboAntenaStatus.textProperty().bind(trainModel.antennaStatusProperty().asString());
    emergencyBrakeStatus.textProperty().bind(trainModel.emergencyBrakeStatusProperty().asString());
    serviceBrakeStatus.textProperty().bind(trainModel.serviceBrakeStatusProperty().asString());
    acStatus.textProperty().bind(trainModel.acStatusProperty().asString());
    heaterStatus.textProperty().bind(trainModel.heaterStatusProperty().asString());

  }


  @FXML
  private void demoClicked() throws InterruptedException {
    runDemo();
  }


  @FXML
  private void toggleLights() {
    if (lightStatus.getText().equals(OnOffStatus.ON.toString())) {
      trainModel.lightStatusProperty().set(OnOffStatus.OFF);
    } else {
      trainModel.lightStatusProperty().set(OnOffStatus.ON);
    }
  }

  @FXML
  private void toggleLeftDoor() {
    if (leftDoorStatus.getText().equals(DoorStatus.OPEN.toString())) {
      trainModel.leftDoorStatusProperty().set(DoorStatus.CLOSED);
    } else {
      trainModel.leftDoorStatusProperty().set(DoorStatus.OPEN);
    }
  }

  @FXML
  private void toggleRightDoor() {
    if (rightDoorStatus.getText().equals(DoorStatus.OPEN.toString())) {
      trainModel.rightDoorStatusProperty().set(DoorStatus.CLOSED);
    } else {
      trainModel.rightDoorStatusProperty().set(DoorStatus.OPEN);
    }
  }

  @FXML
  private void addPassenger() {
    this.trainModel.addPassengers(1);
  }

  @FXML
  private void removePassenger() {
    this.trainModel.removePassengers(1);
  }

  private void runDemo() throws InterruptedException {
    trainModel.run();
  }

  /**
   * Initializes Status icons for failure modes.
   */
  private void initializeStatusIcons() {
    engineFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    brakeFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    signalFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
  }

  private void initializeButtonHandlers() {
    startButton.setOnAction(this::toggleSelectedFailures);
    endButton.setOnAction(this::toggleSelectedFailures);
  }

  private void startEngineFailure() {
    engineFailureStatusIcon.setFill(Paint.valueOf(Constants.RED));
    trainModel.setEmergencyBrakeStatus(OnOffStatus.ON);
    trainModel.cutEnginePower();
  }

  private void startBrakeFailure() {
    brakeFailureStatusIcon.setFill(Paint.valueOf(Constants.RED));
    trainModel.setEmergencyBrakeStatus(OnOffStatus.ON);
    trainModel.cutEnginePower();
  }

  private void startSignalFailure() {
    signalFailureStatusIcon.setFill(Paint.valueOf(Constants.RED));
    trainModel.setEmergencyBrakeStatus(OnOffStatus.ON);
    trainModel.cutEnginePower();
  }

  private void endEngineFailure() {
    engineFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    trainModel.setEmergencyBrakeStatus(OnOffStatus.OFF);
  }

  private void endBrakeFailure() {
    brakeFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    trainModel.setEmergencyBrakeStatus(OnOffStatus.OFF);
  }

  private void endSignalFailure() {
    signalFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    trainModel.setEmergencyBrakeStatus(OnOffStatus.OFF);
  }

}
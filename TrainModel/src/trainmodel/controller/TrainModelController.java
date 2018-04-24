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
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import mainmenu.Clock;
import traincontroller.model.TrainControllerInterface;
import trainmodel.model.TrainModel;
import utils.general.Constants;
import utils.train.DoorStatus;
import utils.train.Failure;
import utils.train.OnOffStatus;
import utils.train.TrainData;


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
  @FXML
  private Button demoHeatTrain;
  @FXML
  private Button demoAds;
  //Demo Pane
  @FXML
  private TitledPane demoPane;


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

  //Status Labels
  @FXML
  private Label brakeFailureStatus;
  @FXML
  private Label engineFailureStatus;
  @FXML
  private Label signalFailureStatus;


  //Velocity Group
  @FXML
  private Label currentSpeedStatus;
  @FXML
  private Label powerOutputStatus;
  @FXML
  private Label emergencyBrakeStatus;
  @FXML
  private Label serviceBrakeStatus;

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
  private Label time;
  @FXML
  private Label currentBlockStatus;
  @FXML
  private Label currentTrack;

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
  private Label cabinTemp;
  @FXML
  private Label heaterStatus;
  @FXML
  private Label acStatus;
  @FXML
  private Label degrees;
  @FXML
  private Label adLabel;

  @FXML
  private TitledPane trainSpec;
  @FXML
  private TitledPane velocityPane;
  @FXML
  private TitledPane stationPane;
  @FXML
  private TitledPane operationsPane;
  @FXML
  private AnchorPane mainAnchorPane;
  @FXML
  private TitledPane failuresPane;




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
  private void emergency_Brake_Engaged(ActionEvent event) {
    if (emergencyBrakeStatus.textProperty().getValue().equals(Constants.ON)) {
      trainModel.setEmergencyBrakeStatus(OnOffStatus.OFF);
    } else {
      trainModel.setEmergencyBrakeStatus(OnOffStatus.ON);
    }

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeStatusLabels();
    initializeStatusIcons();
    initializeButtonHandlers();
    initializeWindowSize();
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
        trainModel.massLbsProperty(), formatter);
    Bindings.bindBidirectional(powerOutputStatus.textProperty(),
        trainModel.powerCommandProperty(), formatter);
    Bindings.bindBidirectional(currentSpeedStatus.textProperty(),
        trainModel.velocityMphProperty(), formatter);
    Bindings.bindBidirectional(cabinTemp.textProperty(),
        trainModel.currentTempProperty(), formatter);
    Bindings.bindBidirectional(length.textProperty(),
        trainModel.lengthOfTrainProperty(), formatter);
    Bindings.bindBidirectional(width.textProperty(),
        trainModel.widthProperty(), formatter);
    Bindings.bindBidirectional(height.textProperty(),
        trainModel.heightProperty(), formatter);
    Bindings.bindBidirectional(numberOfCars.textProperty(),
        trainModel.numberOfCarsProperty(), numberStringConverter);
    Bindings.bindBidirectional(adLabel.textProperty(), trainModel.advertisementProperty());

    capacity.setText(String.valueOf(trainModel.getCapacityOfTrain()));
    degrees.setText(Constants.DEGREES);


    time.textProperty().setValue(Clock.getInstance().getFormattedTime());
    beaconStatus.textProperty().bind(trainModel.trackLineFailureStatusProperty().asString());
    leftDoorStatus.textProperty().bind(trainModel.leftDoorStatusProperty().asString());
    rightDoorStatus.textProperty().bind(trainModel.rightDoorStatusProperty().asString());
    lightStatus.textProperty().bind(trainModel.lightStatusProperty().asString());
    emergencyBrakeStatus.textProperty().bind(trainModel.emergencyBrakeStatusProperty().asString());
    serviceBrakeStatus.textProperty().bind(trainModel.serviceBrakeStatusProperty().asString());
    acStatus.textProperty().bind(trainModel.acStatusProperty().asString());
    heaterStatus.textProperty().bind(trainModel.heaterStatusProperty().asString());
    currentBlockStatus.textProperty().bind(trainModel.currentBlockProperty());
    currentTrack.textProperty().bind(trainModel.activeTrackProperty());

    demoPane.setVisible(false);
  }

  private void initializeWindowSize() {
    double width = trainSpec.getWidth() + velocityPane.getWidth()
        + operationsPane.getWidth() + stationPane.getWidth();
    double height = failuresPane.getHeight() + trainSpec.getHeight();
    mainAnchorPane.setPrefSize(width, height);
  }

  @FXML
  private void toggleLights(ActionEvent event) {
    if (lightStatus.getText().equals(OnOffStatus.ON.toString())) {
      trainModel.lightStatusProperty().set(OnOffStatus.OFF);
    } else {
      trainModel.lightStatusProperty().set(OnOffStatus.ON);
    }
  }

  @FXML
  private void toggleRightDoor(ActionEvent event) {
    if (rightDoorStatus.getText().equals(DoorStatus.OPEN.toString())) {
      trainModel.rightDoorStatusProperty().set(DoorStatus.CLOSED);
    } else {
      trainModel.rightDoorStatusProperty().set(DoorStatus.OPEN);
    }
  }

  @FXML
  private void toggleLeftDoor(ActionEvent event) {
    if (leftDoorStatus.getText().equals(DoorStatus.OPEN.toString())) {
      trainModel.leftDoorStatusProperty().set(DoorStatus.CLOSED);
    } else {
      trainModel.leftDoorStatusProperty().set(DoorStatus.OPEN);
    }
  }

  @FXML
  private void addPassenger(ActionEvent event) {
    this.trainModel.addPassengers(1);
  }

  @FXML
  private void removePassenger(ActionEvent event) {
    this.trainModel.removePassengers(1);
  }

  private void runDemo() throws InterruptedException {

  }

  @FXML
  private void changeAd(ActionEvent event){
    if (this.adLabel.getText().equals(TrainData.advertisements.get(0))) {
      adLabel.setText(TrainData.advertisements.get(1));
    } else if (this.adLabel.getText().equals(TrainData.advertisements.get(1))) {
      adLabel.setText(TrainData.advertisements.get(2));
    }
    else if (this.adLabel.getText().equals(TrainData.advertisements.get(2))) {
      adLabel.setText(TrainData.advertisements.get(3));
    }
    else if (this.adLabel.getText().equals(TrainData.advertisements.get(3))) {
      adLabel.setText(TrainData.advertisements.get(1));
    }
  }

  private void signalLastStop(ActionEvent event){
    adLabel.setText(TrainData.advertisements.get(0));
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
    toggleLeftDoor.setOnAction(this::toggleLeftDoor);
    toggleRightDoor.setOnAction(this::toggleRightDoor);
    toggleLights.setOnAction(this::toggleLights);
    addPassenger.setOnAction(this::addPassenger);
    removePassenger.setOnAction(this::removePassenger);
    emergencyBrakeButton.setOnAction(this::emergency_Brake_Engaged);

    demoAds.setOnAction(this::changeAd);
    demoButton.setOnAction(this::signalLastStop);
  }

  private void startEngineFailure() {
    engineFailureStatusIcon.setFill(Paint.valueOf(Constants.RED));
    trainModel.engineFailureStatusProperty().set(Failure.FAILED);
  }

  private void startBrakeFailure() {
    brakeFailureStatusIcon.setFill(Paint.valueOf(Constants.RED));
    trainModel.brakeFailureStatusProperty().set(Failure.FAILED);
    trainModel.serviceBrakeStatusProperty().set(OnOffStatus.FAILED);
  }

  private void startSignalFailure() {
    signalFailureStatusIcon.setFill(Paint.valueOf(Constants.RED));
    trainModel.trackLineFailureStatusProperty().set(Failure.FAILED);
  }

  private void endEngineFailure() {
    engineFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    trainModel.engineFailureStatusProperty().set(Failure.WORKING);
  }

  private void endBrakeFailure() {
    brakeFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    trainModel.brakeFailureStatusProperty().set(Failure.WORKING);
    trainModel.serviceBrakeStatusProperty().set(OnOffStatus.ON);
  }

  private void endSignalFailure() {
    signalFailureStatusIcon.setFill(Paint.valueOf(Constants.GREEN));
    trainModel.trackLineFailureStatusProperty().set(Failure.WORKING);
  }
}

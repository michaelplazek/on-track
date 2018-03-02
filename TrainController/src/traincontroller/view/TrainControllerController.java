package traincontroller.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

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
  private ToggleButton ligtsButton;
  @FXML
  private ToggleButton rightDoorButton;
  @FXML
  private ToggleButton leftDoorButton;
  @FXML
  private ToggleButton serviceBrakeButton;

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

  private static TrainControllerController tcc;

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
    } else {
      emergencyBrakeButton.textProperty().setValue("EMERGENCY BRAKE ON");
    }
  }

  @FXML
  private void toggleServiceBrakes(ActionEvent event) {
    if (!serviceBrakeButton.isSelected()) {
      serviceBrakeButton.textProperty().setValue("OFF");
    } else {
      serviceBrakeButton.textProperty().setValue("ON");
    }
  }

  @FXML
  private void toggleLights(ActionEvent event) {
    if (!ligtsButton.isSelected()) {
      ligtsButton.textProperty().setValue("OFF");
    } else {
      ligtsButton.textProperty().setValue("ON");
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
    } else {
      rightDoorButton.textProperty().setValue("OPEN");
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
    } else {
      leftDoorButton.textProperty().setValue("OPEN");
    }
  }

  /**
   * This function initalizes Status Labels on UI.
   */
  public void initializeStatusLabels() {
    currentSpeed.textProperty().setValue("0");
    setSpeed.textProperty().setValue("50");
    authority.textProperty().setValue("1000");
    driverSetSpeed.textProperty().setValue("0");
    temperature.textProperty().setValue("68");
    setTemperature.textProperty().setValue("68");
    currentStation.textProperty().setValue("Mt. Lebanon");
    nextStation.textProperty().setValue("Greenview");
    powerCommand.textProperty().setValue("0");
    kp.textProperty().setValue("5");
    ki.textProperty().setValue("5");
  }

  /** Initializes the event handlers needed for TrainController. */
  public void initializeEventHandlers() {
    emergencyBrakeButton.setOnAction(this::toggleEmergencyBrakes);
    leftDoorButton.setOnAction(this::toggleLeftDoors);
    rightDoorButton.setOnAction(this::toggleRightDoors);
    ligtsButton.setOnAction(this::toggleLights);
    serviceBrakeButton.setOnAction(this::toggleServiceBrakes);
    setSpeedButton.setOnAction(this::setSpeedAction);
    setTemperatureButton.setOnAction(this::setTemperatureAction);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeStatusLabels();
    initializeEventHandlers();
  }

  public TrainControllerController() {
    tcc = this;
  }
}

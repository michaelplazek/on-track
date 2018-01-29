package trainmodel.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.shape.Circle;
import trainmodel.controller.Constants;

import java.net.URL;
import java.util.ResourceBundle;

public class TrainModelUIController implements Initializable{

    //Main Buttons
    @FXML
    private Button emergencyBrake_Button;
    @FXML
    private Button start_Button;
    @FXML
    private Button end_Button;

    //Status Icons
    @FXML
    private Circle engineFailure_StatusIcon;
    @FXML
    private Circle brakeFailure_StatusIcon;
    @FXML
    private Circle signalFailure_StatusIcon;

    //CheckBoxes
    @FXML
    private Label brakeFailure_Status;
    @FXML
    private Label engineFailure_Status;
    @FXML
    private Label signalFailure_Status;


    //Velocity Group
    @FXML
    private Label setSpeed_Status;
    @FXML
    private Label currentSpeed_Status;
    @FXML
    private Label setAuthority_Status;
    @FXML
    private Label powerOutput_Status;
    @FXML
    private Label emergencyBrake_Status;
    @FXML
    private Label service_Status;
    @FXML
    private Label currentBlock_Status;
    @FXML
    private Label currentTrack_Status;

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
    private Label station_Status;

    //Operation Group
    @FXML
    private Label leftDoor_Status;
    @FXML
    private Label rightDoor_Status;
    @FXML
    private Label light_Status;
    @FXML
    private Label beacon_Status;
    @FXML
    private Label GPSAntena_Status;
    @FXML
    private Label MBOAntena_Status;
    @FXML
    private Label cabinTemp;



    @FXML
    private void emergency_Brake_Engaged(){
        emergencyBrake_Status.textProperty().setValue("ENGAGED");
        //Also change Brake failure image to a red light.
    }

    @FXML
    private void end_failure_mode(){
        System.out.println("end failure called");
        if (emergencyBrake_Status.textProperty().getValue().equals("ENGAGED")){
            emergencyBrake_Status.textProperty().setValue("NOT ENGAGED");
            System.out.println("end failure if statement called");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeStatusLabels();
    }

    public void initializeStatusLabels(){
        //Initialize status labels. If connection fails use default ("N/A").
        //TODO: Initialize labels with real data
        weight.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        setSpeed_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        currentSpeed_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        setAuthority_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        powerOutput_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        service_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        currentBlock_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        currentTrack_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        numberOfPassengers.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        nextStation.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        time.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        station_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        leftDoor_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        rightDoor_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        light_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        beacon_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        GPSAntena_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        MBOAntena_Status.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        cabinTemp.textProperty().setValue(Constants.LABEL_STATUS_UNAVAILABLE);
        emergencyBrake_Status.textProperty().setValue(Constants.LABEL_EMERGENCY_BRAKE_NOT_ENGAGED);
    }
}

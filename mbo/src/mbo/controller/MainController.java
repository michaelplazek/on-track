package mbo.controller;

import javafx.scene.paint.Paint;
import mbo.model.MovingBlockOverlay;
import mbo.model.DriverScheduleItem;
import mbo.model.TrainScheduleItem;
import mbo.model.TrainInfoItem;
import mbo.controller.Constants;

import java.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;


public class MainController {
  boolean clicked = false;
  boolean mode = false;

  private MovingBlockOverlay mbo = MovingBlockOverlay.getInstance();

  @FXML
  private Label scheduleName;
  @FXML
  private Label desireThroughput;
  @FXML
  private Button generateSchedule;
  @FXML
  private Button exportSchedule;
  @FXML
  private TextField setScheduleName;
  @FXML
  private TextField setDesiredThroughput;
  @FXML
  private Circle mboEnabled;

  @FXML
  private TableView<TrainInfoItem> trainInfoTable;
  @FXML
  private TableColumn<TrainInfoItem, String> trainID;
  @FXML
  private TableColumn<TrainInfoItem, String> line;
  @FXML
  private TableColumn<TrainInfoItem, String> coordinates;
  @FXML
  private TableColumn<TrainInfoItem, String> passengerCount;
  @FXML
  private TableColumn<TrainInfoItem, String> velocity;
  @FXML
  private TableColumn<TrainInfoItem, String> authority;
  @FXML
  private TableColumn<TrainInfoItem, String> safeBrakeDist;


  @FXML
  private TableView<DriverScheduleItem> schedulerDriverSchedule;
  @FXML
  private TableColumn<DriverScheduleItem, String> schedDriver;
  @FXML
  private TableColumn<DriverScheduleItem, String> schedDriverTrainID;
  @FXML
  private TableColumn<DriverScheduleItem, String> schedShiftStart;
  @FXML
  private TableColumn<DriverScheduleItem, String> schedBreakStart;
  @FXML
  private TableColumn<DriverScheduleItem, String> schedShiftEnd;
  @FXML
  private TableColumn<DriverScheduleItem, String> schedBreakEnd;

  @FXML
  private TableView<TrainScheduleItem> schedulerTrainSchedule;
  @FXML
  private TableColumn<TrainScheduleItem, String> schedTrainID;
  @FXML
  private TableColumn<TrainScheduleItem, String> schedLine;
  @FXML
  private TableColumn<TrainScheduleItem, String> schedStation;
  @FXML
  private TableColumn<TrainScheduleItem, String> schedTotalTime;
  @FXML
  private TableColumn<TrainScheduleItem, String> schedArrival;

  public void initialize() {
    enableMBO(mode);
    connect();
  }

  // change light to green when MBO mode is enabled from CTC
  private void enableMBO(boolean mode){
    Circle mboEnabled = new Circle();
    if (mode)
      mboEnabled.setFill(Paint.valueOf(Constants.GREEN));
    else
      mboEnabled.setFill(Paint.valueOf(Constants.RED));
  }

  private void connect() {
    connectTables();
    connectButtons();
  }

  private void connectTables() {
  }

  // connect buttons to event handler
  private void connectButtons() {
    generateSchedule.setOnAction(this::buttonPressed);
    exportSchedule.setOnAction(this::buttonPressed);
  }

  // buttonPressed event handler
  private void buttonPressed(ActionEvent event) {
    Button b = (Button) event.getSource();
    switch (b.getId()) {
      case "generateSchedule":
        generateScheduleAction();
        break;
      case "exportSchedule":
        exportScheduleAction();
        break;
      default:
        break;
    }
  }


  // if TextField empty, disable generate schedule button
  private void generateScheduleAction() {
    TextField setScheduleName = new TextField();
    TextField setDesiredThroughput = new TextField();
    if (setScheduleName.getText().trim().isEmpty() || setDesiredThroughput.getText().trim().isEmpty())
      generateSchedule.setDisable(true);
    else if (setScheduleName.getText().trim().isEmpty() == false && setDesiredThroughput.getText().trim().isEmpty() == false) {
      generateSchedule.setDisable(false);
      setScheduleName.setOnAction(e -> clicked = true);
    }
  }

  // if generateSchedule button has been clicked, enable exportSchedule button
  private void exportScheduleAction() {
    if (!clicked)
      exportSchedule.setDisable(true);
    else
      exportSchedule.setDisable(false);
  }



}





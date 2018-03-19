package mbo.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import mbo.model.TrainScheduleItem;
import mbo.model.TrainInfoItem;
import mbo.model.DriverScheduleItem;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import mbo.controller.Constants;
import javafx.beans.binding.Bindings;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.*;

import mainmenu.Clock;
import mbo.controller.Constants;
import mbo.model.MovingBlockOverlayInterface;


public class MovingBlockOverlayController implements Initializable {

  String name;
  boolean mboMode;

  private ObservableList<TrainInfoItem> trainData = FXCollections.observableArrayList();
  private ObservableList<TrainScheduleItem> trainSchedule = FXCollections.observableArrayList();
  private ObservableList<DriverScheduleItem> driverSchedule = FXCollections.observableArrayList();

  // main buttons
  @FXML
  private Button generateSchedule;
  @FXML
  private Button exportSchedule;

  // mode status indicator
  @FXML
  private Circle mboModeEnabled;

  @FXML
  private TextField setScheduleName;
  @FXML
  private TextField setDesiredThroughput;

  @FXML
  private TableView schedulerDriverSchedule;
  @FXML
  private TableView schedulerTrainSchedule;
  @FXML
  private TableView trainInfoTable;

  @FXML
  private TableColumn trainID;
  @FXML
  private TableColumn line;
  @FXML
  private TableColumn coordinates;
  @FXML
  private TableColumn passengerCount;
  @FXML
  private TableColumn velocity;
  @FXML
  private TableColumn authority;
  @FXML
  private TableColumn safeBrakeDist;

  @FXML
  private TableColumn schedTrainID;
  @FXML
  private TableColumn schedLine;
  @FXML
  private TableColumn schedStation;
  @FXML
  private TableColumn schedTotalTime;
  @FXML
  private TableColumn schedArrival;

  @FXML
  private TableColumn schedDriver;
  @FXML
  private TableColumn schedDriverTrainID;
  @FXML
  private TableColumn schedShiftStart;
  @FXML
  private TableColumn schedBreakStart;
  @FXML
  private TableColumn schedShiftEnd;
  @FXML
  private TableColumn schedBreakEnd;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setMBOMode(mboMode);
    initializeStatus();
    initializeTrainInfo();
    connectButtons();

    generateSchedule.setOnAction(event -> makeSchedule());

    exportSchedule.setOnAction(event -> exportCSV(name));
  }

  // initialize values upon UI start
  private void initializeStatus(){
    mboModeEnabled.setFill(Paint.valueOf(Constants.GREEN));
  }

  private void setMBOMode(boolean mboMode) { this.mboMode = mboMode; }

  private void initializeTrainInfo(){

    trainID.setCellValueFactory(new PropertyValueFactory<TrainInfoItem,String>("Train1"));
    line.setCellValueFactory(new PropertyValueFactory<TrainInfoItem,String>(""));
    coordinates.setCellValueFactory(new PropertyValueFactory<TrainInfoItem,String>(""));
    //trainInfoTable.getItems().addAll(trainSchedule);


    setTrainInfoData();
    trainInfoTable.getItems().addAll(trainSchedule);

  }

  // connect buttons to event handler
  private void connectButtons() {
    // disable if text fields are empty
    generateSchedule.disableProperty().bind(
        Bindings.isEmpty(setScheduleName.textProperty())
        .and(Bindings.isEmpty(setDesiredThroughput.textProperty()))
    );
    // disable if table is empty
    exportSchedule.disableProperty().bind(
        Bindings.isEmpty(schedulerDriverSchedule.getItems())
        .and(Bindings.isEmpty(schedulerTrainSchedule.getItems()))
    );

    name = setScheduleName.getText();
}

  private void makeSchedule() {
    //TopTable
    //Sets Column factories
    schedTrainID.setCellValueFactory(new PropertyValueFactory<TrainScheduleItem,String>("train id"));
    schedLine.setCellValueFactory(new PropertyValueFactory<TrainScheduleItem,String>("line"));
    schedStation.setCellValueFactory(new PropertyValueFactory<TrainScheduleItem,String>("station"));
    schedArrival.setCellValueFactory(new PropertyValueFactory<TrainScheduleItem, String>("total time to station"));
    schedTotalTime.setCellValueFactory(new PropertyValueFactory<TrainScheduleItem, String>("arrival time"));
    setTrainScheduleData();
    schedulerTrainSchedule.getItems().addAll(trainSchedule);


    //Bottom Label
    schedDriver.setCellValueFactory(new PropertyValueFactory<DriverScheduleItem,String>("Mike"));
    schedDriverTrainID.setCellValueFactory(new PropertyValueFactory<DriverScheduleItem,String>(""));
    schedShiftStart.setCellValueFactory(new PropertyValueFactory<DriverScheduleItem,String>(""));
    setDriverScheduleData();
    schedulerDriverSchedule.getItems().addAll(driverSchedule);


  }

  private void setTrainInfoData(){
    TrainInfoItem train1 = new TrainInfoItem("Train1","Green",300.221, 556,
        120.00,140.222,23.333);
    TrainInfoItem train2 = new TrainInfoItem("Train1","Green",300.221, 556,
        120.00,140.222,23.333);
    TrainInfoItem train3 = new TrainInfoItem("Train1","Green",300.221, 556,
        120.00,140.222,23.333);
    trainData.addAll(train1, train2, train3);
  }

  private void setTrainScheduleData() {
    TrainScheduleItem train1 = new TrainScheduleItem("Train1",
        "Red", "A1", "12:00", "6:00");
    TrainScheduleItem train2 = new TrainScheduleItem("Train2",
        "Red", "A1", "12:00", "6:00");
    TrainScheduleItem train3 = new TrainScheduleItem("Train3",
        "Red", "A1", "12:00", "6:00");
    trainSchedule.addAll(train1, train2, train3);
  }

  private void setDriverScheduleData(){
    DriverScheduleItem Mike = new DriverScheduleItem("Mike","Train1","6:00",
        "10:30","14:00","12:00");
    DriverScheduleItem Julissa = new DriverScheduleItem("Julissa","Train1","6:00",
        "10:30","14:00","12:00");
    DriverScheduleItem Joseph = new DriverScheduleItem("Joseph","Train1","6:00",
        "10:30","14:00","12:00");
    driverSchedule.addAll(Mike, Julissa, Joseph);
  }

  private static void exportCSV(String name) {
    FileWriter writer = null;
    try {
      writer = new FileWriter(name);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        writer.flush();
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }
}

package mbo.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import mbo.controller.Constants;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

  // main buttons
  @FXML
  private Button generateSchedule;
  @FXML
  private Button exportSchedule;

  // mode status indicator
  @FXML
  private Circle mboModeEnabled;

  // train information table columns
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

  // train schedule columns
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

  // driver schedule columns
  @FXML
  private TableColumn schedDriver;
  @FXML
  private TableColumn schedDriverTrainID;
  @FXML
  private TableColumn schedShiftStart;
  @FXML
  private TableColumn schedBreakStart;
  @FXML
  private TableColumn schedBreakEnd;
  @FXML
  private TableColumn schedShiftEnd;

  // scheduler generator inputs
  @FXML
  private Label scheduleName;
  @FXML
  private Label desiredThroughput;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeStatus();
  }

  // initialize values upon UI start
  public void initializeStatus(){
    mboModeEnabled.setFill(Paint.valueOf(Constants.RED));
  }
}

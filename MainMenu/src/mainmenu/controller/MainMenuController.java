package mainmenu.controller;

import ctc.view.CentralTrafficControlUserInterface;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import mainmenu.model.MainMenuModel;
import mbo.view.MovingBlockOverlayUserInterface;
import trackctrl.model.TrackControllerLineManager;
import trackctrl.view.TrackControllerUserInterface;
import trackmodel.view.TrackModelUserInterface;
import traincontroller.model.TrainController;
import traincontroller.model.TrainControllerManager;
import traincontroller.view.TrainControllerUserInterface;
import trainmodel.TrainModelUserInterface;
import trainmodel.model.TrainModel;

/**
 * Main controller class for the Main Menu.
 * All the user interfaces will be called and opened from handlers in this class.
 */
public class MainMenuController implements Initializable {

  private static MainMenuController instance;
  private MainMenuModel mmm = MainMenuModel.getInstance();

  private CentralTrafficControlUserInterface ctcui =
      CentralTrafficControlUserInterface.getInstance();

  private MovingBlockOverlayUserInterface mboui =
      MovingBlockOverlayUserInterface.getInstance();

  private TrackModelUserInterface tmui =
      TrackModelUserInterface.getInstance();

  @FXML private Button trackControllerButton;
  @FXML private ChoiceBox<String> trackControllerLineChoiceBox;
  @FXML private ChoiceBox<String> trackControllerIdChoiceBox;
  @FXML private Button trainControllerButton;
  @FXML private ChoiceBox<String> trainControllerChoiceBox;
  @FXML private Button trainModelButton;
  @FXML private ChoiceBox<String> trainModelChoiceBox;

  private MainMenuController() { }

  /**
   * This is the logic to maintain a single instance of a CTC object.
   * @return the single instance of the CTC
   */
  public static MainMenuController getInstance() {
    if (instance == null) {
      instance = new MainMenuController();
    }
    return instance;
  }

  /**
   * This function sets the lines to the initial line values.
   */
  public void initializeTrackControllers() {

    ObservableList lineList = FXCollections.observableArrayList();
    //ObservableList[] ctrlrLists = FXCollections.observableArrayList()[];
    HashMap<String,ObservableList> ctrlrLists = new HashMap<>();

    lineList.add("Select Line");
    for (TrackControllerLineManager lm : TrackControllerLineManager.getLines()) {
      lineList.add(lm.getLine());
      ObservableList<String> currLineList = FXCollections.observableArrayList();
      currLineList.addAll(lm.getObservableListOfIds());
      ctrlrLists.put(lm.getLine(),currLineList);
    }

    trackControllerLineChoiceBox.setItems(lineList);
    trackControllerLineChoiceBox.setValue("Select Line");

    //Disable invalid operations
    trackControllerIdChoiceBox.setDisable(true);
    trackControllerButton.setDisable(true);

    //Action Event for line selection
    trackControllerLineChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (!(trackControllerLineChoiceBox.getSelectionModel().getSelectedItem().equals("Select Line"))) {
            //Line Selected, select Id, get proper list
            trackControllerIdChoiceBox.setItems(ctrlrLists.get(newValue));
            String testboi = (String) ctrlrLists.get(newValue).get(0);
            trackControllerIdChoiceBox.setValue((String) ctrlrLists.get(newValue).get(0));
            trackControllerIdChoiceBox.setDisable(false);
          } else {
            trackControllerLineChoiceBox.setValue(oldValue);
          }
        });

    //Action Event for id selection
    trackControllerIdChoiceBox.getSelectionModel().selectedItemProperty()
        .addListener((observableValue, oldValue, newValue) -> {
          if (!(trackControllerIdChoiceBox.getSelectionModel().getSelectedItem().equals("Select ID"))) {

            //Line Selected, Id selected, enable button
            trackControllerButton.setDisable(false);
          }
        });
  }

  /**
   * Handler to open to CTC.
   * @param event event from the button.
   */
  @FXML
  private void openCentralTrafficControl(ActionEvent event) {
    try {
      ctcui.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handler to open to MBO.
   * @param event event from the button.
   */
  @FXML
  private void openMovingBlockOverlay(ActionEvent event) {
    try {
      mboui.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handler to open Track Model.
   * @param event event from button.
   */
  @FXML
  private void openTrackModel(ActionEvent event) {
    try {
      tmui.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handler to open Track Controller.
   * @param event event from button.
   */
  @FXML
  private void openTrackController(ActionEvent event) {

    if (trackControllerIdChoiceBox.getSelectionModel().getSelectedItem() != null
        && !(trackControllerLineChoiceBox.getSelectionModel().getSelectedItem().equals("Select Line"))) {
      TrackControllerUserInterface.openTrackController(
          trackControllerIdChoiceBox.getSelectionModel().getSelectedItem());
    }
  }

  /**
   * Handler to open Train Controller.
   * @param event event from button.
   */
  @FXML
  private void openTrainController(ActionEvent event) {
    TrainControllerUserInterface.openTrainController(
        trainControllerChoiceBox.getSelectionModel().getSelectedItem());
  }

  /**
   * Disables and enables the train controller dropdown on the main menu.
   */
  public void updateTrainControllerDropdown() {
    trainControllerChoiceBox.setItems(TrainControllerManager.getListOfTrainControllers());

    if (TrainControllerManager.getListOfTrainControllers().size() > 0) {
      trainControllerChoiceBox.setDisable(false);
      trainControllerButton.setDisable(false);

      if (trainControllerChoiceBox.getSelectionModel().isEmpty()) {
        trainControllerChoiceBox.setValue(TrainControllerManager.getListOfTrainControllers().get(0));
      }
    } else {
      trainControllerButton.setDisable(true);
      trainControllerChoiceBox.setDisable(true);
    }
  }

  /**
   * Handler to open Train Model.
   * @param event event from button.
   */
  @FXML
  private void openTrainModel(ActionEvent event) {
    TrainModelUserInterface.openTrainModel(
        trainModelChoiceBox.getSelectionModel().getSelectedItem());
  }

  /**
   * Enables and disables the train model dropdown.
   */
  public void updateTrainModelDropdown() {
    ObservableList list = TrainModel.getObservableListOfTrainModels();
    trainModelChoiceBox.setItems(list);

    if (TrainModel.getObservableListOfTrainModels().size() > 0) {
      trainModelChoiceBox.setDisable(false);
      trainModelButton.setDisable(false);

      if (trainModelChoiceBox.getSelectionModel().isEmpty()) {
        trainModelChoiceBox.setValue(TrainModel.getObservableListOfTrainModels().get(0));
      }
    } else {
      trainModelButton.setDisable(true);
      trainModelChoiceBox.setDisable(true);
    }
  }

  public void updateTrackControllerPanel() {

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    initializeTrackControllers();
  }
}

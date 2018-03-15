package mainmenu.controller;

import ctc.view.CentralTrafficControlUserInterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import mainmenu.model.MainMenuModel;
import mbo.view.MovingBlockOverlayUserInterface;
import trackmodel.view.TrackModelUserInterface;
import traincontroller.model.TrainController;
import traincontroller.view.TrainControllerUserInterface;
import trainmodel.TrainModelUserInterface;
import trainmodel.model.TrainModel;

/**
 * Main controller class for the Main Menu.
 * All the user interfaces will be called and opened from handlers in this class.
 */
public class MainMenuController implements Initializable {

  private static int id = 0;

  private MainMenuModel mmm = MainMenuModel.getInstance();

  private static MainMenuController instance;

  private CentralTrafficControlUserInterface ctcui =
      CentralTrafficControlUserInterface.getInstance();

  private MovingBlockOverlayUserInterface mboui =
      MovingBlockOverlayUserInterface.getInstance();

  private TrackModelUserInterface tmui =
      TrackModelUserInterface.getInstance();

  @FXML private ChoiceBox<String> trackControllerChoiceBox;
  @FXML private ChoiceBox<String> trainControllerChoiceBox;
  @FXML private ChoiceBox<String> trainModelChoiceBox;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // TODO: initialize lists here
  }

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
   * Handler to open to CTC.
   * @param event event from the button.
   */
  public void openCentralTrafficControl(ActionEvent event) {
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
  public void openMovingBlockOverlay(ActionEvent event) {
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
  public void openTrackModel(ActionEvent event) {
    try {
      tmui.load();
    } catch (Exception e) {
      e.printStackTrace();
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

  public void updateTrainControllerDropdown() {
    trainControllerChoiceBox.setItems(TrainController.getListOfTrains());
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

  public void updateTrainModelDropdown() {
    trainModelChoiceBox.setItems(TrainModel.getObservableListOfTrainModels());
  }
}

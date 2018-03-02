package mainmenu.controller;

import ctc.view.CentralTrafficControlUserInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import mainmenu.model.MainMenuModel;
import mbo.view.MovingBlockOverlayUserInterface;
import trackmodel.view.TrackModelUserInterface;

/**
 * Main controller class for the Main Menu.
 * All the user interfaces will be called and opened from handlers in this class.
 */
public class MainMenuController implements Initializable {

  private MainMenuModel mmm = MainMenuModel.getInstance();

  private CentralTrafficControlUserInterface ctcui =
      CentralTrafficControlUserInterface.getInstance();

  private MovingBlockOverlayUserInterface mboui =
      MovingBlockOverlayUserInterface.getInstance();

  private TrackModelUserInterface tmui =
      TrackModelUserInterface.getInstance();

  @FXML private ChoiceBox<String> trackControllerChoiceBox = new ChoiceBox<>();
  @FXML private ChoiceBox<String> trainControllerChoiceBox = new ChoiceBox<>();
  @FXML private ChoiceBox<String> trainModelChoiceBox = new ChoiceBox<>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // TODO: initialize lists here
  }

  /**
   * Handler to open to CTC.
   * @param event event from the button
   */
  public void openCentralTrafficControl(ActionEvent event) {
    try {
      ctcui.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Handler to open to CTC.
   * @param event event from the button
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
   * @param event event from button
   */
  public void openTrackModel(ActionEvent event) {
    try {
      tmui.load();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

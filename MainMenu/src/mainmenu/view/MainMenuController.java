package mainmenu.view;

import ctc.view.CentralTrafficControlUserInterface;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

/**
 * Main controller class for the Main Menu.
 * All the user interfaces will be called and opened from handlers in this class.
 */
public class MainMenuController implements Initializable {


  @FXML
  private ChoiceBox<String> trackControllerChoiceBox = new ChoiceBox<>();
  @FXML
  private ChoiceBox<String> trainControllerChoiceBox = new ChoiceBox<>();
  @FXML
  private ChoiceBox<String> trainModelChoiceBox = new ChoiceBox<>();
  
  @FXML
  private Button trackControllerButton;
  @FXML
  private Button trainControllerButton;
  @FXML
  private Button trainModelButton;

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    // TODO: populate this with real data
    trainControllerChoiceBox.getItems().addAll("Select Train", "Train 1", "Train 2", "Train 3");
    trainModelChoiceBox.getItems().addAll("Select Train", "Train 1", "Train 2", "Train 3");
    trackControllerChoiceBox.getItems().addAll(
        "Select Wayside", "Wayside 1", "Wayside  2", "Wayside  3");

    trainControllerChoiceBox.setValue("Select Train");
    trainModelChoiceBox.setValue("Select Train");
    trackControllerChoiceBox.setValue("Select Wayside");

  }

  /**
   * Handler to open to CTC.
   * @param event event from the button
   */
  public void openCentralTrafficControl(ActionEvent event) {
    try {
      CentralTrafficControlUserInterface.main(new String[0]);

      //Parent root1 = (Parent) FXMLLoader.load(getClass().getResource("ctc.fxml"));
      //Stage stage = new Stage();
      //stage.setScene(new Scene(root1));
      //stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

package mainmenu.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;


/**
 * Created by jeremyzang on 1/30/18.
 * This class is the controller class for mainmenuview.fxml
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

    //Dummy Data for UI Presentation
    trainControllerChoiceBox.getItems().addAll("Select Train", "Train 1", "Train 2", "Train 3");
    trainModelChoiceBox.getItems().addAll("Select Train", "Train 1", "Train 2", "Train 3");
    trackControllerChoiceBox.getItems().addAll(
        "Select Wayside", "Wayside 1", "Wayside  2", "Wayside  3");

    trainControllerChoiceBox.setValue("Select Train");
    trainModelChoiceBox.setValue("Select Train");
    trackControllerChoiceBox.setValue("Select Wayside");

  }
}

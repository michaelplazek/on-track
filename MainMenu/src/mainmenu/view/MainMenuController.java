package mainmenu.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by jeremyzang on 1/30/18.
 *
 * This class is the controller class for MainMenuView.fxml
 */
public class MainMenuController implements Initializable{

    @FXML
    private ChoiceBox<String> trackController_ChoiceBox  = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> trainController_ChoiceBox  = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> trainModel_ChoiceBox = new ChoiceBox<>();

    @FXML
    private Button trackController_Button;
    @FXML
    private Button trainController_Button;
    @FXML
    private Button trainModel_Button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Dummy Data for UI Presentation
        trainController_ChoiceBox.getItems().addAll("Select Train", "Train 1", "Train 2" , "Train 3");
        trainModel_ChoiceBox.getItems().addAll("Select Train", "Train 1", "Train 2" , "Train 3");
        trackController_ChoiceBox.getItems().addAll("Select Wayside", "Wayside 1" , "Wayside  2", "Wayside  3");

        trainController_ChoiceBox.setValue("Select Train");
        trainModel_ChoiceBox.setValue("Select Train");
        trackController_ChoiceBox.setValue("Select Wayside");

    }
}

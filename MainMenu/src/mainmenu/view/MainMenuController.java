package mainmenu.view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private ChoiceBox<String> trainModel_ChoiceBox= new ChoiceBox<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Dummy Data for UI Presentation
        trainController_ChoiceBox.getItems().addAll("Train 1", "Train 2" , "Train 3");
        trainModel_ChoiceBox.getItems().addAll("Train 1", "Train 2" , "Train 3");
        trackController_ChoiceBox.getItems().addAll("Wayside Controller 1" , "Wayside Controller 2", "Wayside Controller 3");
    }
}

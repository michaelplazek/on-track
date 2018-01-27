package mainmenu.view;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.collections.ObservableArray;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterface extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainmenu.fxml"));
        primaryStage.setTitle("On-Track Train Simulator");
        primaryStage.setScene(new Scene(root, 350, 550));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

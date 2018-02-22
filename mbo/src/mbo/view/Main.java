package mbo.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mbo.controller.MainController;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mbo.fxml"));
        primaryStage.setTitle("Moving Block Overlay");
        primaryStage.setScene(new Scene(root, 955, 969));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
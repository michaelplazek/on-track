package ctc.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UserInterface extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("ctc.fxml"));
    primaryStage.setTitle("Centralized Traffic Control");
    primaryStage.setScene(new Scene(root, 1100, 500));
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}

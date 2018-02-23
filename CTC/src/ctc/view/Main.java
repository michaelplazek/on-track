package ctc.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {

    try {
      Parent root1 = (Parent) FXMLLoader.load(Main.class.getResource("../controller/ctc.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}

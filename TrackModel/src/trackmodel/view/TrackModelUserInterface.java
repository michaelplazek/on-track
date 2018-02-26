package trackmodel.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TrackModelUserInterface {

  /**
   * Main class to called from the Main Menu.
   * @param args necessary String array
   */
  public static void main(String[] args) {

    try {
      Parent root1 = (Parent) FXMLLoader.load(
          TrackModelUserInterface.class.getResource("trackmodel.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}

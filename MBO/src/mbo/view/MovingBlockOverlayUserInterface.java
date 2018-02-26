package mbo.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MovingBlockOverlayUserInterface {

  /**
   * Main method to be called from the Main Menu.
   * @param args necessary String array because Java.
   * @throws IOException for IO problems!
   */
  public static void main(String[] args) throws IOException {
    try {
      Parent root1 = (Parent) FXMLLoader.load(
          MovingBlockOverlayUserInterface.class.getResource("mbo.fxml"));
      Stage stage = new Stage();
      stage.setScene(new Scene(root1));
      stage.show();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}
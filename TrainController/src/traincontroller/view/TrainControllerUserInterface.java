package traincontroller.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TrainControllerUserInterface {


  /**
   * Opens the Train Controller user interface for the specified train.
   * @param trainId Id of a train
   */
  public static void openTrainController(String trainId) {
    FXMLLoader loader = new FXMLLoader(
        TrainControllerUserInterface.class.getResource("TrainControllerUI.fxml"));
    loader.setController(new TrainControllerController(trainId));
    try {
      Parent root = loader.load();
      Stage stage = new Stage();
      stage.setTitle(trainId);
      stage.setScene(new Scene(root, 600, 400));
      stage.show();
    } catch (IOException e) {
      System.out.println(e);
    }
  }
}

package traincontroller.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import traincontroller.controller.TrainControllerController;

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
      Scene scene = new Scene(root, 600, 400);
      scene.getStylesheets().add("utils/ctc_style.css");
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e);
    }
  }
}

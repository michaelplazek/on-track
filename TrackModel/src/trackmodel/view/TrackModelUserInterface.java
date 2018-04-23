package trackmodel.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trackmodel.controller.TrackModelController;
import trackmodel.model.Track;

public class TrackModelUserInterface {

  private static TrackModelUserInterface instance = null;
  private TrackModelController controller;
  private Parent root;
  private Scene scene;

  private TrackModelUserInterface() {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("trackmodel.fxml"));

    try {
      this.root = loader.load();
      this.controller = loader.getController();
      this.scene = new Scene(root);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
   * This is the logic to maintain a single instance of a Track Model UI object.
   * @return the single instance of the Track Model UI
   */
  public static TrackModelUserInterface getInstance() {
    if (instance == null) {
      instance = new TrackModelUserInterface();
    }
    return instance;
  }

  /**
   * Called by Main Menu to load the window for the Track Model UI.
   */
  public void load() {

    Stage stage = new Stage();
    stage.setScene(scene);
    scene.getStylesheets().add("utils/ctc_style.css");
    stage.show();

  }

  public TrackModelController getController() {
    return controller;
  }
}

package ctc.view;

import ctc.controller.CentralTrafficControlController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CentralTrafficControlUserInterface {

  private static CentralTrafficControlUserInterface instance = null;
  private CentralTrafficControlController controller;
  private Parent root;
  private Scene scene;

  private CentralTrafficControlUserInterface() {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("ctc.fxml"));

    try {
      this.root = loader.load();
      this.controller = loader.getController();
      this.scene = new Scene(root);
      scene.getStylesheets().add("utils/ctc_style.css");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * This is the logic to maintain a single instance of a CTC UI object.
   * @return the single instance of the CTC UI
   */
  public static CentralTrafficControlUserInterface getInstance() {
    if (instance == null) {
      instance = new CentralTrafficControlUserInterface();
    }
    return instance;
  }

  /**
   * Called by Main Menu to load the window for the CTC UI.
   */
  public void load() {

    Stage stage = new Stage();
    stage.setScene(this.scene);
    stage.show();
  }

  public CentralTrafficControlController getController() {
    return controller;
  }
}

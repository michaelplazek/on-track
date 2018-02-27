package mbo.view;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MovingBlockOverlayUserInterface {

  private static MovingBlockOverlayUserInterface instance = null;
  private MovingBlockOverlayController controller;
  private Parent root;

  private MovingBlockOverlayUserInterface() {

    FXMLLoader loader = new FXMLLoader(getClass().getResource("mbo.fxml"));

    try {
      this.root = loader.load();
      this.controller = loader.getController();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
   * (mlp)
   * This is the logic to maintain a single instance of a MBO UI object.
   * @return the single instance of the MBO UI
   */
  public static MovingBlockOverlayUserInterface getInstance() {
    if (instance == null) {
      instance = new MovingBlockOverlayUserInterface();
    }
    return instance;
  }

  /**
   * (mlp)
   * Called by Main Menu to load the window for the CTC UI.
   */
  public void load() {

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();
  }

  public MovingBlockOverlayController getController() {
    return controller;
  }
}
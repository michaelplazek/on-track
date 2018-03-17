package traincontroller.model;

import java.util.Map;

import mainmenu.Clock;
import mainmenu.ClockInterface;
import trackmodel.model.Block;

public class TrainControllerManager {
  ClockInterface clock = Clock.getInstance();

  /**
   * Ticks all trains once.
   */
  public static void runTrainControllers() {
    Map<String, TrainController> trains = TrainController.getTrainControllers();
    for (String s : trains.keySet()) {
      run(trains.get(s));
    }
  }

  private static void run(TrainController tc) {

  }

  private static float getSetSpeed() {
    return 0;
  }

  private static float getNextSpeedLimit(Block block) {
    return 0;
  }

  private static int checkForFailures() {
    return 0;
  }

  private static double calculatePowerCommand(double lastSpeed,
                                              double currentSpeed, double setSpeed) {
    return 0;
  }

  private static void updateValues() {

  }

}

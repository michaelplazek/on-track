package traincontroller.model;

import mainmenu.Clock;
import mainmenu.ClockInterface;
import trackmodel.model.Block;
import trackmodel.model.Track;
import trainmodel.model.TrainModelInterface;
import utils.general.Authority;
import utils.train.TrainData;

public class PowerCalculator {
  private static ClockInterface clock = Clock.getInstance();

  /**
   * Updates and runs the train controller.
   * @param tc pass train controller to update
   */
  public static void run(TrainController tc) {
    TrainModelInterface trainModel = tc.getTrainModel();
    double currentSpeed = trainModel.getCurrentSpeed();
    double setSpeed = tc.getSetSpeed();
    double safeStopDistance = getSafeStopDistance(tc);
    Authority authority = tc.getAuthority();
    double speedLimit = getSpeedLimit(tc, safeStopDistance);
    tc.setCurrentSpeed(currentSpeed);
    tc.setCurrentTemperature(trainModel.getCurrentTemp());
  }

  static double getSpeedLimit(TrainController tc, double safeStopDistance) {
    double remainingDistance = safeStopDistance + tc.getDistanceIntoCurrentBlock();
    return recursiveSpeedLimit(tc.getCurrentBlock(), tc.getLastBlock(), remainingDistance);
  }

  private static double recursiveSpeedLimit(Block currentBlock, Block lastBlock,
                                            double remainingDistance) {
    if (currentBlock == null) {
      return 0;
    }
    double max;
    Track track = Track.getTrack(currentBlock.getLine());
    remainingDistance -= currentBlock.getSize();
    if (remainingDistance <= 0) {
      max = currentBlock.getSpeedLimit();
    } else {
      max = Math.max(currentBlock.getSpeedLimit(), recursiveSpeedLimit(
          track.getNextBlock(currentBlock.getNumber(), lastBlock.getNumber()),
          currentBlock, remainingDistance));
      if (currentBlock.isSwitch()) {
        max = Math.max(max, recursiveSpeedLimit(
            track.getNextBlock2(currentBlock.getNumber(), currentBlock.getPreviousBlock()),
            currentBlock, remainingDistance));
      }
    }
    return max;
  }

  static double getSafeStopDistance(TrainController tc) {
    double acceleration = TrainData.SERVICE_BRAKE_ACCELERATION;
    double velocity = tc.getTrainModel().getCurrentSpeed();
    double time = velocity / acceleration;
    return velocity * time + .5 * acceleration * time * time;
  }

  static double getPowerCommand(TrainController tc) {
    double currentSpeed = tc.getTrainModel().getCurrentSpeed();
    double setSpeed = tc.getSetSpeed();
    double lastIntegral = tc.getIntegral();
    double lastSpeed = tc.getCurrentSpeed();
    double kp = tc.getKp();
    double ki = tc.getKi();

    double integral = 0;
    if (currentSpeed != 0) {
      integral = lastIntegral + clock.getChangeInTime() / 2 * (-currentSpeed + lastSpeed);
    }

    double power = kp * (currentSpeed - setSpeed) + ki * integral;

    if (power > TrainData.MAX_POWER) {
      power = TrainData.MAX_POWER;
      integral = lastIntegral;
    }

    tc.setIntegral(integral);

    return power;
  }
}

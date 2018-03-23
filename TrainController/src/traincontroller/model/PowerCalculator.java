package traincontroller.model;

import mainmenu.Clock;
import mainmenu.ClockInterface;
import trainmodel.model.TrainModelInterface;
import utils.train.TrainData;
import utils.train.TrainModelEnums;

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
    double authority = tc.getAuthority();
    if (tc.isAutomatic()) {
      if (currentSpeed > setSpeed) {
        tc.setServiceBrake(TrainModelEnums.BrakeStatus.ON);
        tc.setPowerCommand(0.0);
      } else if (safeStopDistance <= authority * 1.01) {
        tc.setServiceBrake(TrainModelEnums.BrakeStatus.ON);
        tc.setPowerCommand(0.0);
      } else {
        tc.setPowerCommand(getPowerCommand(tc));
      }
    }
    tc.setCurrentSpeed(currentSpeed);
    tc.setCurrentTemperature(trainModel.getCurrentTemp());
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

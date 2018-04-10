package traincontroller.model;

import mainmenu.Clock;
import mainmenu.ClockInterface;
import trackmodel.model.Beacon;
import trackmodel.model.Block;
import trackmodel.model.Track;
import traincontroller.enums.Mode;
import trainmodel.model.TrainModelInterface;
import utils.train.DoorStatus;
import utils.train.Failure;
import utils.train.OnOffStatus;
import utils.train.TrainData;

public class PowerCalculator {
  private static ClockInterface clock = Clock.getInstance();

  /**
   * Updates and runs the train controller.
   * @param tc pass train controller to update
   */
  static void run(TrainController tc) {
    updateFailures(tc);
    executeAction(tc);
    updateTemperature(tc);
    updateModelValues(tc);
  }

  static void updateFailures(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    Failure trackLineStatus = tm.getTrackLineFailureStatus();
    Failure engineStatus = tm.getEngineFailureStatus();
    Failure brakeStatus = tm.getBrakeFailureStatus();

    if (trackLineStatus == Failure.FAILED || engineStatus == Failure.FAILED
        || brakeStatus == Failure.FAILED) {
      tc.setMode(Mode.FAILURE);
    }
  }

  static void executeAction(TrainController tc) {
    Mode mode = tc.getMode();
    if (mode == Mode.FAILURE || mode == Mode.CTC_EMERGENCY_BRAKE)  {
      activateEmergencyBrake(tc);
    } else if (mode == Mode.CTC_BRAKE || mode == Mode.DRIVER_BRAKE) {
      activateServiceBrake(tc);
    } else if (mode == Mode.AT_STATION) {
      executeAtStation(tc);
    } else if (mode == Mode.STATION_BRAKE) {
      executeStopAtStation(tc);
    } else {
      executeNormal(tc);
    }
  }

  static void updateModelValues(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    tc.setCurrentSpeed(tm.getCurrentSpeed());
    tc.setCurrentTemperature(tm.getCurrentTemp());
  }

  static double getSafeStopDistance(TrainController tc) {
    double acceleration = TrainData.SERVICE_BRAKE_ACCELERATION;
    double velocity = tc.getTrainModel().getCurrentSpeed();
    double time = velocity / acceleration;
    return velocity * time + .5 * acceleration * time * time;
  }

  static void updateTemperature(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    double currentTemp = tm.getCurrentTemp();
    double setTemperature = tc.getSetTemperature();
    if (currentTemp <= setTemperature - 0.1) {
      tm.setHeaterStatus(OnOffStatus.ON);
    } else if (currentTemp >= setTemperature + 0.1) {
      tm.setAcStatus(OnOffStatus.OFF);
    }
  }

  static void executeAtStation(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    Beacon beacon = tc.getBeacon();
    activateServiceBrake(tc);
    if (beacon.isRight() && tm.getRightDoorStatus() != DoorStatus.OPEN) {
      tm.setRightDoorStatus(DoorStatus.OPEN);
    } else if (!beacon.isRight() && tm.getLeftDoorStatus() != DoorStatus.OPEN) {
      tm.setLeftDoorStatus(DoorStatus.OPEN);
    }
  }

  static void executeStopAtStation(TrainController tc) {
    double safeStoppingDistance = getSafeStopDistance(tc);
    if (tc.getBeacon() != null) {
      if (tc.getDistanceToStation() - 1 <= safeStoppingDistance) {
        activateServiceBrake(tc);
      } else if (Math.abs(tc.getDistanceToStation()) <= 1 && tc.getCurrentSpeed() == 0) {
        tc.setMode(Mode.AT_STATION);
      } else {
        executeNormal(tc);
      }
    } else {
      executeNormal(tc);
    }
  }

  static void executeNormal(TrainController tc) {
    double currentSpeed = tc.getTrainModel().getCurrentSpeed();
    double setSpeed = tc.getSetSpeed();
    double lastIntegral = tc.getIntegral();
    double lastSpeed = tc.getCurrentSpeed();
    double kp = tc.getKp();
    double ki = tc.getKi();
    double integral;
    double speedLimit = getSpeedLimit(tc, getSafeStopDistance(tc));

    if (currentSpeed > setSpeed || currentSpeed > speedLimit) {
      activateServiceBrake(tc);
      tc.getTrainModel().setPowerCommand(0);
    } else {
      deactivateServiceBrake(tc);

      integral = lastIntegral + clock.getChangeInTime() / 2
          * (Math.abs(currentSpeed - setSpeed) + Math.abs(lastSpeed - setSpeed));

      double power = kp * Math.abs(currentSpeed - setSpeed) + ki * integral;

      if (power > TrainData.MAX_POWER) {
        power = TrainData.MAX_POWER;
        integral = lastIntegral;
      }

      deactivateServiceBrake(tc);
      tc.setIntegral(integral);
      tc.setPowerCommand(80);
    }
  }

  static double getSpeedLimit(TrainController tc, double safeStopDistance) {
    double remainingDistance = safeStopDistance + tc.getDistanceIntoCurrentBlock();
    return recursiveSpeedLimit(tc.getCurrentBlock(), tc.getLastBlock(), remainingDistance);
  }

  private static double recursiveSpeedLimit(Block currentBlock, Block lastBlock,
                                            double remainingDistance) {
    if (currentBlock == null) {
      return 70;
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

  static void activateEmergencyBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getEmergencyBrakeStatus() != OnOffStatus.ON) {
      tc.activateEmergencyBrake();
    }
  }

  static void deactivateServiceBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getServiceBrakeStatus() != OnOffStatus.OFF) {
      tc.setServiceBrake(OnOffStatus.OFF);
    }
  }

  static void activateServiceBrake(TrainController tc) {
    TrainModelInterface tm = tc.getTrainModel();
    if (tm.getServiceBrakeStatus() != OnOffStatus.ON) {
      tc.setServiceBrake(OnOffStatus.ON);
    }
  }
}

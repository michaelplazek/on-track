package traincontroller.model;

import utils.general.Authority;

public interface TrainControllerInterface {

  void setBeaconSignal(Byte[] signal);

  void setTrackCircuitSignal(float speed, Authority authority);

  void activateEmergencyBrake();

}

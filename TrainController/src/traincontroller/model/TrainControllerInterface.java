package traincontroller.model;

import trackmodel.model.Beacon;
import utils.general.Authority;

public interface TrainControllerInterface {

  void setBeaconSignal(Beacon signal);

  void setTrackCircuitSignal(float speed, Authority authority);

  void activateEmergencyBrake();
}

package traincontroller.model;

public interface TrainControllerInterface {

  void setBeaconSignal(Byte[] signal);

  void setTrackCircuitSignal(float speed, float authority);

  void activateEmergencyBrake();

}

package ctc.model;

public interface CentralTrafficControlInterface {

  CentralTrafficControl getInstance();

  void initialize();

  void run();

  void exit();

  boolean isActive();

  void setActive(boolean active);
}

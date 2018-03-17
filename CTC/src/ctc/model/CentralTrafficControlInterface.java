package ctc.model;

public interface CentralTrafficControlInterface {

  void initialize();

  boolean isActive();

  void setActive(boolean active);
}

package mainmenu;

public interface ClockInterface {

  /**
   * Use this function to populate clock in header of CTC and MBO.
   * @return Date object with the current time
   */
  String getFormattedTime();

  /**
   * Use to set the initial time in milliseconds to initialize the clock.
   */
  void setInitialTime();

  /**
   * Use this function to the get the number of milliseconds since the last clock tick.
   * @return the long value in milliseconds
   */
  long getChangeInTime();

  /**
   *  Use this function to set the multiplier on the clock tick speed.
   * @param multiplier is the multiplier used for each clock tick
   */
  void setMultiplier(int multiplier);

  /**
   * Pauses the clock - to be called by CTC.
   */
  void pause();

  /**
   * Unpauses the clock - to be called by CTC.
   */
  void unpause();
}

package mainmenu;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Clock implements ClockInterface {

  private static Clock instance = null;

  private Calendar calendar;
  private DateFormat dateFormat =
      new SimpleDateFormat("HH:mm:ss");
  private long timeSinceLastTick;
  private long simulatedTime;
  private long lastTimestamp;
  private int multiplier = 1;
  private long timedPaused = 0;
  private boolean isPaused = true;

  /**
   * The main constructor for the Clock class.
   */
  private Clock() {}

  /**
   * This is the logic to maintain a single instance of a Clock object.
   * @return the single instance of the Clock
   */
  public static Clock getInstance() {
    if (instance == null) {
      instance = new Clock();
    }
    return instance;
  }

  /**
   * Use to tick the clock.
   */
  public void tick() {

    if (!isPaused) {
      calendar = Calendar.getInstance();
      long delta = calendar.getTimeInMillis() - lastTimestamp - timedPaused;
      timeSinceLastTick = delta * (long) multiplier;
      lastTimestamp += delta;
      simulatedTime += timeSinceLastTick;
    }
  }

  /**
   * Use to initialize the clock time.
   */
  public void setInitialTime() {
    calendar = Calendar.getInstance();
    lastTimestamp = calendar.getTimeInMillis();
    simulatedTime = calendar.getTimeInMillis();
    timeSinceLastTick = 0;
    timedPaused = simulatedTime;
  }

  /**
   * Use this function to retrieve the current time formatted like "HH:mm:ss".
   * @return String containing the current time
   */
  public String getFormattedTime() {
    Date currentTime = new Date(simulatedTime);
    return dateFormat.format(currentTime);
  }

  /**
   * Use this function to retrieve the change in time (in milliseconds)
   * since the last tick. The initial start time will be the time that is
   * initialized upon construction.
   *
   * @return the change in time since the last tick in milliseconds
   */
  public long getChangeInTime() {
    return timeSinceLastTick;
//    return 20;
  }

  /**
   * Use this function to set the multiplier for speeding up or slowing down the clock.
   * @param mult is the multiplier used for each clock tick
   */
  public void setMultiplier(int mult) {
    if (mult > 0) {
      multiplier = mult;
    }
  }

  /**
   * Use to maintain system time when the clock is paused.
   */
  public void pause() {

    if (!isPaused) {
      isPaused = true;
      timedPaused = lastTimestamp;
    }
  }

  /**
   * Use to allow system time to continue as usual.
   */
  public void unpause() {

    if (isPaused) {
      isPaused = false;

      if (timedPaused != 0) {
        calendar = Calendar.getInstance();
        long initial = timedPaused;
        timedPaused = (calendar.getTimeInMillis() - initial);
      }
    }
  }

  /**
   * Use this function to get the multiplier for speeding up or slowing down the clock.
   */
  public int getMultiplier() {
    return multiplier;
  }
}

package ctc.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Clock implements ClockInterface {

  private static Calendar calendar;
  private static DateFormat dateFormat =
      new SimpleDateFormat("HH:mm:ss");
  private static long start;
  private static long end;
  private static long delta;
  private static int multiplier = 1;

  /**
   * The main constructor for the Clock class.
   */
  public Clock() { }

  /**
   * Use to tick the clock for 10 milliseconds.
   */
  public void tick() {
    try {
      Thread.sleep(10);

      calendar = Calendar.getInstance();
      start = end;
      end = calendar.getTimeInMillis();
      delta = end - start;
    } catch (InterruptedException e) {
      System.out.println(e);
    }
  }

  /**
   * Use to initialize the clock time.
   */
  public void setInitialTime() {
    calendar = Calendar.getInstance();
    start = calendar.getTimeInMillis();
  }

  /**
   * Use this function to retrieve the current time formatted like "HH:mm:ss".
   * @return String containing the current time
   */
  public String getFormattedTime() {
    calendar = Calendar.getInstance();
    return dateFormat.format(calendar.getTime());
  }

  /**
   * Use this function to retrieve the change in time (in milliseconds)
   * since the last tick. The initial start time will be the time that is
   * initialized upon construction.
   * @return the change in time since the last tick in milliseconds
   */
  public long getChangeInTime() {
    return delta * multiplier;
  }

  /**
   * Use this function to set the multiplier for speeding up or slowing down the clock.
   * @param mult is the multiplier used for each clock tick
   */
  public void setMultiplier(int mult) {
    multiplier = mult;
  }
}

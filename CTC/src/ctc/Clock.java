package ctc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Clock implements ClockInterface {

  private Calendar calendar;
  private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  private long start;
  private int multiplier = 1;

  /**
   * The main constructor for the Clock class.
   */
  public Clock() {

    this.calendar = Calendar.getInstance();
    this.start = calendar.getTimeInMillis();
  }

  /**
   * Use to tick the clock for a number of milliseconds.
   * @param ms the time in milliseconds
   */
  public void tick(int ms) {
    try {
      this.calendar = Calendar.getInstance();
      this.start = calendar.getTimeInMillis();

      Thread.sleep(ms);
    } catch (InterruptedException e) {
      System.out.println(e);
    }
  }

  /**
   * Use to initialize the clock time.
   */
  public void setInitialTime() {
    this.calendar = Calendar.getInstance();
    this.start = calendar.getTimeInMillis();
  }

  /**
   * Use this function to retrieve the current time formatted like "HH:mm:ss".
   * @return String containing the current time
   */
  public String getFormattedTime() {
    this.calendar = Calendar.getInstance();
    return dateFormat.format(calendar.getTime());
  }

  /**
   * Use this function to retrieve the change in time (in milliseconds)
   * since the last tick. The initial start time will be the time that is
   * initialized upon construction.
   * @return the change in time since the last function call in milliseconds
   */
  public long getChangeInTime() {
    this.calendar = Calendar.getInstance();
    long end = this.calendar.getTimeInMillis();
    return (end - this.start) * this.multiplier;
  }

  /**
   * Use this function to set the multiplier for speeding up or slowing down the clock.
   * @param multiplier is the multiplier used for each clock tick
   */
  public void setMultiplier(int multiplier) {
    this.multiplier = multiplier;
  }
}

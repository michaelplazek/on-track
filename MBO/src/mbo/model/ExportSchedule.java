/*
* ExportSchedule.java
*
* @author Christine Nguyen
* */

package mbo.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

// units of authority in yards

// csv format
// line / station: "station name" / dwell time


public class ExportSchedule {

  String scheduleName;
  int scheduleThroughput;
  private ArrayList<String> throughputInfo = new ArrayList<String>();
  private static final char DEFAULT_SEPARATOR = ',';
  private static final String GREEN_LINE = "greenline.csv";
  private static final String RED_LINE = "redline.csv";


  // generated train schedule
  private String schedTrainId;
  private String schedLine;
  private String schedStation;
  private String schedTotalTime;
  private String schedArrival;

  // generated driver schedule
  private String schedDriver;
  private String schedDriverTrainId;
  private String schedShiftStart;
  private String schedBreakStart;
  private String schedShiftEnd;
  private String schedBreakEnd;

  public void setThroughputInfo(ArrayList<String> throughputInfo) {
    this.throughputInfo = throughputInfo;
  }

  /**
   * Method to set schedule start time.
   *
   * @param scheduleStartTime - The schedule start time.
   **/
  public void setScheduleStartTime(String scheduleStartTime) {
    scheduleStartTime = scheduleStartTime.split("\\s")[3].substring(0, 5);
    String scheduleStartTimeFinal = scheduleStartTime.split("\\s")[3].substring(0, 5);

  }

  /**
   * Generates train schedule.
   *
   * @return some shit.
   */
  public ArrayList<String> generateTrainSchedule() {
    for (int i = 0; i < throughputInfo.size(); i += 2) {
      BufferedReader br = null;

      String tempName = throughputInfo.get(0);

      switch (tempName) {
        case "SHADYSIDE":
        case "HERRON AVE":
        case "SWISSVALE":
        case "PENN STATION":
        case "STEEL PLAZA":
        case "FIRST AVE":
        case "STATION SQUARE":
        case "SOUTH HILLS":
          try {
            br = new BufferedReader(new FileReader(new File(RED_LINE)));
          } catch (FileNotFoundException e) {
            System.out.println("File not found!");
          }
          break;
        default:
          System.out.println("not a train station");
          break;
      }
    }
    return null;
  }

  // create operator schedule
  ArrayList<String> temp = new ArrayList<String>();

  public ArrayList<String> getDriverSchedule() {
    return null;
  }

}

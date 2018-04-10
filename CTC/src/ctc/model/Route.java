package ctc.model;

import java.util.LinkedList;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;

public class Route {

  private LinkedList<Block> route;
  private Block start;
  private Block end;
  private int currentIndex;
  private Track line;
  private TrainTracker train;
  private String nextStation;
  private int nextStationIndex;

  /**
   * Default constructor.
   */
  Route(String line, TrainTracker train) {

    this.line = Track.getListOfTracks().get(line);
    this.train = train;
    this.nextStationIndex = 0;
    this.currentIndex = 0;
    this.route = new LinkedList<>();
  }

  /**
   * Create a new route associated with a TrainTracker.
   * @param end final block of the route
   * @param line the line of the route
   */
  public Route(Block end, String line, TrainTracker train) {

    this.line = Track.getListOfTracks().get(line);
    this.train = train;
    this.nextStationIndex = 0;
    this.currentIndex = 0;
    createRoute(this.line.getStartBlock(), end);
  }

  /**
   * Create a new route for rerouting a train down the track.
   * @param start current Block
   * @param end final Block
   * @param line current line
   */
  public Route(Block start, Block end, String line, TrainTracker train) {

    this.line = Track.getListOfTracks().get(line);
    this.train = train;
    this.nextStationIndex = 0;
    this.currentIndex = 0;
    createRoute(start, end);
  }

  Block getNext() {
    if (currentIndex < route.size() - 1) {
      return route.get(currentIndex + 1);
    }
    return null;
  }

  Block getCurrent() {
    return route.get(currentIndex);
  }

  Block getPrevious() {
    if (currentIndex > 0) {
      return route.get(currentIndex - 1);
    }
    return null;
  }

  public Block getFirst() {
    return this.start;
  }

  Block getLast() {
    return this.end;
  }

  void incrementCurrentIndex() {
    this.currentIndex++;
  }

  void incrementNextStationIndex() {
    this.nextStationIndex++;
  }

  ScheduleRow getNextStop() {

    Schedule schedule = train.getSchedule();
    if (schedule.getStops().size() > 0 && nextStationIndex < schedule.getStops().size()) {
      return schedule.getStops().get(nextStationIndex);
    }

    return null;
  }

  String getNextStation() {

    Block current = getCurrent();
    int index = currentIndex;
    while ((!current.isRightStation() && !current.isLeftStation())
        && current != end
        && index < route.size()) {
      current = route.get(index++);
    }

    if (current != end) {
      return current.getStationName();
    } else {
      return null;
    }
  }

  private Switch getNextSwitch(int index) {

    Block current = route.get(index);

    while (!current.isSwitch() && current != end) {
      current = route.get(index++);
    }

    if (current != end) {
      return (Switch) current;
    } else {
      return null;
    }
  }

  /*
    Return TRUE for RIGHT
    Return FALSE for LEFT
   */
  boolean getNextDirection() {

    Switch sw = getNextSwitch(currentIndex);
    if (sw == null) {
      return true;
    }

    int index = route.indexOf(sw);
    if (index > 0) {
      while (sw.getPreviousBlock() != route.get(index - 1).getNumber()) {
        index = route.indexOf(sw);
        sw = getNextSwitch(index);
      }

      // nextBlock2 == LEFT && nextBlock1 == RIGHT
      return sw.getNextBlock1() == route.get(index - 1).getNumber();
    }

    return true; // doesn't matter

  }

  public LinkedList<Block> getPath() {
    return this.route;
  }

  private boolean checkPath(Block current) {

    boolean valid;

    // if we reached the last block
    if ((current == this.end)
        || current.getStationName().equals(nextStation)) {
      if (nextStationIndex < train.getSchedule().getStops().size()) {
        nextStation = train.getSchedule().getStops().get(nextStationIndex++).getStop();
      }
      return true;
    } else if (current.isSwitch()) {
      return false;
    } else {
      valid = checkPath(line.getBlock(current.getNextBlock1()));
    }

    return valid;
  }

  /**
   * Use to test a fork and decide with path to take.
   * @return return true if you should fork and false otherwise
   */
  private boolean shouldFork(Switch sw) {

    boolean fork;
    boolean straight;

    // check main path
    straight = checkPath(line.getBlock(sw.getNextBlock1()));

    // check forked path
    if (!straight) {
      fork = checkPath(line.getBlock(sw.getNextBlock2()));
      return fork;
    }

    return false;
  }

  /**
   * Use to build the route.
   */
  private void traverse(Block current, Block previous, LinkedList<Block> path) {

    if (current == this.end) {
      return;
    }

    boolean shouldFork;

    Block next = line.getNextBlock(current.getNumber(), previous.getNumber());

    // add current block to path
    path.add(current);

    if (next.isSwitch()) {

      // add next block - switch - to path
      path.add(next);

      Block fork = line.getNextBlock2(next.getNumber(), current.getNumber());
      Block straight = line.getNextBlock(next.getNumber(), current.getNumber());
      Block after;

      // if we are entering the wrong side of switch and have to go straight
      if (fork == null) {

        if (next.getPreviousBlock() == current.getNumber()) {
          after = line.getBlock(next.getNextBlock1());
        } else {
          after = line.getBlock(next.getPreviousBlock());
        }

        traverse(after, next, path);

      } else if (straight == null) {

        if (next.getPreviousBlock() == current.getNumber()) {
          after = fork;
        } else {
          after = line.getBlock(next.getPreviousBlock());
        }

        traverse(after, next, path);

      } else {

        // determine what to do
        shouldFork = shouldFork((Switch) next);

        // make the move
        if (!shouldFork) {
          after = line.getNextBlock(next.getNumber(), current.getNumber());
          traverse(after, next, path);
        } else {
          after = line.getNextBlock2(next.getNumber(), current.getNumber());
          traverse(after, next, path);
        }
      }
    } else {
      next = line.getNextBlock(current.getNumber(), previous.getNumber());
      traverse(next, current, path);
    }
  }

  /**
   * Use by the CTC to create a new route or reroute a train.
   * @param start starting block of the route
   * @param end final block of the route
   * @return true if operation was a success and false otherwise
   */
  private boolean createRoute(Block start, Block end) {

    this.start = start;
    this.end = end;

    LinkedList<Block> path = new LinkedList<>();

    // check if we're starting from the first block
    if (start == line.getStartBlock()) {

      path.add(line.getBlock(-1));
      path.add(start);
      currentIndex = 1;

      Block current = line.getNextBlock(start.getNumber(), -1);
      traverse(current, line.getBlock(current.getPreviousBlock()), path);
    } else {
      traverse(start, line.getBlock(start.getPreviousBlock()), path);
    }

    nextStationIndex = 0; // reset index
    path.add(end);
    this.route = path;


    return true;
  }
}

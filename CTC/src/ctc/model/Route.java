package ctc.model;

import java.util.LinkedList;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;

public class Route {

  private LinkedList<Block> route;
  private Block start;
  private Block end;
  private Track line;
  private TrainTracker train;
  private String nextStation;
  private int nextStationIndex;

  /**
   * Default constructor.
   */
  protected Route(String line, TrainTracker train) {

    this.line = Track.getListOfTracks().get(line);
    this.train = train;
    this.nextStationIndex = 0;
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
    createRoute(start, end);
  }

  public Block getNext() {

    return new Block();
  }

  public Block getFirst() {

    return new Block();
  }

  /**
   * Use to test a fork and decide with path to take.
   * @param current Block
   * @return return true if you should fork and false otherwise
   */
  private boolean shouldFork(Block current) {

    boolean isPath = false;

    // check if current block is a switch
    if (current.isSwitch()) {

      Switch sw = (Switch) current;
      if (line.getNextBlock2(sw.getNumber(), sw.getPreviousBlock()) != null) {
        isPath = shouldFork(line.getBlock(sw.getNextBlock2()));

        // if we reached the last block
        if ((current == this.end) || current.getStationName().equals(nextStation)) {
          if (nextStationIndex < train.getSchedule().getStops().size()) {
            nextStation = train.getSchedule().getStops().get(nextStationIndex++).getStop();
          }
          return true;
        }
      }
    }

    if (!isPath) {

      // continue up the path
      isPath = shouldFork(line.getBlock(current.getNextBlock1()));

      // if we reached the last block
      if ((current == this.end) || current.getStationName().equals(nextStation)) {
        if (nextStationIndex < train.getSchedule().getStops().size()) {
          nextStation = train.getSchedule().getStops().get(nextStationIndex++).getStop();
        }
        return true;
      }
    }

    return isPath;
  }

  /**
   * Use to build the route.
   */
  private void traverse(Block current, LinkedList<Block> path) {

    if (current == this.end) {
      return;
    }

    boolean shouldFork = false;
    Block next;

    // add current block to path
    path.add(current);

    if (current.isSwitch()) {
      shouldFork = shouldFork(current);
    }

    if (!shouldFork) {
      next = line.getNextBlock(current.getNumber(), current.getPreviousBlock());
      traverse(next, path);
    } else {
      next = line.getNextBlock2(current.getNumber(), current.getPreviousBlock());
      traverse(next, path);
    }
  }

  /**
   * Use by the CTC to create a new route or reroute a train.
   * @param start starting block of the route
   * @param end final block of the route
   * @return true if operation was a success and false otherwise
   */
  public boolean createRoute(Block start, Block end) {

    this.start = start;
    this.end = end;

    LinkedList<Block> path = new LinkedList<>();

    // check if we're starting from the first block
    if (start == line.getStartBlock()) {

      path.add(start);

      Block current = line.getBlock(start.getNextBlock1());
      traverse(current, path);
    } else {
      traverse(start, path);
    }

    this.route = path;

    return true;
  }
}

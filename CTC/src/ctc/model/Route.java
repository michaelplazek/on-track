package ctc.model;

import java.util.LinkedList;
import java.util.ListIterator;

import sun.awt.image.ImageWatched;
import trackmodel.model.Block;
import trackmodel.model.Switch;
import trackmodel.model.Track;
import utils.general.Authority;

public class Route {

  private LinkedList<Block> route;
  private Block start;
  private Block end;
  private Track line;
  private TrainTracker train;

  /**
   * Default constructor.
   */
  protected Route(String line) {

    this.line = Track.getListOfTracks().get(line);
    this.route = new LinkedList<>();
  }

  /**
   * Create a new route associated with a TrainTracker.
   * @param end final block of the route
   * @param line the line of the route
   */
  public Route(Block end, String line) {

    this.line = Track.getListOfTracks().get(line);
    createRoute(this.line.getStartBlock(), end);
  }

  /**
   * Create a new route for rerouting a train down the track.
   * @param start current Block
   * @param end final Block
   * @param line current line
   */
  public Route(Block start, Block end, String line) {

    this.line = Track.getListOfTracks().get(line);
    createRoute(start, end);
  }

  public Block getNext() {

    return new Block();
  }

  public Block getFirst() {

    return new Block();
  }

  @SuppressWarnings("unchecked")
  private void traverse(Block current, LinkedList<Block> path) {

    // add current block to the path
    path.add(current);

    // if we reached the last block
    if (current == this.end) {

      this.route = (LinkedList<Block>) path.clone();
      return;
    }

    // return if we reach the end of the track and we still haven't reached the target block
    if (current.getNumber() == -1) {
      return;
    }

    if (current.isSwitch()) {

      Switch sw = (Switch) current;

      traverse(line.getBlock(sw.getNextBlock1()), path);
      traverse(line.getBlock(sw.getNextBlock2()), path);
    } else {
      traverse(line.getBlock(current.getNextBlock1()), path);
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

    return true;
  }
}

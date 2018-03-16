package ctc.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import trackmodel.model.Block;

public class Route {

  private LinkedList<Block> path;
  private Block current;

  /**
   * Default constructor.
   */
  public Route() {

    this.path = new LinkedList<>();
  }

  public void addBlock(Block block) {
    path.add(block);
  }

  public ListIterator<Block> getIterator() {
    return path.listIterator();
  }
}

package trackmodel.model;

public class Switch {

  private int id;
  private Block mainB;
  private boolean state;
  private Block destination1;
  private Block destination2;

  public Switch(int id, Block mainB) {
    setId(id);
    setMainBlock(mainB);
  }

  public int getId() {
    return this.id;
  }

  public Block getDestination1() {
    return this.destination1;
  }

  public Block getDestination2() {
    return this.destination2;
  }

  public boolean getState() {
    return this.state;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setMainBlock(Block mainB) {
    this.mainB = mainB;
  }

  public void setDestination1(Block dest1) {
    this.destination1 = dest1;
  }

  public void setDestination2(Block dest2) {
    this.destination2 = dest2;
  }

  /**
   * This method will allow the user to set a State.
   * @param newState The boolean value of a state for the switch.
   */
  public void setState(boolean newState) {
    this.state = newState;

    if (!state) {
      if (mainB.getNextBlock() == destination1) {
        mainB.setNextBlock(destination1);
      } else if (mainB.getPreviousBlock() == destination2) {
        mainB.setPreviousBlock(destination1);
      }

      if (destination1.getNextBlock() == null) {
        destination1.setNextBlock(mainB);
      } else if (destination1.getPreviousBlock() == null) {
        destination1.setPreviousBlock(mainB);
      }
    } else if (state) {
      if (mainB.getNextBlock() == destination1) {
        mainB.setNextBlock(destination2);
      } else if (mainB.getPreviousBlock() == destination1) {
        mainB.setPreviousBlock(destination2);
      }

      if (destination1.getNextBlock() == mainB) {
        destination1.setNextBlock(null);
      } else if (destination1.getPreviousBlock() == mainB) {
        destination1.setPreviousBlock(null);
      }

      if (destination2.getNextBlock() == mainB) {
        destination2.setNextBlock(mainB);
      } else if (destination2.getPreviousBlock() == mainB) {
        destination2.setPreviousBlock(mainB);
      }
    }
  }

  public void toggleSwitch() {
    setState(!state);
  }

  public String toString() {
    return "ID =" + id + ", Main Block = " + mainB.getNumber() + ", Destination 1 "
            + destination1.getNumber() + ", Destination 2 = " + destination2.getNumber();
  }
}

package utils.general;

public class Authority {
  private AuthorityCommand authorityCommand;
  private byte blocksLeft;

  public Authority() {
    this.authorityCommand = AuthorityCommand.EMERGENCY_BRAKE_STOP;
    this.blocksLeft = 0;
  }

  public Authority(AuthorityCommand authorityCommand, byte blocksLeft) {
    this.authorityCommand = authorityCommand;
    this.blocksLeft = blocksLeft;
  }

  public AuthorityCommand getAuthorityCommand() {
    return authorityCommand;
  }

  public void setAuthorityCommand(AuthorityCommand authorityCommand) {
    this.authorityCommand = authorityCommand;
  }

  public byte getBlocksLeft() {
    return blocksLeft;
  }

  public void setBlocksLeft(byte blocksLeft) {
    this.blocksLeft = blocksLeft;
  }
}

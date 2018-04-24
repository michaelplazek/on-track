package utils.general;

public enum AuthorityCommand {
  SERVICE_BRAKE_STOP,
  STOP_AT_NEXT_STATION,
  SEND_POWER,
  EMERGENCY_BRAKE_STOP,
  STOP_AT_LAST_STATION,
  STOP_IN_THREE_BLOCKS, // for non-station authority
  STOP_AT_END_OF_ROUTE
}

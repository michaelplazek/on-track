keywords:
block - block refers to any block on the track that is not a switch or openCrossing

crossing -

station -

preferred -

IF/THEN - used for BLOCK section code

IF/AND/THEN - used for SWITCH section code (limited to the three possible directions of the switch)

[ |+/-N| ] - relative block traversal direction and distance (in logical blocks)

[ /N/ ] - absolute block position, will check if block ID is equal to this number

[isOccupied/notOccupied]

[isBroken/notBroken]

[isFreezing/notFreezing]

[movingTo/movingFrom] - this will check occupancies based on previous times and return true if movement is detected
                        coming to/from the block being checked

[pblock/n1block/n2block] - these are used in switch logic to search a particular number of blocks in a certain direction.

********************************************************************************************************************************

state assertions:
[brake/auto] - sets Authority on affected blocks to either brake status (stop train) or relays the automatic mode from the CTC

[switchN2/switchN1] - switch will be set to NextBlock2 (- speed given) or NextBlock1 (+ speed given). Lights are set automatically
                      based on the state, but a PLC engineer may still test the lights in Manual Mode. Additionally, Authority is
                      overridden from previous PLC statements to account for switch State.

[closed/open] - crossing will be set to closed/open

[heaterOn/heaterOff]

[ignore] - sets evaluation term to true




examples:

BLOCK:
IF (block |+4| notOccupied) THEN (blocks |+4| auto)
IF (block |+8| isOccupied) THEN (blocks |+4| brake)


SWITCH:
NRM switch example:
TODO: confirm if you need direction on switches, I don't think I do. In general, you move away from the switch
      N blocks (unsigned).
IF (pblock |+6| notOccupied) AND (n1block |0| ignore) AND (n2block |+6| notOccupied) THEN (switch /62/ switchN1)

Track Heater example:
IF (station |0| isFreezing) THEN (station |0| heaterOn)
IF (station |0| notFreezing) THEN (station |0| heaterOff)


NEW PLC ENFORCEMENTS:

- Possibly: always have switches to yard be in the last track controller and under the same track controller's jurisdiction
          : in turn - this means the first track controller will be placed at the first block from the yard
          : prone to change - for Red line starting loop. May add the whole loop into a controller? Controlled by switches



- MovingTo/MovingFrom and isOccupied/notOccupied behavior around switches:
    - Should continue in direction UNTIL a block is no longer in the track controllers jurisdiction [return occupancy]
    - Should continue through switches through Track nextBlock call (TODO: MAYBE)
        -- edge case: switch is in invalid state from current traversal (Have plc programmer specify correct number of blocks? - doesn't work because of distrubuted application of plc code)

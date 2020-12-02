# 2048

This game was tested and works on Java 1.8+.
When in the root directory of this project:

1. Change directory to `bin`
2. Run on the terminal: `java Frame1`
3. A window will pop up.
4. Enter your name, size of board and select your desired board size and Game Mode
Currently, two game modes are supported:
**Practice**: Classic 2048 with unlimited undo and lookahead moves.
**Simulation**: The game executes random moves until the board reaches a sum of 8. If the sum exceeds 8, it will backtrack.


# Future Expansion

Additional Game Modes can be added by extending the Game Class and defining/overriding the required components in the subclass.

A 2048 Game-playing agent can be built by using the undo() and lookahead() methods

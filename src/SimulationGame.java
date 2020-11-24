import java.util.Random;
import javax.swing.Timer;

/**
 * A Game for a demo of key game functionality.
 * Makes moves in random directions till a sum of tiles = 8 is attained.
 * If the sum exceeds 8, it backtracks till sum goes below 8, and continues making moves. 
 */
public class SimulationGame extends Game {
	/**
	 * Time gap between moves(in milliseconds), for easy visualization.
	 */
	public static final int PAUSE_BETWEEN_MOVE = 2000;
	/**
	 * For generating random moves.
	 */
	private static final Random RANDOM = new Random();
	/**
	 * For making moves at regular intervals of {@link SimulationGame#PAUSE_BETWEEN_MOVE}.
	 */
	private javax.swing.Timer timer;

	/**
	 * Creates a SimulationGame with dimensions n x n.
	 * @param n The size of the board.
	 * @param s The name of the game mode.
	 */
	public SimulationGame(int n, String s) {
		// here target=8, is the sum of tiles.
		super(n, 8, s);
		frame.undo.setVisible(false);
		frame.lookahead.setVisible(false);
		frame.resetLookahead.setVisible(false);
		frame.jComboBox1.setVisible(false);
		System.out.println("START");
		timer = new Timer(PAUSE_BETWEEN_MOVE, e -> {
			simulate();
	    });
		timer.start();
	}
	
	/**
	 * The move/undo logic to be executed at regular intervals.
	 */
	private void simulate() {
		System.out.println("SumOfTiles:" + currentState.getSumOfTiles());
		System.out.println(currentState);
		int sum=currentState.getSumOfTiles();
		if(sum==winningTile) {
			gameOver=true;
			System.out.println("Game Over");
			timer.stop();
		}
		else if(sum>winningTile) {
			System.out.println("MOVE: UNDO");
			super.undo();
		}
		else {
			MoveDirection moveDir;
			int rand = RANDOM.nextInt(4);
			switch (rand) {
			case 0:
				moveDir = MoveDirection.UP;
				System.out.println("Move: UP");
				break;
			case 1:
				moveDir = MoveDirection.RIGHT;
				System.out.println("Move: RIGHT");
				break;
			case 2:
				moveDir = MoveDirection.DOWN;
				System.out.println("Move: DOWN");
				break;
			default:
				moveDir = MoveDirection.LEFT;
				System.out.println("Move: LEFT");
				break;
			}
			move(moveDir, true);
		}
	}

}

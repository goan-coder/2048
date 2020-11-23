import java.util.Random;
import javax.swing.JOptionPane;

public class SimulationGame extends Game {
	// in ms
	public static final int PAUSE_BETWEEN_MOVE = 2000;
	private static final Random RANDOM = new Random();

	public SimulationGame(int n, int target, String s) {
	// here winning tile is taken to be the target sum
		super(n, target, s);
		System.out.println("START");
		System.out.println("SumOfTiles:" + currentState.getSumOfTiles());
		System.out.println(currentState);
	}
	
	public void checkGameOver() {
		// here winning tile is taken to be the target sum
		if (currentState.getSumOfTiles() == winningTile) {
			JOptionPane.showMessageDialog(null, "Target sum reached! Simulation complete.");
			gameOver=true;
		}
	}

	public void move(MoveDirection dir,Boolean flag) {
		if(gameOver==true)
			return;
		try {
			Thread.sleep(PAUSE_BETWEEN_MOVE);
		} catch (InterruptedException e) {
			System.out.println("Interrupted while paused for next move in simulation.");
			e.printStackTrace();
		}
		// check if over target sum, if yes, undo
		if (currentState.getSumOfTiles() > winningTile) {
			System.out.println("MOVE: UNDO");
			super.undo();
		}
		else {
			// make a random move
			// generate a random direction
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
			super.move(moveDir, true);
		}
		System.out.println("SumOfTiles:" + currentState.getSumOfTiles());
		System.out.println(currentState);
		move(dir, flag);
	}
}

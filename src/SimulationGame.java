import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class SimulationGame extends Game {
	// in ms
	public static final int PAUSE_BETWEEN_MOVE = 2000;
	private static final Random RANDOM = new Random();
	private javax.swing.Timer timer;

	public SimulationGame(int n, String s) {
	// here winning tile is taken to be the target sum
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
	
	private void simulate() {
		// TODO Auto-generated method stub
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

//	public void checkGameOver() {
//		// here winning tile is taken to be the target sum
//		if (currentState.getSumOfTiles() == winningTile) {
//			JOptionPane.showMessageDialog(null, "Target sum reached! Simulation complete.");
//			gameOver=true;
//		}
//	}

//	public void move(MoveDirection dir,Boolean flag) {
//		if(gameOver==true)
//			return;
//		try {
//			Thread.sleep(PAUSE_BETWEEN_MOVE);
//		} catch (InterruptedException e) {
//			System.out.println("Interrupted while paused for next move in simulation.");
//			e.printStackTrace();
//		}
//		// check if over target sum, if yes, undo
//		if (currentState.getSumOfTiles() > winningTile) {
//			System.out.println("MOVE: UNDO");
//			super.undo();
//		}
//		else {
//			// make a random move
//			// generate a random direction
//			MoveDirection moveDir;
//			int rand = RANDOM.nextInt(4);
//			switch (rand) {
//			case 0:
//				moveDir = MoveDirection.UP;
//				System.out.println("Move: UP");
//				break;
//			case 1:
//				moveDir = MoveDirection.RIGHT;
//				System.out.println("Move: RIGHT");
//				break;
//			case 2:
//				moveDir = MoveDirection.DOWN;
//				System.out.println("Move: DOWN");
//				break;
//			default:
//				moveDir = MoveDirection.LEFT;
//				System.out.println("Move: LEFT");
//				break;
//			}
//			super.move(moveDir, true);
//		}
//		System.out.println("SumOfTiles:" + currentState.getSumOfTiles());
//		System.out.println(currentState);
//		move(dir, flag);
//	}
}

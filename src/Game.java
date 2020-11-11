import java.time.Duration;
import java.time.LocalDateTime;

public class Game {
	private int gameMode;
	private State currentState;
	private State previousState;
	private int size;
	private LocalDateTime startTime;
	private Duration maxTime;
	private int winningTile;
	private boolean canUndo;
	
	private NewJFrame frame;
	Game(int n) {
		size = n;
		startTime = LocalDateTime.now();
		currentState = new State(size);
		//// GUI init start
		frame=new NewJFrame(n,this);
		frame.setVisible(true);
		frame.display(currentState.board);
		//// GUI init end
	}
	
	public void move(MoveDirection dir) {
		// TODO: maybe some more stuff to do here
		previousState = currentState.clone();
		currentState.move(dir);
		frame.display(currentState.board);
	}
}
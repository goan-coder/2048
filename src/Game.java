import java.time.Duration;
import java.time.LocalDateTime;

import javax.swing.JOptionPane;

public class Game {
	private String gameMode;
	private State currentState;
	private State previousState;
	private int size;
	private LocalDateTime startTime;
	private Duration maxTime;
	private int winningTile;
	private boolean canUndo;
	private boolean gameOver;
	
	private NewJFrame frame;
	
	Game(int n,int target,String s) {
		size = n;
		startTime = LocalDateTime.now();
		currentState = new State(size);
		winningTile=target;
		gameMode=s;
		//// GUI init start
		frame=new NewJFrame(n,this,target,s);
		frame.setVisible(true);
		frame.display(currentState.board);
		gameOver=false;
		//// GUI init end
	}
	
	public void move(MoveDirection dir) {
		// TODO: maybe some more stuff to do here
		if(gameOver==true)
			return;
		
		previousState = currentState.clone();
		currentState.move(dir);
		frame.display(currentState.board);
		checkGameOver();
	}
	
	private void checkGameOver() {
		if(currentState.getMaxTile()>=winningTile) {
			JOptionPane.showMessageDialog(null, "YOU WIN");
			gameOver=true;
		}
		else if(currentState.getNumEmptyTiles()==0) {
			JOptionPane.showMessageDialog(null, "YOU LOSE");
			gameOver=true;
		}
		
	}
	
}
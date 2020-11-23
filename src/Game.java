import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import javax.swing.JOptionPane;

public class Game {
	private String gameMode;
	protected State currentState;
	private int size;
	private LocalDateTime startTime;
	private Duration maxTime;
	protected int winningTile;
	private boolean canUndo;
	protected boolean gameOver;
	private boolean lookaheadFlag;
	private State resetState;
	protected NewJFrame frame;
	
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
		frame.jTextField12.setText("START");
		frame.jTextField13.setText(currentState.getSumOfTiles()+"");
		gameOver=false;
		lookaheadFlag=false;
		//// GUI init end
	}
	
	public static Game fromGameMode(int n, int target, String s) {
		if (s.equals("Simulation"))
			return new SimulationGame(n, s);
		else return new Game(n, target, s);
	}
	
	public void move(MoveDirection dir,Boolean flag) {
		// TODO: maybe some more stuff to do here
		// flag=true -> makeNewState instead of using existing
		// flag=false -> useExistingState;
		if(gameOver==true)
			return;
		State newState;
		if(currentState.isEmpty(dir)) {
			newState=currentState.clone(0);
			currentState.insert(newState,dir);
			currentState=newState;
			currentState.move(dir);
		}
		else if(flag==false)  {
			newState=currentState.getRandomChild(dir);
			currentState=newState;
		}
		else {
			newState=currentState.getRandomChild(dir);
			newState=newState.clone(1);
			newState.updateRandomTile();
			currentState.insert(newState,dir);
			currentState=newState;
		}
		frame.display(currentState.board);
		frame.jTextField12.setText(dir.name());
		frame.jTextField13.setText(currentState.getSumOfTiles()+"");
		checkGameOver();
	}
	
	public void checkGameOver() {
		if(lookaheadFlag==true)
			return;
		if(currentState.getMaxTile()>=winningTile) {
			JOptionPane.showMessageDialog(null, "YOU WIN");
			gameOver=true;
		}
		else if(currentState.getNumEmptyTiles()==0 && currentState.noAdjacentsSame()) {
			JOptionPane.showMessageDialog(null, "YOU LOSE");
			gameOver=true;
		}
		
	}

	public void undo() {
		// TODO Auto-generated method stub
		if(currentState.hasParent()==false || gameOver)
			return;
		currentState=currentState.getParentState();
		frame.display(currentState.board);
		frame.jTextField12.setText("UNDO");
		frame.jTextField13.setText(currentState.getSumOfTiles()+"");
		if(isLookaheadFlag()==true && currentState==resetState)
			lookaheadFlag=false;
		if(isLookaheadFlag()==true)
			frame.jTextField12.setText("UNDO-L");
		else
			frame.jTextField12.setText("UNDO");
	}
	
	public void lookahead(MoveDirection dir) {
		if(gameOver)
			return;
		if(isLookaheadFlag()==false) {
			resetState=currentState;
			lookaheadFlag=true;
		}
		move(dir,false);
		frame.jTextField12.setText("LOOKAHEAD "+dir.name());
	}
	public void resetLookahead() {
		if(gameOver || isLookaheadFlag()==false) {
			return;
		}
		currentState=resetState;
		lookaheadFlag=false;
		frame.display(currentState.board);
		frame.jTextField12.setText("RESET LOOKAHEAD");
		frame.jTextField13.setText(currentState.getSumOfTiles()+"");
	}

	public boolean isLookaheadFlag() {
		return lookaheadFlag;
	}
	
}
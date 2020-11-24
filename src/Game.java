import javax.swing.JOptionPane;

/**
 * Manages the game.
 */
public class Game {
	/**
	 * The name of the game mode.
	 */
	private String gameMode;
	/**
	 * The current state of the game.
	 */
	protected State currentState;
	/**
	 * The size of the board.
	 */
	private int size;
	/**
	 * One generating a tile of this value, the game is won.
	 */
	protected int winningTile;
	/**
	 * If the game is over gameOver will be set to True.
	 */
	protected boolean gameOver;
	/**
	 * Indicates if we are in look ahead mode, True if so.
	 */
	private boolean lookaheadFlag;
	/**
	 * The state to original state to reset to, on resetting the look ahead.
	 */
	private State resetState;
	/**
	 * The GUI frame for this game.
	 */
	protected NewJFrame frame;
	
	/**
	 * Creates a new game.
	 * @param n The size of the board, n x n.
	 * @param target The target winning tile.
	 * @param s The name of the game mode.
	 */
	Game(int n,int target,String s) {
		size = n;
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
	
	/**
	 * Factory to make specialized games based on the game mode.
	 * @param n The size of the board, n x n.
	 * @param target The target win condition (may be winning tile or sum of tiles, based on game mode).
	 * @param s The name of the game mode.
	 * @return The created Game object.
	 */
	public static Game fromGameMode(int n, int target, String s) {
		if (s.equals("Simulation"))
			return new SimulationGame(n, s);
		else return new Game(n, target, s);
	}
	
	/**
	 * Makes a move in the specified direction, reusing previously calculated moves, if possible.
	 * @param dir The direction to move in.
	 * @param flag True-> make a new state, False-> use existing state
	 */
	public void move(MoveDirection dir,Boolean flag) {
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
	
	/**
	 * Check and set {@link Game#gameOver}
	 */
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

	/**
	 * Undo the last move.
	 */
	public void undo() {
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
	
	 /**
	  * Lookahead in the specified direction
	  * @param dir The direction to lookahead in.
	  */
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
	
	/**
	 * Resets the lookahead status and reverts to the original state before lookahead.
	 */
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

	/**
	 * Returns true if in lookahead state.
	 */
	public boolean isLookaheadFlag() {
		return lookaheadFlag;
	}
	
	
	protected int getMaxAttainableTile() {
		int res;
		lookahead(MoveDirection.UP);
		res = currentState.getMaxTile();
		undo();
		lookahead(MoveDirection.RIGHT);
		Math.max(res, currentState.getMaxTile());
		undo();
		lookahead(MoveDirection.DOWN);
		Math.max(res, currentState.getMaxTile());
		undo();
		lookahead(MoveDirection.LEFT);
		Math.max(res, currentState.getMaxTile());
		undo();
		return res;
	}
}
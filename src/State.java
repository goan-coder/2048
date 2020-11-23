import java.util.ArrayList;
import java.util.Random;
/**
 * Encapsulates state of the game.
 */
public class State {
	/**
	 * Stores the game board as a 2D matrix
	 */
	int[][] board;
	/**
	 * The dimensions of the board is size x size
	 */
	protected int size;
	/**
	 * The score of the player.
	 * Calculated as done in the original 2048 game, based on the sum of values of merged tiles.
	 */
	protected int score;
	/**
	 * The highest tile currently on the board.
	 */
	protected int maxTile;

	/**
	 * Stores the list of indexes empty tiles.
	 */
	protected ArrayList<Integer> emptyTiles;
	/**
	 * Link to the parent state.
	 */
	protected State parentState;
	/**
	 * List of enumerated children on making a LEFT move on the current state.
	 */
	protected ArrayList<State> leftChildren;
	/**
	 * List of enumerated children on making a RIGHT move on the current state.
	 */
	protected ArrayList<State> rightChildren;
	/**
	 * List of enumerated children on making a UP move on the current state.
	 */
	protected ArrayList<State> upChildren;
	/**
	 * List of enumerated children on making a DOWN move on the current state.
	 */
	protected ArrayList<State> downChildren;
	/**
	 * Stores the sum of all the tiles.
	 */
	protected int sumOfTiles;
	/**
	 * Stores the row at which the randomly generated tile was inserted.
	 * When making a move from the parentState.
	 */
	protected int randomTileRow;
	/**
	 * Stores the column at which the randomly generated tile was inserted.
	 * When making a move from the parentState.
	 */
	protected int randomTileCol;
	
	/**
	 * Constructs a board of size x size
	 * @param size The dimension of the board
	 */
	public State(int size) {
		this.size = size;
		// upper left corner is (0, 0)
		board = new int[size][size];
		fillZeros();
		emptyTiles=new ArrayList<Integer>();
		leftChildren = new ArrayList<State>();
		rightChildren= new ArrayList<State>();
		upChildren= new ArrayList<State>();
		downChildren= new ArrayList<State>();
		parentState=null;
		score = 0;
		sumOfTiles=0;
		updateEmptyTiles();
		maxTile = insertRandomTile();
		maxTile = Math.max(maxTile, insertRandomTile());
	}
	
	/**
	 * Inserts a tile of value either 2 or 4 at a random empty position on the board.
	 * @return The value of the tile inserted.
	 */
	protected int insertRandomTile() {
		if (emptyTiles.size() == 0) {
			// can't insert a tile
			return 0;
		}
		Random rand = new Random(); 
		int index=rand.nextInt();
		index=index%emptyTiles.size();
		if(index<0)
			index*=-1;
		int i=(emptyTiles.get(index))/size;
		int j=(emptyTiles.get(index))%size;
		int val=rand.nextInt();
		if(val%2==1)
			val=4;
		else
			val=2;
		board[i][j]=val;
		randomTileRow = i;
		randomTileCol = j;
		sumOfTiles+=val;
		emptyTiles.remove(index);
		return val;
	}

	/**
	 * Fills the {@link State#board} with zeros (empty tiles).
	 * Used during initialization.
	 */
	private void fillZeros() {
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				board[i][j] = 0;
			}
		}
	}

	/**
	 * Updates the list of {@link State#emptyTiles}.
	 */
	private void updateEmptyTiles(){
		emptyTiles.clear();
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(board[i][j]==0){
					emptyTiles.add(i*size+j);
				}
			}
		}
	}
	
	/**
	 * Makes a game move in the specified direction.
	 * @param dir The direction for the move.
	 * @return A value greater than 0, if there was either any tile was shifted or merged. Else 0.
	 */
	public int move(MoveDirection dir) {
		int moveCount, mergeCount;
		moveCount = slideTiles(dir);
		mergeCount = mergeTiles(dir);
		moveCount += slideTiles(dir);
			
		updateEmptyTiles();
		insertRandomTile();
		
		return moveCount + mergeCount;
	}
	/**
	 * Slides the tiles in the specified direction, to close gaps.
	 * Does NOT merge tiles.
	 * @param dir The direction to slide tiles in.
	 * @return A value greater than 0 if any tile was moved. Else 0.
	 */
	protected int slideTiles(MoveDirection dir) {
		int tilesMoved = 0;
		Position start, iter, lastEmpty;
		switch(dir) {
		case UP:
			start = new Position(0, 0, 0, 1);
			// for iter we just need to set the deltas correctly
			// row, col will  be set later
			iter =  new Position(0, 0, 1, 0);
			break;
		case RIGHT:
			start = new Position(size-1, size-1, -1, 0);
			iter =  new Position(0, 0, 0, -1);
			break;
		case DOWN:
			start = new Position(size-1, size-1, 0, -1);
			iter =  new Position(0, 0, -1, 0);
			break;
		case LEFT:
			start = new Position(0, 0, 1, 0);
			iter =  new Position(0, 0, 0, 1);
			break;
		default:
			System.err.println("Invalid MoveDirection. Defaulting to UP.");
			start = new Position(0, 0, 0, 1);
			iter =  new Position(0, 0, 1, 0);
			break;
		}
		
		for (int i = 0; i < size; ++i) {
			iter.goToPos(start);
			lastEmpty = iter.clone();
			int iterMovesLeft = size - 1;
			int lastEmptyMovesLeft = size - 1;
			// find first empty space
			while (lastEmptyMovesLeft >= 0 && board[lastEmpty.getRow()][lastEmpty.getCol()] != 0) {
				lastEmptyMovesLeft -= lastEmpty.next();
			}
			// if something to do here
			if (lastEmptyMovesLeft > 0) {
				iterMovesLeft -= iter.goToPos(lastEmpty);
				iterMovesLeft -= iter.next();
				// at this point lastEmpty will be at an empty tile
				// and iter will be 1 position ahead of it
				while (iterMovesLeft >= 0) {
					if (board[iter.getRow()][iter.getCol()] != 0) {
						board[lastEmpty.getRow()][lastEmpty.getCol()] = board[iter.getRow()][iter.getCol()];
						board[iter.getRow()][iter.getCol()] = 0;
						lastEmptyMovesLeft -= lastEmpty.next();
						tilesMoved++;
					}
					iterMovesLeft -= iter.next();
				}
			}

			start.next();
		}
		return tilesMoved;
	}
	
	/**
	 * Merge adjacent tiles, assuming a move was made in the specified direction.
	 * Call {@link State#slideTiles(MoveDirection)} first.
	 * @param dir The direction in which the move was made.
	 * @return A value greater than 0 if any tiles were merged. Else 0.
	 */
	protected int mergeTiles(MoveDirection dir) {
		Position start, iter;
		int tilesMerged = 0;
		switch(dir) {
		case UP:
			start = new Position(0, 0, 0, 1);
			// for iter we just need to set the deltas correctly
			// row, col will  be set later
			iter =  new Position(0, 0, 1, 0);
			break;
			
		case RIGHT:
			start = new Position(size-1, size-1, -1, 0);
			iter =  new Position(0, 0, 0, -1);
			break;
		case DOWN:
			start = new Position(size-1, size-1, 0, -1);
			iter =  new Position(0, 0, -1, 0);
			break;
		case LEFT:
			start = new Position(0, 0, 1, 0);
			iter =  new Position(0, 0, 0, 1);
			break;
		default:
			System.err.println("Invalid MoveDirection. Defaulting to UP.");
			start = new Position(0, 0, 0, 1);
			iter =  new Position(0, 0, 1, 0);
			break;
		}
		
		for (int i = 0; i < size; ++i) {
			iter.goToPos(start);
			int iterMovesLeft = size - 1;
			while (iterMovesLeft > 0) {
				if (board[iter.getRow()][iter.getCol()] != 0 &&
						board[iter.getRow()][iter.getCol()] == board[iter.getRow()+iter.getRowDelta()][iter.getCol()+iter.getColDelta()]) {
					board[iter.getRow()][iter.getCol()] *= 2;
					board[iter.getRow()+iter.getRowDelta()][iter.getCol()+iter.getColDelta()] = 0;

					score += board[iter.getRow()][iter.getCol()];
					maxTile = Math.max(maxTile, board[iter.getRow()][iter.getCol()]);
					tilesMerged++;
					
					iterMovesLeft -= iter.next();
					iterMovesLeft -= iter.next();
				}
				else {
					iterMovesLeft -= iter.next();
				}
			}
			start.next();
		}
		return tilesMerged;
	}
	
	/**
	 * Creates a clone of this object.
	 * The children lists are not cloned. They are empty in the new object.
	 * Can be used to make both siblings or children.
	 * @param flag If flag==0, the {@link State#parentState} of the clone is set to the calling object. Else it is set to the parentState of the calling object.
	 * @return The cloned object.
	 */
	public State clone(int flag) {
		State ret = new State(size);
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				ret.board[i][j] = this.board[i][j];
			}
		}
		ret.score = this.score;
		ret.maxTile = this.maxTile;
		ret.sumOfTiles = this.sumOfTiles;
		ret.emptyTiles = new ArrayList<Integer>(this.emptyTiles);
		ret.randomTileRow = this.randomTileRow;
		ret.randomTileCol = this.randomTileCol;
		if(flag==0)
			ret.parentState=this;
		else if(flag==1)
			ret.parentState=this.parentState;
		return ret;
	}
	
	/**
	 * Getter for {@link State#maxTile}
	 */
	public int getMaxTile() {
		return maxTile;
	}

	/**
	 * @return Returns the number of empty tiles on the board.
	 */
	public int getNumEmptyTiles() {
		return emptyTiles.size();
	}
	
	/**
	 * Getter for {@link State#score}
	 */
	public int getScore() {
		return score;
	}
	
	/**
	 * Precondition: Board must be full.
	 * @return True if there are tiles that can be merged.
	 */
	public Boolean noAdjacentsSame() {
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				if(i<size-1 && board[i][j]==board[i+1][j])
					return false;
				if(j<size-1 && board[i][j]==board[i][j+1])
					return false;
			}
		}
		return true;
	}

	/**
	 * @param dir Direction of the move.
	 * @return Returns true if the list of children in the specified direction is empty. Else, False.
	 */
	public boolean isEmpty(MoveDirection dir) {
		// TODO Auto-generated method stub
		if(dir==MoveDirection.LEFT)
			return leftChildren.isEmpty();
		else if(dir==MoveDirection.RIGHT)
			return rightChildren.isEmpty();
		else if(dir==MoveDirection.UP)
			return upChildren.isEmpty();
		else
			return downChildren.isEmpty();
	}
	
	/**
	 * Inserts a state into the list of children.
	 * @param newState The state to insert
	 * @param dir The direction of the children list.
	 */
	public void insert(State newState, MoveDirection dir) {
		// TODO Auto-generated method stub
		if(dir==MoveDirection.LEFT)
			leftChildren.add(newState);
		else if(dir==MoveDirection.RIGHT)
			rightChildren.add(newState);
		else if(dir==MoveDirection.UP)
			upChildren.add(newState);
		else
			downChildren.add(newState);
		
	}
	
	/**
	 * @param dir The children list direction to get the state from.
	 * @return Returns a random state from the children list of the specified direction.
	 */
	public State getRandomChild(MoveDirection dir) {
		// TODO Auto-generated method stub
		Random rand = new Random(); 
		if(dir==MoveDirection.LEFT) {
			int index=rand.nextInt();
			index=index%leftChildren.size();
			if(index<0)
				index*=-1;
			return leftChildren.get(index);
		}
		else if(dir==MoveDirection.RIGHT) {
			int index=rand.nextInt();
			index=index%rightChildren.size();
			if(index<0)
				index*=-1;
			return rightChildren.get(index);
		}
		else if(dir==MoveDirection.UP) {
			int index=rand.nextInt();
			index=index%upChildren.size();
			if(index<0)
				index*=-1;
			return upChildren.get(index);
		}
		else {
			int index=rand.nextInt();
			index=index%downChildren.size();
			if(index<0)
				index*=-1;
			return downChildren.get(index);
		}
	}

	/**
	 * Removes the current random tile and inserts a new one.
	 */
	public void updateRandomTile() {
		sumOfTiles -= board[randomTileRow][randomTileCol];
		board[randomTileRow][randomTileCol] = 0;
		updateEmptyTiles();
		insertRandomTile();
	}

	/**
	 * Getter for {@link State#parentState}.
	 */
	public State getParentState() {
		return parentState;
	}

	/**
	 * @return True, if this is the root state.
	 */
	public boolean hasParent() {
		return parentState!=null;
	}
	
	/**
	 * Getter for {@link State#sumOfTiles}
	 */
	public int getSumOfTiles() {
		return sumOfTiles;
	}
	
	/**
	 * @return A String representation of {@link State#board}
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				sb.append(String.format("%5d ", board[i][j]));
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}

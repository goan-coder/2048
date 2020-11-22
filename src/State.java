import java.util.ArrayList;
import java.util.Random;

public class State {
	int[][] board;
	private int size;
	private int score;
	private int maxTile;
	private int mergeStreak;
	private ArrayList<Integer> emptyTiles;
	private State parentState;
	private ArrayList<State> leftChildren;
	private ArrayList<State> rightChildren;
	private ArrayList<State> upChildren;
	private ArrayList<State> downChildren;
	private int sumOfTiles;
	private int randomTileRow;
	private int randomTileCol;
	
	
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
		mergeStreak = 0;
		sumOfTiles=0;
		updateEmptyTiles();
		maxTile = insertRandomTile();
		maxTile = Math.max(maxTile, insertRandomTile());
	}
	
	private int insertRandomTile() {
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

	private void fillZeros() {
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				board[i][j] = 0;
			}
		}
	}

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

	public int move(MoveDirection dir) {
		int moveCount, mergeCount;
		moveCount = slideTiles(dir);
		mergeCount = mergeTiles(dir);
		moveCount += slideTiles(dir);
			
		updateEmptyTiles();
		insertRandomTile();
		
		if (mergeCount > 0) mergeStreak++;
		else mergeStreak = 0;
		return moveCount + mergeCount;
	}
	
	private int slideTiles(MoveDirection dir) {
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
	
	private int mergeTiles(MoveDirection dir) {
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
	
	public State clone(int flag) {
		State ret = new State(size);
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				ret.board[i][j] = this.board[i][j];
			}
		}
		ret.score = this.score;
		ret.maxTile = this.maxTile;
		ret.mergeStreak = this.mergeStreak;
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
	
	// perc is in range [0, 1]
	// denotes what fraction of the score should be added as bonus
	public void addBonusPoints(float perc) {
		score += (int)(perc * score);
	}
	
	public int getMaxTile() {
		return maxTile;
	}

	public int getNumEmptyTiles() {
		return emptyTiles.size();
	}
	
	public int getScore() {
		return score;
	}
	
	public int getMergeStreak() {
		return mergeStreak;
	}
	
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

	public void updateRandomTile() {
		sumOfTiles -= board[randomTileRow][randomTileCol];
		board[randomTileRow][randomTileCol] = 0;
		updateEmptyTiles();
		insertRandomTile();
	}

	public State getParentState() {
		return parentState;
	}

	public boolean hasParent() {
		// TODO Auto-generated method stub
		return parentState!=null;
	}
}

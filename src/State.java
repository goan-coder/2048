import java.util.ArrayList;
import java.util.Random;

public class State {
	int[][] board;
	private int size;
	private int score;
	private int maxTile;
	private ArrayList<Integer> emptyTiles;
	
	public State(int size) {
		this.size = size;
		// upper left corner is (0, 0)
		board = new int[size][size];
		fillZeros();
		emptyTiles=new ArrayList<Integer>();
		maxTile = 2;
		score = 0;
		updateEmptyTiles();
		insertRandomTile();
		insertRandomTile();
	}
	
	private void insertRandomTile() {
		if (emptyTiles.size() == 0) {
			// can't insert a tile
			return;
		}
		Random rand = new Random(); 
		int index=rand.nextInt();
		index=index%emptyTiles.size();
		if(index<0)
			index*=-1;
		int i=(emptyTiles.get(index))/size;
		int j=(emptyTiles.get(index))%size;
		board[i][j]=2;
		emptyTiles.remove(index);
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

	public void move(MoveDirection dir) {
		slideTiles(dir);
		mergeTiles(dir);
		slideTiles(dir);
		updateEmptyTiles();
		insertRandomTile();
	}
	
	private void slideTiles(MoveDirection dir) {
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
					}
					iterMovesLeft -= iter.next();
				}
			}

			start.next();
		}
	}
	
	private void mergeTiles(MoveDirection dir) {
		Position start, iter;
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
				if (board[iter.getRow()][iter.getCol()] == board[iter.getRow()+iter.getRowDelta()][iter.getCol()+iter.getColDelta()]) {
					board[iter.getRow()][iter.getCol()] *= 2;
					board[iter.getRow()+iter.getRowDelta()][iter.getCol()+iter.getColDelta()] = 0;

					score += board[iter.getRow()][iter.getCol()];
					maxTile = Math.max(maxTile, board[iter.getRow()][iter.getCol()]);
					iterMovesLeft -= iter.next();
					iterMovesLeft -= iter.next();
				}
				else {
					iterMovesLeft -= iter.next();
				}
			}
			start.next();
		}
	}
	
	public State clone() {
		State ret = new State(size);
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				ret.board[i][j] = this.board[i][j];
			}
		}
		ret.score = this.score;
		ret.maxTile = this.maxTile;
		ret.emptyTiles = new ArrayList<Integer>(this.emptyTiles);
		return ret;
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
}

public class State {
	private int[][] board;
	private int size;
	private int maxPoints;
	private int maxTile;
	
	public State(int size) {
		this.size = size;
		// upper left corner is (0, 0)
		board = new int[size][size];
		fillZeros();
		// TODO: place 2s in 2 random positions
	}
	
	private void fillZeros() {
		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				board[i][j] = 0;
			}
		}
	}

	public void move(MoveDirection dir) {
		slideTiles(dir);
		mergeTiles(dir);
		slideTiles(dir);
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
			// if nothing to do here
			if (lastEmptyMovesLeft <= 0) return;

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
				if (board[iter.getRow()][iter.getCol()] == board[iter.getRow()+1][iter.getCol()+1]) {
					board[iter.getRow()][iter.getCol()] *= 2;
					board[iter.getRow()+1][iter.getCol()+1] = 0;
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
		// TODO: return a independent clone of this state
	}
}

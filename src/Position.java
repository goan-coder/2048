/**
 * An iterator for a 2D array.
 * Can be used to iterate over a column, row, or diagonal, not over the whole array.
 */
public class Position {
	private int row;
	private int col;
	private int rowDelta;
	private int colDelta;

	/**
	 * Construct an iterator, given the initial postion and the direction(deltas) of change.
	 * @param row The row to start from.
	 * @param col The column to start from.
	 * @param rowDelta The increment on row, on each call of {@link Position#next()}
	 * @param colDelta The increment on col, on each call of {@link Position#next()}
	 */
	public Position(int row, int col,
			int rowDelta, int colDelta) {
		this.row = row;
		this.col = col;
		this.rowDelta = rowDelta;
		this.colDelta = colDelta;
	}
	
	/**
	 * Go to the next position.
	 * @return Always returns 1.
	 */
	public int next() {
		row += rowDelta;
		col += colDelta;
		return 1;
	}
	
	/**
	 * @return The current row 
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @return The current column
	 */
	public int getCol() {
		return col;
	}

	/**
	 * @return The increment on row, on each call of {@link Position#next()}
	 */
	public int getRowDelta() {
		return rowDelta;
	}
	
	/**
	 * @return The increment on col, on each call of {@link Position#next()}
	 */
	public int getColDelta() {
		return colDelta;
	}

	/**
	 * Directly go to the pos, assuming pos is reachable in the current direction of iteration.
	 * @param pos The Position to go t
	 * @return The equivalent number of {@link Position#next()} calls required.
	 */
	public int goToPos(Position pos) {
		// typically pos will always be "ahead" of this object
		int numJumps = (pos.col - col)*colDelta + (pos.row - row)*rowDelta;
		this.row = pos.row;
		this.col = pos.col;
		return numJumps;
	}
	
	/**
	 * Returns a clone of the calling object.
	 */
	public Position clone() {
		return new Position(row, col, rowDelta, colDelta);
	}
}
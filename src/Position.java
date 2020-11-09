/*
 * For storing position in a 2D Array
 */
public class Position {
	private int row;
	private int col;
	private int rowDelta;
	private int colDelta;

	public Position(int row, int col,
			int rowDelta, int colDelta) {
		this.row = row;
		this.col = col;
		this.rowDelta = rowDelta;
		this.colDelta = colDelta;
	}
	
	public int next() {
		row += rowDelta;
		col += colDelta;
		return 1;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}

	public int goToPos(Position pos) {
		// typically pos will always be "ahead" of this object
		int numJumps = (col - pos.col)*colDelta + (row - pos.row)*rowDelta;
		this.row = pos.row;
		this.col = pos.col;
		return numJumps;
	}
	
	public Position clone() {
		return new Position(row, col, rowDelta, colDelta);
	}
}
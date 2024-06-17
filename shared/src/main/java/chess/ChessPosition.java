package chess;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    String columns = "ABCDEFGH";

    private int row;
    private int col;

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass()!=getClass()){
            return false;
        };
        ChessPosition positionObj = (ChessPosition)obj;
        return positionObj.getColumn() == this.getColumn() && positionObj.getRow() == this.getRow();
    }

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(int row, char col) {
        this.row = row;
        this.col = letterToNumber(col);
    }

    private int letterToNumber(char col) {
        col = Character.toUpperCase(col);
        if (columns.contains(String.valueOf(col))) {
            return columns.indexOf(col) + 1;
        } else {
            throw new IllegalArgumentException("Column must be between 'A' and 'H'");
        }
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {

        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }
}

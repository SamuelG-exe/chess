package chess;

import java.util.Arrays;

import static java.lang.Math.abs;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] gameBoard;
    public ChessBoard() {
        gameBoard = new ChessPiece[8][8];
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //System.out.println(position.getRow()+" "+position.getColumn());
        gameBoard[abs(position.getRow()-8)][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return gameBoard[abs(position.getRow()-8)][position.getColumn()-1];
    }

    void resethelperPawn(int row, ChessGame.TeamColor pieceColor){
        for (int i = 1; i <= 8; i++){
            ChessPosition pieceAdder= new ChessPosition(row, i);
            addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.PAWN));
        }

    }
    void resethelperBackrow(int row, ChessGame.TeamColor pieceColor){
        ChessPosition pieceAdder= new ChessPosition(row, 1);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));

        pieceAdder= new ChessPosition(row, 2);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT));

        pieceAdder= new ChessPosition(row, 3);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP));

        pieceAdder= new ChessPosition(row, 4);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN));

        pieceAdder= new ChessPosition(row, 5);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));

        pieceAdder= new ChessPosition(row, 6);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP));

        pieceAdder= new ChessPosition(row, 7);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT));

        pieceAdder= new ChessPosition(row, 8);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     *
     *
     */
    public void resetBoard() {
        ChessPiece[][] gameBoard = new ChessPiece[8][8];
        ///Reset Black
        //System.out.println("Statring backline Black");
        resethelperBackrow(8, ChessGame.TeamColor.BLACK);
        //System.out.println("Starting front line Black");
        resethelperPawn(7, ChessGame.TeamColor.BLACK);

        ///Reset White
        //System.out.println("Starting front line White");
        resethelperPawn(2, ChessGame.TeamColor.WHITE);
        //System.out.println("Starting back line white")
        resethelperBackrow(1, ChessGame.TeamColor.WHITE);

    }

    public boolean onBoard(ChessPosition location){
        return location.getRow() <= 8 && location.getRow() >= 1 && location.getColumn() <= 8 && location.getColumn() >= 1;
    }
    public boolean validMove(ChessPosition startPosition, ChessPosition endPosition){
        return (getPiece(endPosition) == null || getPiece(endPosition).getTeamColor() != getPiece(startPosition).getTeamColor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
            }
        if (o == null || o.getClass() != super.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (that.gameBoard[i][j] == null && gameBoard[i][j]== null){
                    continue;
                }
                if(that.gameBoard[i][j] == null || gameBoard[i][j]== null){
                    return false;
                }
                if (!(that.gameBoard[i][j].getPieceType() == gameBoard[i][j].getPieceType())) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(gameBoard);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "gameBoard=" + Arrays.toString(gameBoard) +
                '}';
    }
}


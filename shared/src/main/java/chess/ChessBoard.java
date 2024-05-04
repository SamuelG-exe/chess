package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] gameBoard = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        gameBoard[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return gameBoard[position.getRow()][position.getColumn()];
    }

    void resethelperPawn(int row, ChessGame.TeamColor pieceColor){
        for (int i = 0; i < 8; i++){
            ChessPosition pieceAdder= new ChessPosition(row, i);
            addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.PAWN));
        }

    }
    void resethelperBackrow(int row, ChessGame.TeamColor pieceColor){
        ChessPosition pieceAdder= new ChessPosition(row, 0);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));

        pieceAdder= new ChessPosition(row, 1);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT));

        pieceAdder= new ChessPosition(row, 2);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP));

        pieceAdder= new ChessPosition(row, 3);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN));

        pieceAdder= new ChessPosition(row, 4);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));

        pieceAdder= new ChessPosition(row, 5);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP));

        pieceAdder= new ChessPosition(row, 6);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT));

        pieceAdder= new ChessPosition(row, 7);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     *
     */
    public void resetBoard() {
        ///Reset Black
        resethelperBackrow(0, ChessGame.TeamColor.BLACK);
        resethelperPawn(1, ChessGame.TeamColor.BLACK);

        ///Reset White
        resethelperPawn(7, ChessGame.TeamColor.WHITE);
        resethelperBackrow(8, ChessGame.TeamColor.WHITE);

    }
}

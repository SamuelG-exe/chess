package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */

public class ChessPiece {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }


    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);

        if (piece.getPieceType() == PieceType.KING){
            for (int xmove = myPosition.getRow()-1, endx = xmove + 1; xmove<endx; xmove++){
                for (int ymove = myPosition.getRow()-1, endy = xmove + 1; ymove<endy; ymove++){
                    ChessPosition checkpos= new ChessPosition(xmove, ymove);
                    if  (   xmove <=8 && xmove >=0 &&
                            ymove <=8 && ymove >=0 &&
                            board.getPiece(checkpos)==null){
                        ///need to finish
                    }
                }
            }
        }


    }
}

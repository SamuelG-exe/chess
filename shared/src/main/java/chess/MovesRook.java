package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesRook {
    public Collection<ChessMove> Rook(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        for(int i = 1; i<8; i++){
            ///going Up
            ChessPosition checkpos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn());
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
            ///going down
            checkpos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn());
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
            ///going Right
            checkpos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i);
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
            ///going Left
            checkpos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i);
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
        }
        return validMoves;
    }
}

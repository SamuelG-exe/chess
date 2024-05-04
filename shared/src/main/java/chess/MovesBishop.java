package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesBishop {
    public Collection<ChessMove> Bishop(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        for(int i = 1; i<8; i++){
            ///going right Up diagonal
            ChessPosition checkpos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
            ///going left UP diagonal
            checkpos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i);
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
            ///going Right Down diagonal
            checkpos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i);
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
            ///going left Down diagonal
            checkpos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i);
            if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)){
                ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                validMoves.add(move);
            }
        }
        return validMoves;
    }
}

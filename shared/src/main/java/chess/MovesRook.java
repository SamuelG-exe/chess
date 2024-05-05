package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesRook {
    public static Collection<ChessMove> rookMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        boolean up = true;
        boolean left = true;
        boolean right = true;
        boolean down = true;

        for(int i = 1; i<8; i++){
            ///going Up
            ChessPosition checkpos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn());
            up = MovesBishop.moveCheckAdder(board, myPosition, validMoves, piece, up, checkpos);

            ///going down
            checkpos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn());
            down = MovesBishop.moveCheckAdder(board, myPosition, validMoves, piece, down, checkpos);

            ///going Right
            checkpos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+i);
            right = MovesBishop.moveCheckAdder(board, myPosition, validMoves, piece, right, checkpos);

            ///going Left
            checkpos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-i);
            left = MovesBishop.moveCheckAdder(board, myPosition, validMoves, piece, left, checkpos);

        }
        return validMoves;
    }

}

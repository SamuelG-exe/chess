package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesKing {
    public static Collection<ChessMove> kingMove(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        //System.out.println("king starting pos: "+myPosition.getRow()+" "+myPosition.getColumn());

        for (int xmove = myPosition.getColumn()-1 , endx = myPosition.getColumn()+ 1; xmove <= endx; xmove++) {
            for (int ymove = myPosition.getRow()-1 , endy = myPosition.getRow()+ 1; ymove <= endy; ymove++) {
                ChessPosition checkpos = new ChessPosition(ymove, xmove);
                if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)) {
                    //System.out.println(ymove +" "+ xmove);
                    ChessMove move = new ChessMove(myPosition, checkpos, null);
                    validMoves.add(move);
                }
            }
        }

        return validMoves;
    }

}



package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesKnight {
    public static Collection<ChessMove> kightMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        System.out.println("Kight starting pos: "+myPosition.getRow()+" "+myPosition.getColumn());

        boolean horizontal = true;

        //right horizontal
        ChessPosition checkpos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+2);
        moverchecker(board, myPosition, checkpos, validMoves, piece, horizontal);

        checkpos = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-2);
        moverchecker(board, myPosition, checkpos, validMoves, piece, horizontal);

        checkpos = new ChessPosition(myPosition.getRow()+2, myPosition.getColumn());
        moverchecker(board, myPosition, checkpos, validMoves, piece, false);

        checkpos = new ChessPosition(myPosition.getRow()-2, myPosition.getColumn());
        moverchecker(board, myPosition, checkpos, validMoves, piece, false);

        return validMoves;
    }
    public static void moverchecker (ChessBoard board, ChessPosition myPosition, ChessPosition checkpos, Collection<ChessMove> validMoves, ChessPiece piece, boolean horizontal) {
        if (horizontal){
            for (int i = -1; i<2; i=i+2) {
                ChessPosition checkposNew = new ChessPosition(checkpos.getRow() + i, checkpos.getColumn());
                if (board.onBoard(checkposNew) && board.validMove(myPosition, checkposNew)) {
                    System.out.println(checkposNew.getRow() + " " + checkposNew.getColumn());
                    ChessMove move = new ChessMove(myPosition, checkposNew, null);
                    validMoves.add(move);
                }
            }
        }
        else{
            for (int i = -1; i<=2; i+=2) {
                ChessPosition checkposNew = new ChessPosition(checkpos.getRow(), checkpos.getColumn()+i);
                if (board.onBoard(checkposNew) && board.validMove(myPosition, checkposNew)) {
                    System.out.println(checkposNew.getRow() + " " + checkposNew.getColumn());
                    ChessMove move = new ChessMove(myPosition, checkposNew, null);
                    validMoves.add(move);
                }
            }

        }
    }
}

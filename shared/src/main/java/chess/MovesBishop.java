package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesBishop {
    public static Collection<ChessMove> bishopMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        System.out.println("Bishop starting pos: "+myPosition.getRow()+" "+myPosition.getColumn());
        boolean rightUp = true;
        boolean rightDown = true;
        boolean leftUp = true;
        boolean leftDown = true;
        
        for(int i = 1; i<=8; i++){
            ///going right Up diagonal
            ChessPosition checkpos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()+i);
            rightUp = moveCheckAdder(board, myPosition, validMoves, piece, rightUp, checkpos);

            ///going left UP diagonal
            checkpos = new ChessPosition(myPosition.getRow()+i, myPosition.getColumn()-i);
            leftUp = moveCheckAdder(board, myPosition, validMoves, piece, leftUp, checkpos);

            ///going Right Down diagonal
            checkpos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()+i);
            rightDown = moveCheckAdder(board, myPosition, validMoves, piece, rightDown, checkpos);

            ///going left Down diagonal
            checkpos = new ChessPosition(myPosition.getRow()-i, myPosition.getColumn()-i);
            leftDown = moveCheckAdder(board, myPosition, validMoves, piece, leftDown, checkpos);

        }
        return validMoves;
    }

    static boolean moveCheckAdder(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPiece piece, boolean logicBool, ChessPosition checkpos) {
        if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos) && logicBool){
            System.out.println(checkpos.getRow() +" "+ checkpos.getColumn());
            ChessMove move = new ChessMove(myPosition, checkpos, null);
            validMoves.add(move);
            if (board.getPiece(checkpos) != null &&(board.getPiece(checkpos).getTeamColor() != board.getPiece(myPosition).getTeamColor())){
                logicBool=false;
            }
        }
        else{logicBool=false;}
        return logicBool;
    }
}

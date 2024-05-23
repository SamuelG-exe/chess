package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesPawn {
    public static Collection<ChessMove> pawnMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor color = piece.getTeamColor();

        if (color == ChessGame.TeamColor.BLACK) {
            ChessPosition nextcheckpos = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            ChessPosition checkpos = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
            if (myPosition.getRow() == 7 && board.getPiece(nextcheckpos) == null) {
                moveAdd(board, myPosition, validMoves, checkpos, false);
            }
            checkpos = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            moveAdd(board, myPosition, validMoves, checkpos, false);

            checkpos = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            moveAdd(board, myPosition, validMoves, checkpos, true);

            checkpos = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            moveAdd(board, myPosition, validMoves, checkpos, true);
        }

        if (color == ChessGame.TeamColor.WHITE) {
            ChessPosition nextcheckpos = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            ChessPosition checkpos = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
            if (myPosition.getRow() == 2 && board.getPiece(nextcheckpos) == null) {
                moveAdd(board, myPosition, validMoves, checkpos, false);
            }
            checkpos = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            moveAdd(board, myPosition, validMoves, checkpos, false);

            checkpos = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            moveAdd(board, myPosition, validMoves, checkpos, true);

            checkpos = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            moveAdd(board, myPosition, validMoves, checkpos, true);


        }
        return validMoves;
    }

    private static void moveAdd(ChessBoard board,
                                ChessPosition myPosition,
                                Collection<ChessMove> validMoves,
                                ChessPosition checkpos,
                                boolean cornerAttack) {


        if ((board.onBoard(checkpos) && board.validMove(myPosition, checkpos)) && cornerAttack && board.getPiece(checkpos) != null) {
            promote(myPosition, validMoves, checkpos);
        } else if (board.onBoard(checkpos) && !cornerAttack && board.getPiece(checkpos) == null) {
            promote(myPosition, validMoves, checkpos);
        }
    }

    private static void promote(ChessPosition myPosition, Collection<ChessMove> validMoves, ChessPosition checkpos) {
        if (checkpos.getRow() == 1 || checkpos.getRow() == 8) {
            //System.out.println(checkpos.getRow() + " " + checkpos.getColumn());
            ChessMove move = new ChessMove(myPosition, checkpos, ChessPiece.PieceType.KNIGHT);
            validMoves.add(move);
            move = new ChessMove(myPosition, checkpos, ChessPiece.PieceType.ROOK);
            validMoves.add(move);
            move = new ChessMove(myPosition, checkpos, ChessPiece.PieceType.QUEEN);
            validMoves.add(move);
            move = new ChessMove(myPosition, checkpos, ChessPiece.PieceType.BISHOP);
            validMoves.add(move);
        } else {
            //System.out.println(checkpos.getRow() + " " + checkpos.getColumn());
            ChessMove move = new ChessMove(myPosition, checkpos, null);
            validMoves.add(move);
        }
    }
}



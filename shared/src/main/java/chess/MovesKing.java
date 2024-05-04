package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesKing {
    public Collection<ChessMove> Kingmove(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(myPosition);

        for (int xmove = myPosition.getRow() - 1, endx = xmove + 1; xmove < endx; xmove++) {
            for (int ymove = myPosition.getRow() - 1, endy = xmove + 1; ymove < endy; ymove++) {
                ChessPosition checkpos = new ChessPosition(xmove, ymove);
                if (board.onBoard(checkpos) && board.validMove(myPosition, checkpos)) {
                    ChessMove move = new ChessMove(myPosition, checkpos, piece.getPieceType());
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }
}



/// previouse checks xmove <= 8 && xmove >= 0 &&
//                        ymove <= 8 && ymove >= 0 &&
//                        board.getPiece(checkpos) == null ||
//                        board.getPiece(checkpos).getTeamColor() != piece.getTeamColor())
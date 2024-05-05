package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovesQueen {
    public static Collection<ChessMove> queenMove(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishop = new ArrayList<>();
        Collection<ChessMove> rook = new ArrayList<>();
        bishop = MovesBishop.bishopMove(board, myPosition);
        rook = MovesRook.rookMove(board, myPosition);
        bishop.addAll(rook);
        return bishop;
    }

}

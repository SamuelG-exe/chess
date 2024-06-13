package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.abs;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable{
    private ChessPiece[][] gameBoard;
    public ChessBoard() {
        gameBoard = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //System.out.println(position.getRow()+" "+position.getColumn());
        gameBoard[abs(position.getRow()-8)][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return gameBoard[abs(position.getRow()-8)][position.getColumn()-1];
    }

    void resethelperPawn(int row, ChessGame.TeamColor pieceColor){
        for (int i = 1; i <= 8; i++){
            ChessPosition pieceAdder= new ChessPosition(row, i);
            addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.PAWN));
        }

    }
    void resetHelperBackrow(int row, ChessGame.TeamColor pieceColor){
        ChessPosition pieceAdder= new ChessPosition(row, 1);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));

        pieceAdder= new ChessPosition(row, 2);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT));


        pieceAdder= new ChessPosition(row, 3);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP));

        pieceAdder= new ChessPosition(row, 4);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN));

        pieceAdder= new ChessPosition(row, 5);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KING));

        pieceAdder= new ChessPosition(row, 6);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP));

        pieceAdder= new ChessPosition(row, 7);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT));

        pieceAdder= new ChessPosition(row, 8);
        addPiece(pieceAdder, new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     *
     *
     */
    public void resetBoard() {
        gameBoard = new ChessPiece[8][8];
        ///Reset Black
        //System.out.println("Statring backline Black");
        resetHelperBackrow(8, ChessGame.TeamColor.BLACK);
        //System.out.println("Starting front line Black");
        resethelperPawn(7, ChessGame.TeamColor.BLACK);

        ///Reset White
        //System.out.println("Starting front line White");
        resethelperPawn(2, ChessGame.TeamColor.WHITE);
        //System.out.println("Starting back line white")
        resetHelperBackrow(1, ChessGame.TeamColor.WHITE);

    }

    public void makeMoveOnBoard(ChessMove move)  {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece pieceToBeMoved = getPiece(start);

            if (move.getPromotionPiece()!=null) {
                ChessPiece promotionpiecce = new ChessPiece(pieceToBeMoved.getTeamColor(), move.getPromotionPiece());
                addPiece(end, promotionpiecce);
                addPiece(start, null);
            }
            else {
                addPiece(end, pieceToBeMoved);
                addPiece(start, null);
            }
    }


    public boolean onBoard(ChessPosition location){
        return location.getRow() <= 8 && location.getRow() >= 1 && location.getColumn() <= 8 && location.getColumn() >= 1;
    }
    public boolean validMove(ChessPosition startPosition, ChessPosition endPosition){
        return (getPiece(endPosition) == null || getPiece(endPosition).getTeamColor() != getPiece(startPosition).getTeamColor());
    }

    /**
     * Finds the position of the king for the specified team color on the given chess board.
     *
     * @param board the chess board to search for the king
     * @param color the team color of the king to find
     * @return the position of the king if found, or {@code null} if the king is not found
     */
    public static ChessPosition kingFinder(ChessBoard board, ChessGame.TeamColor color){
        for(int i = 1; i<= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition inter = new ChessPosition(i, j);
                if(board.getPiece(inter) != null && board.getPiece(inter).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(inter).getTeamColor() == color){
                    ///System.out.println("found king at "+inter.getRow()+" "+inter.getColumn() +" of the team:"+board.getPiece(inter).getTeamColor());

                    return inter;
                }
            }
        }
        return null;
    }


    public static boolean inChecker(ChessBoard board, ChessGame.TeamColor color){
        List<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition kingPos = ChessBoard.kingFinder(board, color);
        for(int i = 1; i<= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition inter = new ChessPosition(i, j);
                if (board.getPiece(inter) != null && kingPos != null){
                    if(board.getPiece(inter).getTeamColor() != board.getPiece(kingPos).getTeamColor()) {
                        ChessPiece enemyPiece = board.getPiece(inter);
                        enemyMoves.addAll(enemyPiece.pieceMoves(board, inter));
                    }
                }
            }
        }
        for(ChessMove move: enemyMoves){
            if(move.getEndPosition().equals(kingPos))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        ChessBoard that = (ChessBoard) o;
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (that.gameBoard[i][j] == null && gameBoard[i][j]== null){
                    continue;
                }
                if(that.gameBoard[i][j] == null || gameBoard[i][j]== null){
                    return false;
                }
                if (!gameBoard[i][j].equals(that.gameBoard[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public int hashCode() {
        return Arrays.deepHashCode(gameBoard);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "gameBoard=" + Arrays.toString(gameBoard) +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            ChessBoard clone = (ChessBoard) super.clone();
            clone.gameBoard = new ChessPiece[8][8];
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition iter = new ChessPosition(i, j);
                    ChessPiece placer = this.getPiece(iter);
                    if (placer != null) {
                        clone.addPiece(iter, placer);
                    }
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException();
        }
    }


}


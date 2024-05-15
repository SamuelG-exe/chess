package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard gameBoard;

    ///black castle
    boolean hasB_L_Moved = false;
    boolean hasB_R_Moved = false;
    boolean BkingMoved = false;

    ///white castle
    boolean hasW_L_Moved = false;
    boolean hasW_R_Moved = false;
    boolean WkingMoved = false;




    public ChessGame() {
        ChessBoard gameBoard = new ChessBoard();
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        Collection<ChessMove> posibleMoves = piece.pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> allowedMoves = piece.pieceMoves(gameBoard, startPosition);

        for(ChessMove move : posibleMoves){
        ChessBoard testBoard = null;
            try {
                testBoard = (ChessBoard) gameBoard.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            ///ChessBoard finalTestBoard = testBoard;
            testBoard.makeMoveOnBoard(move);
            if ((inChecker(testBoard, piece.getTeamColor()))) {
                //System.out.println(move.getEndPosition().getRow()+" "+move.getEndPosition().getColumn()+"this puts me in check");
                allowedMoves.remove(move);
            }
        }

        //for(ChessMove move : posibleMoves){
        //    System.out.println(move.getEndPosition().getRow()+" "+move.getEndPosition().getColumn());
        //}
        return allowedMoves;
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece pieceToBeMoved = gameBoard.getPiece(start);

        if(pieceToBeMoved == null){
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validmoves = pieceToBeMoved.pieceMoves(gameBoard, start);
        if (!(validmoves.contains(move))){
            throw new InvalidMoveException();
        }
        gameBoard.makeMoveOnBoard(move);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return inChecker(gameBoard, teamColor);
    } ///if for each move other team can make is does that return the location of the king

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }


    public static boolean inChecker(ChessBoard board, ChessGame.TeamColor color){
        List<ChessMove> enemyMoves = new ArrayList<>();
        ChessPosition kingpos = kingFinder(board, color);
        for(int i = 1; i<= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition inter = new ChessPosition(i, j);
                if (board.getPiece(inter) != null && kingpos != null){
                    if(board.getPiece(inter).getTeamColor() != board.getPiece(kingpos).getTeamColor()) {
                        ChessPiece enemyPiece = board.getPiece(inter);
                        enemyMoves.addAll(enemyPiece.pieceMoves(board, inter));
                }
                }
            }
        }
        for(ChessMove move: enemyMoves){
            if(move.getEndPosition().equals(kingpos))
                return true;
        }
        return false;
    }

    public static ChessPosition kingFinder(ChessBoard board, TeamColor color){
        for(int i = 1; i<= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition inter = new ChessPosition(i, j);
                if(board.getPiece(inter) != null && board.getPiece(inter).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(inter).getTeamColor() == color){
                    System.out.println("found king at "+inter.getRow()+" "+inter.getColumn() +" of the team:"+board.getPiece(inter).getTeamColor());

                    return inter;
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return turn == chessGame.turn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, gameBoard);
    }
}

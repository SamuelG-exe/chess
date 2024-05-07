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
       if (piece == null){
           return null;
       }
       return piece.pieceMoves(gameBoard, startPosition);
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

        if (move.getPromotionPiece()!=null) {
         ChessPiece promotionpiecce = new ChessPiece(pieceToBeMoved.getTeamColor(), move.getPromotionPiece());
         gameBoard.addPiece(end, promotionpiecce);
         gameBoard.addPiece(start, null);
        }
        else {
            gameBoard.addPiece(end, pieceToBeMoved);
            gameBoard.addPiece(start, null);
        }



    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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


  /**  public static boolean inChecker(ChessBoard board, ChessPosition myPosition, ChessPosition checkpos){
        List<ChessMove> enemyMoves = new ArrayList<>();
        for(int i = 1; i<= 8; i++){
            for(int j = 1; j<= 8; j++){
                ChessPosition inter = new ChessPosition(i, j);
                if(board.getPiece(inter) != null && board.getPiece(inter).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                    ChessPiece enemyPiece = board.getPiece(inter);
                    enemyMoves.addAll(enemyPiece.pieceMoves(board, myPosition));
                }
            }
        }
        return enemyMoves.contains(checkpos);
    }
**/
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

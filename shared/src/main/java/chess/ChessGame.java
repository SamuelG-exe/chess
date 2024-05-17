package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

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

    //En Passant
    boolean canEnPassant = false;
    ChessPosition temporaryPawn = null;



    public ChessGame() {
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        turn = TeamColor.WHITE;
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

        if(piece.getPieceType() == ChessPiece.PieceType.KING && !inChecker(gameBoard, piece.getTeamColor())){
            posibleMoves.addAll(castleMoveChecks(piece.getTeamColor()));
            allowedMoves.addAll(castleMoveChecks(piece.getTeamColor()));
        }

        for(ChessMove move : posibleMoves){
        ChessBoard testBoard = null;
            try {
                testBoard = (ChessBoard) gameBoard.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            testBoard.makeMoveOnBoard(move);
            if ((inChecker(testBoard, piece.getTeamColor()))) {
                //System.out.println(move.getEndPosition().getRow()+" "+move.getEndPosition().getColumn()+"this puts me in check");
                allowedMoves.remove(move);
            }
        }

        for(ChessMove move : posibleMoves){
          System.out.println(move.getEndPosition().getRow()+" "+move.getEndPosition().getColumn());
        }
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

        //invalid move Checks
        if(pieceToBeMoved == null){
            throw new InvalidMoveException();
        }
        Collection<ChessMove> validMoves = validMoves(start);

        if (!(validMoves.contains(move)) || pieceToBeMoved.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException();
        }

        //Castling moves for rook
        if(pieceToBeMoved.getPieceType() == ChessPiece.PieceType.KING && Math.abs(start.getColumn()-end.getColumn()) > 1){
            if(end.getColumn()>5){
                ChessPosition rookStart = new ChessPosition(start.getRow(), 8);
                ChessPosition rookEnd = new ChessPosition(start.getRow(), end.getColumn()-1);
                ChessMove rookMove = new ChessMove(rookStart, rookEnd, null);
                gameBoard.makeMoveOnBoard(rookMove);

            }
            else {
                ChessPosition rookStart = new ChessPosition(start.getRow(), 1);
                ChessPosition rookEnd = new ChessPosition(start.getRow(), end.getColumn()+1);
                ChessMove rookMove = new ChessMove(rookStart, rookEnd, null);
                gameBoard.makeMoveOnBoard(rookMove);

            }
        }



        gameBoard.makeMoveOnBoard(move);
        turn = getTeamTurn()==TeamColor.WHITE ? TeamColor.BLACK :TeamColor.WHITE;
        haveCastlePiecesMoved(start, pieceToBeMoved);

        if(pieceToBeMoved.getPieceType() == ChessPiece.PieceType.PAWN && abs(end.getRow()- start.getRow()) > 1){
            canEnPassant = true;
            if(pieceToBeMoved.getTeamColor() == TeamColor.BLACK){
                temporaryPawn = new ChessPosition(start.getRow()-1, start.getColumn());
            }
            if(pieceToBeMoved.getTeamColor() == TeamColor.WHITE){
                temporaryPawn = new ChessPosition(start.getRow()+1, start.getColumn());
            }

        }
        else{
            canEnPassant = false;
            temporaryPawn = null;
        }



    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return inChecker(gameBoard, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        List<ChessMove> teamMoves = new ArrayList<>(getAllValidMoves(gameBoard, teamColor));
        return inChecker(gameBoard, teamColor) && teamMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        List<ChessMove> teamMoves = new ArrayList<>(getAllValidMoves(gameBoard, teamColor));
        return !(inChecker(gameBoard, teamColor)) && teamMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
        hasB_L_Moved = false;
        hasB_R_Moved = false;
        BkingMoved = false;

        ///white castle
        hasW_L_Moved = false;
        hasW_R_Moved = false;
        WkingMoved = false;


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
                    ///System.out.println("found king at "+inter.getRow()+" "+inter.getColumn() +" of the team:"+board.getPiece(inter).getTeamColor());

                    return inter;
                }
            }
        }
        return null;
    }

    public Collection<ChessMove> getAllValidMoves(ChessBoard board, TeamColor color) {
        List<ChessMove> allMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition inter = new ChessPosition(i, j);
                if (board.getPiece(inter) != null && board.getPiece(inter).getTeamColor() == color) {
                    ChessPosition temPosition = new ChessPosition(i, j);
                    allMoves.addAll(validMoves(temPosition));
                }
            }
        }
        return allMoves;
    }

    public Collection<ChessMove> getAllPieceMoves(ChessBoard board, TeamColor color) {
        List<ChessMove> allMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition inter = new ChessPosition(i, j);
                if (board.getPiece(inter) != null && board.getPiece(inter).getTeamColor() == color) {
                    ChessPiece enemyPiece = board.getPiece(inter);
                    allMoves.addAll(enemyPiece.pieceMoves(board, inter));
                }
            }
        }
        return allMoves;
    }




    public void haveCastlePiecesMoved (ChessPosition start, ChessPiece piece){
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 1 && start.getColumn()== 1){
            hasW_L_Moved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 1 && start.getColumn()== 8){
            hasW_R_Moved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 8 && start.getColumn()== 1){
            hasB_L_Moved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 8 && start.getColumn()== 8){
            hasB_R_Moved = true;
        }


        if(piece.getPieceType() == ChessPiece.PieceType.KING && start.getRow() == 1 && start.getColumn()== 5){
            WkingMoved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING && start.getRow() == 8 && start.getColumn()== 5){
            BkingMoved = true;
        }

    }

    public Collection<ChessMove> castleMoveChecks(TeamColor teamTurn) {
        List<ChessMove> castleMoves = new ArrayList<>();

        if (teamTurn == TeamColor.WHITE) {
            ChessPosition whitekingLocation = kingFinder(gameBoard, teamTurn);
            assert whitekingLocation != null;
            if(whitekingLocation.getRow()==1 && whitekingLocation. getColumn()==5) {
                if (!WkingMoved) {

                    if (!hasW_L_Moved) {
                        List<ChessMove> enemyMoves = new ArrayList<>(getAllPieceMoves(gameBoard, TeamColor.BLACK));
                        boolean safeCross = true;
                        ChessPosition left1 = new ChessPosition(1, 2);
                        ChessPosition left2 = new ChessPosition(1, 3);
                        ChessPosition left3 = new ChessPosition(1, 4);

                        castleHelperQueenside(castleMoves, whitekingLocation, enemyMoves, safeCross, left1, left2, left3);
                    }

                    if (!hasW_R_Moved) {
                        List<ChessMove> enemyMoves = new ArrayList<>(getAllPieceMoves(gameBoard, TeamColor.BLACK));
                        boolean safeCross2 = true;
                        ChessPosition right1 = new ChessPosition(1, 6);
                        ChessPosition right2 = new ChessPosition(1, 7);

                        castleHelperKingside(castleMoves, whitekingLocation, enemyMoves, safeCross2, right1, right2);
                    }
                }
            }
        }
        if (teamTurn == TeamColor.BLACK) {
            ChessPosition blackKingLocation = kingFinder(gameBoard, teamTurn);
            assert blackKingLocation != null;
            if(blackKingLocation.getRow()==8 && blackKingLocation.getColumn()==5) {
                if (!BkingMoved) {

                    if (!hasB_L_Moved) {
                        List<ChessMove> enemyMoves = new ArrayList<>(getAllPieceMoves(gameBoard, TeamColor.WHITE));
                        boolean safeCross = true;
                        ChessPosition left1 = new ChessPosition(8, 2);
                        ChessPosition left2 = new ChessPosition(8, 3);
                        ChessPosition left3 = new ChessPosition(8, 4);

                        castleHelperQueenside(castleMoves, blackKingLocation, enemyMoves, safeCross, left1, left2, left3);
                    }

                    if (!hasB_R_Moved) {
                        List<ChessMove> enemyMoves = new ArrayList<>(getAllPieceMoves(gameBoard, TeamColor.WHITE));
                        boolean safeCross2 = true;
                        ChessPosition right1 = new ChessPosition(8, 6);
                        ChessPosition right2 = new ChessPosition(8, 7);

                        castleHelperKingside(castleMoves, blackKingLocation, enemyMoves, safeCross2, right1, right2);
                    }
                }
            }
        }
        return castleMoves;
    }

    public void castleHelperKingside(List<ChessMove> castleMoves, ChessPosition kingLocation, List<ChessMove> enemyMoves, boolean safeCross2, ChessPosition right1, ChessPosition right2) {
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition().equals(right1) || move.getEndPosition().equals(right2)) {
                safeCross2 = false;
                break;
            }
        }
        if (gameBoard.getPiece(right1) == null && gameBoard.getPiece(right2) == null && safeCross2 && kingLocation.getColumn() == 5 && (kingLocation.getRow() == 1 || kingLocation.getRow() == 8)) {
            ChessPosition whiteKingEnd = new ChessPosition(kingLocation.getRow(), kingLocation.getColumn() + 2);
            ChessMove white_K_CastleRight = new ChessMove(kingLocation, whiteKingEnd, null);
            castleMoves.add(white_K_CastleRight);
        }
    }

    public void castleHelperQueenside(List<ChessMove> castleMoves, ChessPosition whitekingLocation, List<ChessMove> enemyMoves, boolean safeCross, ChessPosition left1, ChessPosition left2, ChessPosition left3) {
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition() == left1 || move.getEndPosition() == left2 || move.getEndPosition() == left3) {
                safeCross = false;
                break;
            }
        }
        if (gameBoard.getPiece(left1) == null && gameBoard.getPiece(left2) == null && gameBoard.getPiece(left3) == null && safeCross) {
            ChessPosition whiteKingEnd = new ChessPosition(whitekingLocation.getRow(), whitekingLocation.getColumn() - 2);
            ChessMove white_K_CastleLeft = new ChessMove(whitekingLocation, whiteKingEnd, null);
            castleMoves.add(white_K_CastleLeft);
        }
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

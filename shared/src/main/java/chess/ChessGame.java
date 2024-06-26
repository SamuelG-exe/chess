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


    private TeamColor whoWon;

    private String whoWonPlayerName;

    private TeamColor turn;
    private ChessBoard gameBoard;

    private Boolean gameOver=false;

    ///black castle
    boolean blackLeftRookMoved = false;
    boolean blackRightRookMoved = false;
    boolean blackKingMoved = false;

    ///white castle
    boolean whiteLeftRookMoved = false;
    boolean whiteRightRookMoved = false;
    boolean whiteKingMoved = false;

    //En Passant
    boolean canEnPassant = false;
    ChessPosition temporaryPawn = null;
    ChessPosition doubleMovedPawn = null;




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

    public TeamColor getWhoWon() {
        return whoWon;
    }

    public void setWhoWon(TeamColor whoWon) {
        this.whoWon = whoWon;
    }

    public String getWhoWonPlayerName() {
        return whoWonPlayerName;
    }

    public void setWhoWonPlayerName(String whoWonPlayerName) {
        this.whoWonPlayerName = whoWonPlayerName;
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
        Collection<ChessMove> posibleMoves;
        Collection<ChessMove> allowedMoves;

        TeamColor tempPawnColor;

        //en passant check

        if(piece.getPieceType() == ChessPiece.PieceType.PAWN && !ChessBoard.inChecker(gameBoard, piece.getTeamColor()) && canEnPassant){
            ChessBoard testBoardPawn = null;
            try {
                testBoardPawn = (ChessBoard) gameBoard.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }

            if(piece.getTeamColor() == TeamColor.WHITE){
                tempPawnColor = TeamColor.BLACK;
            }
            else{
                tempPawnColor = TeamColor.WHITE;
            }

            ChessPiece tempPawn = new ChessPiece(tempPawnColor, ChessPiece.PieceType.PAWN);
            testBoardPawn.addPiece(temporaryPawn, tempPawn);

            posibleMoves = piece.pieceMoves(testBoardPawn, startPosition);
            allowedMoves = piece.pieceMoves(testBoardPawn, startPosition);
        }
        else {
            posibleMoves = piece.pieceMoves(gameBoard, startPosition);
            allowedMoves = piece.pieceMoves(gameBoard, startPosition);
        }

        //castling check
        if(piece.getPieceType() == ChessPiece.PieceType.KING && !ChessBoard.inChecker(gameBoard, piece.getTeamColor())){
            posibleMoves.addAll(castleMoveChecks(piece.getTeamColor()));
            allowedMoves.addAll(castleMoveChecks(piece.getTeamColor()));
        }

        //run the move on a clone to check for "check"
        for(ChessMove move : posibleMoves){
            ChessBoard testBoard = null;
            try {
                testBoard = (ChessBoard) gameBoard.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            testBoard.makeMoveOnBoard(move);
            if ((ChessBoard.inChecker(testBoard, piece.getTeamColor()))) {
                //System.out.println(move.getEndPosition().getRow()+" "+move.getEndPosition().getColumn()+"this puts me in check");
                allowedMoves.remove(move);
            }
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

        //for removing the forward piece on an en pasant attack
        if(temporaryPawn != null && end.equals(temporaryPawn)){
            gameBoard.addPiece(doubleMovedPawn, null);
            doubleMovedPawn = null;
            temporaryPawn = null;
        }

        //En passant moves
        if(pieceToBeMoved.getPieceType() == ChessPiece.PieceType.PAWN && abs(end.getRow()- start.getRow()) > 1){
            canEnPassant = true;
            if(pieceToBeMoved.getTeamColor() == TeamColor.BLACK){
                temporaryPawn = new ChessPosition(start.getRow()-1, start.getColumn());
            }
            if(pieceToBeMoved.getTeamColor() == TeamColor.WHITE){
                temporaryPawn = new ChessPosition(start.getRow()+1, start.getColumn());
            }
            doubleMovedPawn=end;

        }
        else{
            canEnPassant = false;
            temporaryPawn = null;
            doubleMovedPawn = null;
        }
    }


    public void setGameOver(boolean gameOverStatus){
        this.gameOver=gameOverStatus;
    }

    public Boolean getGameOver(){
        return this.gameOver;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return ChessBoard.inChecker(gameBoard, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        List<ChessMove> teamMoves = new ArrayList<>(getAllValidMoves(gameBoard, teamColor));

        boolean inCheckMate = ChessBoard.inChecker(gameBoard, teamColor) && teamMoves.isEmpty();
        if(inCheckMate){gameOver=true;}
        return inCheckMate;
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
        boolean inStalemate =  !(ChessBoard.inChecker(gameBoard, teamColor)) && teamMoves.isEmpty();
        if(inStalemate){gameOver=true;}
        return inStalemate;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
        blackLeftRookMoved = false;
        blackRightRookMoved = false;
        blackKingMoved = false;

        ///white castle
        whiteLeftRookMoved = false;
        whiteRightRookMoved = false;
        whiteKingMoved = false;


    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }


    /**
     * Checks if the king of the specified team color is in check on the given chess board.
     *
     * @param board the chess board to check
     * @param color the team color of the king to check for check condition
     * @return {@code true} if the king is in check, {@code false} otherwise
     */




    /**
     * Retrieves all valid moves for the specified team color on the given chess board.
     *
     * @param board the chess board to evaluate
     * @param color the team color whose valid moves are to be retrieved
     * @return a collection of all valid moves for the specified team color
     */
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


    /**
     * Retrieves all possible moves for all pieces of the specified team color on the given chess board.
     *
     * @param board the chess board to evaluate
     * @param color the team color whose pieces' moves are to be retrieved
     * @return a collection of all possible moves for all pieces of the specified team color
     */
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



    /**
     * Updates the status of whether the castle pieces (rooks and kings) have moved based on the given piece's type and starting position.
     *
     * @param start the starting position of the piece
     * @param piece the piece whose movement status is to be updated
     */
    public void haveCastlePiecesMoved (ChessPosition start, ChessPiece piece){
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 1 && start.getColumn()== 1){
            whiteLeftRookMoved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 1 && start.getColumn()== 8){
            whiteRightRookMoved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 8 && start.getColumn()== 1){
            blackLeftRookMoved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.ROOK && start.getRow() == 8 && start.getColumn()== 8){
            blackRightRookMoved = true;
        }


        if(piece.getPieceType() == ChessPiece.PieceType.KING && start.getRow() == 1 && start.getColumn()== 5){
            whiteKingMoved = true;
        }
        if(piece.getPieceType() == ChessPiece.PieceType.KING && start.getRow() == 8 && start.getColumn()== 5){
            blackKingMoved = true;
        }

    }


    /**
     * Determines all valid castling moves for the current team turn, considering the state of the board and the movement history of the king and rooks.
     *
     * @param teamTurn the team color whose castling moves are to be checked
     * @return a collection of valid castling moves for the specified team color
     */
    public Collection<ChessMove> castleMoveChecks(TeamColor teamTurn) {
        List<ChessMove> castleMoves = new ArrayList<>();

        if (teamTurn == TeamColor.WHITE) {
            ChessPosition whitekingLocation = ChessBoard.kingFinder(gameBoard, teamTurn);
            assert whitekingLocation != null;
            if(whitekingLocation.getRow()==1 && whitekingLocation. getColumn()==5) {
                if (!whiteKingMoved) {

                    if (!whiteLeftRookMoved) {
                        List<ChessMove> enemyMoves = new ArrayList<>(getAllPieceMoves(gameBoard, TeamColor.BLACK));
                        boolean safeCross = true;
                        ChessPosition left1 = new ChessPosition(1, 2);
                        ChessPosition left2 = new ChessPosition(1, 3);
                        ChessPosition left3 = new ChessPosition(1, 4);

                        castleHelperQueenside(castleMoves, whitekingLocation, enemyMoves, safeCross, left1, left2, left3);
                    }

                    if (!whiteRightRookMoved) {
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
            ChessPosition blackKingLocation = ChessBoard.kingFinder(gameBoard, teamTurn);
            assert blackKingLocation != null;
            if(blackKingLocation.getRow()==8 && blackKingLocation.getColumn()==5) {
                if (!blackKingMoved) {

                    if (!blackLeftRookMoved) {
                        List<ChessMove> enemyMoves = new ArrayList<>(getAllPieceMoves(gameBoard, TeamColor.WHITE));
                        boolean safeCross = true;
                        ChessPosition left1 = new ChessPosition(8, 2);
                        ChessPosition left2 = new ChessPosition(8, 3);
                        ChessPosition left3 = new ChessPosition(8, 4);

                        castleHelperQueenside(castleMoves, blackKingLocation, enemyMoves, safeCross, left1, left2, left3);
                    }

                    if (!blackRightRookMoved) {
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


    /**
     * Helper method to check and add valid kingside castling moves to the provided list.
     *
     * @param castleMoves the list to which valid kingside castling moves will be added
     * @param kingLocation the current location of the king
     * @param enemyMoves a list of moves that enemy pieces can make
     * @param safeCross2 a flag indicating if the path is safe for castling
     * @param right1 the first position to the right of the king to check for obstacles
     * @param right2 the second position to the right of the king to check for obstacles
     */
    public void castleHelperKingside(List<ChessMove> castleMoves, ChessPosition kingLocation, List<ChessMove> enemyMoves, boolean safeCross2, ChessPosition right1, ChessPosition right2) {
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition().equals(right1) || move.getEndPosition().equals(right2)) {
                safeCross2 = false;
                break;
            }
        }
        if (gameBoard.getPiece(right1) == null && gameBoard.getPiece(right2) == null && safeCross2 && kingLocation.getColumn() == 5 && (kingLocation.getRow() == 1 || kingLocation.getRow() == 8)) {
            ChessPosition whiteKingEnd = new ChessPosition(kingLocation.getRow(), kingLocation.getColumn() + 2);
            ChessMove kingCastleRight = new ChessMove(kingLocation, whiteKingEnd, null);
            castleMoves.add(kingCastleRight);
        }
    }


    /**
     * Helper method to check and add valid queenside castling moves to the provided list.
     *
     * @param castleMoves the list to which valid queenside castling moves will be added
     * @param whitekingLocation the current location of the king
     * @param enemyMoves a list of moves that enemy pieces can make
     * @param safeCross a flag indicating if the path is safe for castling
     * @param left1 the first position to the left of the king to check for obstacles
     * @param left2 the second position to the left of the king to check for obstacles
     * @param left3 the third position to the left of the king to check for obstacles
     */
    public void castleHelperQueenside(List<ChessMove> castleMoves, ChessPosition whitekingLocation, List<ChessMove> enemyMoves, boolean safeCross, ChessPosition left1, ChessPosition left2, ChessPosition left3) {
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition() == left1 || move.getEndPosition() == left2 || move.getEndPosition() == left3) {
                safeCross = false;
                break;
            }
        }
        if (gameBoard.getPiece(left1) == null && gameBoard.getPiece(left2) == null && gameBoard.getPiece(left3) == null && safeCross) {
            ChessPosition whiteKingEnd = new ChessPosition(whitekingLocation.getRow(), whitekingLocation.getColumn() - 2);
            ChessMove kingCastleLeft = new ChessMove(whitekingLocation, whiteKingEnd, null);
            castleMoves.add(kingCastleLeft);
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

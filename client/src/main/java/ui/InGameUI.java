package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import response.ListGamesResp;
import uiutils.DrawChess;
import uiutils.UserStatus;
import web.ServerFacade;
import web.ServerMessageObserver;
import web.WebSocketFacade;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static chess.ChessPiece.PieceType.*;
import static uiutils.EscapeSequences.*;
import static ui.PreLoginUI.setHelpText;
import static uiutils.UserStatus.*;

public class InGameUI  {
    private ServerFacade server;
    private String input;
    private UserStatus userStatus;
    private final PrintStream out;
    private AtomicReference<GameData> game;

    private WebSocketFacade websocket;



    InGameUI(ServerFacade server, String input, UserStatus userStatus, PrintStream out, AtomicReference<GameData> gameData,  WebSocketFacade websocket) {
        this.server = server;
        this.input = input;
        this.userStatus = userStatus;
        this.out = out;
        this.game = gameData;
        this.websocket=websocket;
    }

    public UserStatus run() throws Exception {
        switch (input) {
            case "help": help();
                break;
            case "redraw chess board", "redraw": userStatus = redrawChessBoard();
                break;
            case "leave": userStatus = leave();
                break;
            case "makeMove", "make move", "move": if(userStatus!= INGAME_GAMEOVER || game.get().game().getGameOver()){userStatus = makeMove();}else{toTerminal(out,"You may not  make a move. Type \"leave\" to leave this game and go join another! ");}
                break;
            case "resign": if(userStatus!= INGAME_GAMEOVER){userStatus = resign();}else{toTerminal(out,"You may not resign. Type \"leave\" to leave this game and go join another! ");}
                break;
            case "highlight legal moves", "highlight": userStatus = highlightLegalMoves();
                break;
            default:
                toTerminal(out,"Unknown Request. Type \"help\" for a list of available commands.");
                break;
        }
        return userStatus;
    }



    private void help(){
        setHelpText(out);
        toTerminal(out,"\u2003" + "--help--" + "\u2003");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"Redraw Chess Board\" to refresh your screen and redraw the board");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"Leave\" to leave the game you are currently in");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"Make Move\" to perform a Chess move");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"Resign\" to surrender the game");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"Highlight Legal Moves\" to Highlight all possible Moves");
        out.println();
    }

    private UserStatus redrawChessBoard(){
        setHelpText(out);

        try{
            if(userStatus == INGAME_WHITE){
                DrawChess.drawBoardWhite(out, game.get().game().getBoard(),null);
            }
            else{
                DrawChess.drawBoardBlack(out, game.get().game().getBoard(), null);
            }
        } catch (Exception e) {
            toTerminal(out,"Redraw failed: " + e.getMessage());
            return userStatus;
        }
        return userStatus;
    }

    private UserStatus leave(){
        setHelpText(out);

        try{
            UserGameCommand command = new UserGameCommand(InteractiveUI.currentToken);
            command.setCommandType(UserGameCommand.CommandType.LEAVE);
            command.setGameID(game.get().gameID());
            websocket.send(new Gson().toJson(command));
            out.print(ERASE_SCREEN);

            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            for (GameData gameInList : listOfGames){
                if(Objects.equals(gameInList.gameID(), game.get().gameID())){
                    game.set(gameInList);
                }
            }



            return userStatus= LOGGEDIN;
        } catch (Exception e) {
            toTerminal(out,"Leave failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }

    private UserStatus makeMove(){
        ChessGame.TeamColor team;
        Scanner in = new Scanner(System.in);

        setHelpText(out);
        setHelpText(out);

        toTerminal(out,"Please enter move you would like to make -->");

        try {
            ChessPiece promtionPiece;
            if(userStatus == INGAME_WHITE){
            promtionPiece = new ChessPiece(ChessGame.TeamColor.WHITE, null);
            team = ChessGame.TeamColor.WHITE;
            }
            else{
                promtionPiece = new ChessPiece(ChessGame.TeamColor.BLACK, null);
                team= ChessGame.TeamColor.BLACK;
            }


            //get the start pos
            toTerminal(out, "Starting Position -->");
            ChessPosition start = getChessPosFromUserInput(in);
            if(game.get().game().getBoard().getPiece(start).getTeamColor() != team){
                throw new Exception("You can only move pieces on your team");
            }

            //get the End pos
            toTerminal(out, "End Position -->");
            ChessPosition end = getChessPosFromUserInput(in);

            if(game.get().game().getBoard().getPiece(start).getPieceType()==PAWN){
                promtionPiece = getPawnPromotionFromUser(out, promtionPiece, end);
            }
            else {
                if(userStatus == INGAME_WHITE){
                    promtionPiece = new ChessPiece(ChessGame.TeamColor.WHITE, null);
                }
                else{
                    promtionPiece = new ChessPiece(ChessGame.TeamColor.BLACK, null);
                }
            }


            ChessMove attemptedMove = new ChessMove(start, end, promtionPiece.getPieceType());
            UserGameCommand command = new MakeMoveCommand(InteractiveUI.currentToken, game.get().gameID(),attemptedMove);
            command.setCommandType(UserGameCommand.CommandType.MAKE_MOVE);
            command.setGameID(game.get().gameID());
            websocket.send(new Gson().toJson(command));

            return userStatus;
        } catch (Exception e) {
            toTerminal(out,"Move failed" + e.getMessage());
            return userStatus;

        }
    }

    private UserStatus resign() throws Exception {
        setHelpText(out);

        UserGameCommand command = new UserGameCommand(InteractiveUI.currentToken);
        command.setCommandType(UserGameCommand.CommandType.RESIGN);
        command.setGameID(game.get().gameID());
        websocket.send(new Gson().toJson(command));


        try{
            //do websocket stuff
            toTerminal(out,"You have Resigned. Game Over.");
            return INGAME_GAMEOVER;
        } catch (Exception e) {
            toTerminal(out,"Game join failed: " + e.getMessage());
            return userStatus;

        }
    }

    private UserStatus highlightLegalMoves	(){
        setHelpText(out);

        try{
            toTerminal(out, "Please enter the location of the piece you want highlighted -->");
            Scanner in = new Scanner(System.in);
            ChessPosition highlightedPiece = getChessPosFromUserInput(in);
            Collection<ChessMove> movesAvalible = game.get().game().validMoves(highlightedPiece);
            if(userStatus == INGAME_WHITE) {
                DrawChess.drawBoardWhite(out, game.get().game().getBoard(), movesAvalible);
            }
            else{
                DrawChess.drawBoardBlack(out, game.get().game().getBoard(), movesAvalible);
            }

            return userStatus;
        } catch (Exception e) {
            out.println("Highlighting Failed: " + e.getMessage());
            return userStatus;
        }
    }

    private ChessPosition getChessPosFromUserInput(Scanner in) throws Exception {
        String inputString = in.nextLine().trim().toUpperCase();
        if (inputString.length() != 2) {
            throw new Exception("Improper input, Please enter a location in the form \"A1\"");
        }

        char columnChar = inputString.charAt(0);
        int rowNumber;
        try {
            rowNumber = Integer.parseInt(inputString.substring(1));
        } catch (NumberFormatException e) {
            throw new Exception("Improper input, the row part must be a number.");
        }

        if (columnChar < 'A' || columnChar > 'H') {
            throw new Exception("Column must be between 'A' and 'H'");
        }

        if (rowNumber < 1 || rowNumber > 8) {
            throw new Exception("Row must be between 1 and 8");
        }

        return new ChessPosition(rowNumber, columnChar);
    }

    private ChessPiece getPawnPromotionFromUser(PrintStream out, ChessPiece promtionPiece, ChessPosition end) {
        ChessGame.TeamColor team;
        if (userStatus == INGAME_WHITE) {
            team = ChessGame.TeamColor.WHITE;
        }
        else {
            team = ChessGame.TeamColor.BLACK;
        }
        Scanner in = new Scanner(System.in);

        if (end.getRow() == 8 || end.getRow() == 1) {
            toTerminal(out, "What would you like to promote your Pawn to? (Knight, Rook, Queen, Bishop)");
            String promotion = in.nextLine().toUpperCase();
            switch (promotion) {
                case "KIGHT":
                    promtionPiece = new ChessPiece(team, KNIGHT);
                case "ROOK":
                    promtionPiece = new ChessPiece(team, ROOK);
                case "QUEEN":
                    promtionPiece = new ChessPiece(team, QUEEN);
                case "BISHOP":
                    promtionPiece = new ChessPiece(team, BISHOP);
                default:
                    toTerminal(out,"Invalid Piece Type. Avalible types are: Knight, Rook, Queen, Bishop");
                    getPawnPromotionFromUser(out, promtionPiece, end);
            }
        }
        return promtionPiece;
    }


}


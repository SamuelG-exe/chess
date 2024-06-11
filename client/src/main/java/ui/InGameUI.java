package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.GameData;
import request.JoinGameReq;
import response.ListGamesResp;
import uiutils.DrawChess;
import uiutils.UserStatus;
import web.ServerFacade;

import java.io.PrintStream;
import java.util.*;

import static uiutils.EscapeSequences.*;
import static ui.PreLoginUI.setHelpText;
import static uiutils.UserStatus.*;

public class InGameUI {
    private ServerFacade server;
    private String input;
    private UserStatus userStatus;
    private final PrintStream out;
    private ChessGame game;
    private int count;
    private boolean gameOver;

    InGameUI(ServerFacade server, String input, UserStatus userStatus, PrintStream out) {
        this.server = server;
        this.input = input;
        this.userStatus = userStatus;
        this.out = out;
        this.count = 1;
        this.game = userStatus.getGameData().game();
        this.gameOver = true;

        try {
            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            for (GameData game : listOfGames) {
                orderedMapOfGames.put(count, game);
                count++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public UserStatus run() {
        switch (input) {
            case "help": help();
                break;
            case "redraw chess board": redrawChessBoard();
                break;
            case "leave": userStatus = leave();
                break;
            case "makeMove": if(!gameOver){userStatus = makeMove();}else{toTerminal(out,"Game is over, you may not longer make a move. Type \"leave\" to leave this game and go join another! ");}
                break;
            case "resign": userStatus = resign();
                break;
            case "observe game": userStatus = observeGame();
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
        toTerminal(out,"Type \"logout\" to log out of the program");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"create game\" to create a new game");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"list games\" to list all the current games on the server");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"play game\" to play an existing game");
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"observe game\" to observe an existing game");
        out.println();
    }

    private void quit(){
        System.exit(0);
    }

    private UserStatus redrawChessBoard(){
        setHelpText(out);

        try{
            if(userStatus == INGAME_WHITE){
                DrawChess.drawBoardWhite(out, game.getBoard());
            }
            else{
                DrawChess.drawBoardBlack(out, game.getBoard());
            }
        } catch (Exception e) {
            toTerminal(out,"Redraw: " + e.getMessage());
            return userStatus;
        }
        return userStatus;
    }

    private UserStatus leave(){
        setHelpText(out);

        try{
            //update list og users connected to this game
            out.print(ERASE_SCREEN);
            return userStatus= LOGGEDIN;
        } catch (Exception e) {
            toTerminal(out,"Leave failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }

    private UserStatus makeMove(){
        Scanner in = new Scanner(System.in);

        setHelpText(out);
        setHelpText(out);

        toTerminal(out,"Please enter move you would like to make -->");

        try {
            //get the start pos
            toTerminal(out, "Starting Position -->");
            ChessPosition start =getChessPosFromUserInput(in);

            //get the End pos
            toTerminal(out, "End Position -->");
            ChessPosition end = getChessPosFromUserInput(in);


            ChessMove attemptedMove = new ChessMove(start, end, null);
            game.makeMove(attemptedMove);

            //send out updated chess data

            if(userStatus == INGAME_WHITE){
                DrawChess.drawBoardWhite(out, game.getBoard());
            }
            else{
                DrawChess.drawBoardBlack(out, game.getBoard());
            }

            return userStatus;
        } catch (Exception e) {
            toTerminal(out,"Move failed" + e.getMessage());
            return userStatus;

        }
    }

    private UserStatus Resign(){
        setHelpText(out);
        gameOver = true;

        try{
            //do websocket stuff
            return userStatus;
        } catch (Exception e) {
            toTerminal(out,"Game join failed: " + e.getMessage());
            return userStatus;

        }
    }

    private UserStatus observeGame	(){
        setHelpText(out);
        out.println("Please enter the ID number of the game you would like to Observe -->");
        Scanner in = new Scanner(System.in);
        String gameID = in.nextLine();


        try{
            GameData gametoWatch = orderedMapOfGames.get(Integer.parseInt(gameID));
            DrawChess.drawBoardBlack(out, gametoWatch.game().getBoard());
            DrawChess.drawBoardWhite(out, gametoWatch.game().getBoard());
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Game Listing failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }

    private ChessPosition getChessPosFromUserInput(Scanner in) throws Exception {
        String endString = in.nextLine().toString();
        if (endString.length() > 2) {
            throw new Exception("Improper input, Please enter a starting location in the form \"A\"\"1\"");
        }
        char columnEnd = endString.charAt(0);
        int rowEnd = Integer.parseInt(endString.substring(1));
        return new ChessPosition(rowEnd, columnEnd);
    }

}


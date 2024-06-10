package ui;

import model.GameData;
import request.CreateGameReq;
import request.JoinGameReq;
import response.CreateGameResp;
import response.ListGamesResp;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;
import static ui.PreLoginUI.setHelpText;

public class PostLoginUI {
    private ServerFacade server;
    private String input;
    private UserStatus userStatus;
    private final PrintStream out;
    Map<String,GameData> orderedMapOfGames = new HashMap<>();

    PostLoginUI(ServerFacade server, String input, UserStatus userStatus, PrintStream out) {
        this.server = server;
        this.input = input;
        this.userStatus = userStatus;
        this.out = out;

        try {
            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            for (GameData game : listOfGames) {
                orderedMapOfGames.put(game.gameID(), game);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public UserStatus run() {
            switch (input) {
                case "help": help();
                    break;
                case "logout": logOut();
                    break;
                case "create game": userStatus = createGame();
                    break;
                case "list games": userStatus = listGames();
                    break;
                case "play game": userStatus = playGame();
                    break;
                case "observe game": userStatus = observeGame();
                    break;
                default:
                    out.println("Unknown Request. Type \"help\" for a list of available commands.");
                    break;
            }
        return userStatus;
    }



    private void help(){
        setHelpText(out);
        out.print("\u2003" + "--help--" + "\u2003");
        out.println();

        setHelpText(out);
        out.print("Type \"logout\" to log out of the program");
        out.println();

        setHelpText(out);
        out.print("Type \"create game\" to create a new game");
        out.println();

        setHelpText(out);
        out.print("Type \"list games\" to list all the current games on the server");
        out.println();

        setHelpText(out);
        out.print("Type \"play game\" to play an existing game");
        out.println();

        setHelpText(out);
        out.print("Type \"observe game\" to observe an existing game");
        out.println();
    }

    private void quit(){
        System.exit(0);
    }

    private UserStatus logOut(){
        setHelpText(out);

        try{
            server.logOut(InteractiveUI.currentToken);
            InteractiveUI.currentToken = null;
            return userStatus=UserStatus.LOGGEDOUT;
        } catch (Exception e) {
            out.println("Logout failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }

    private UserStatus createGame(){
        setHelpText(out);
        out.println("Please enter a name for your game -->");
        Scanner in = new Scanner(System.in);
        String gameName = in.nextLine();

        CreateGameReq newGameReq = new CreateGameReq(gameName);
        try{
            CreateGameResp createdGame = server.createGame(newGameReq, InteractiveUI.currentToken);
            out.println("Congratulations! You have successfully Created a game (GameName: "+gameName +" GameID: " +createdGame.gameID()+"), happy dueling!");

            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            for (GameData game : listOfGames){
                orderedMapOfGames.put(game.gameID(), game);
            }

            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Game creation failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }

    private UserStatus listGames(){
        setHelpText(out);

        orderedMapOfGames.clear();

        try{
            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            for (GameData game : listOfGames){
                orderedMapOfGames.put(game.gameID(), game);
            }
            orderedMapOfGames.forEach((id, gameData) -> out.println(gameData.toString()));
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Game Listing failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }

    private UserStatus playGame(){
        setHelpText(out);
        out.println("Please enter the ID number of the game you would like to join -->");
        Scanner in = new Scanner(System.in);
        String gameID = in.nextLine();

        out.println("Perfect, now what team would you like to join? (White/Black) -->");
        String team = in.nextLine();

        String teamCAPS = team.toUpperCase();
        JoinGameReq joinGame = new JoinGameReq(teamCAPS,gameID);
        GameData gameToBeJoined = orderedMapOfGames.get(gameID);

        try{
            server.joinGame(joinGame, InteractiveUI.currentToken);
            out.println("Congratulations! You sucessfuly Joined the game "+gameID+". Good Luck!");
            if(teamCAPS.equals("BLACK")){
                DrawChess.drawBoardBlack(out, gameToBeJoined.game().getBoard());
            }
            else{
                DrawChess.drawBoardWhite(out, gameToBeJoined.game().getBoard());

            }
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Game join failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }

    private UserStatus observeGame	(){
        setHelpText(out);
        out.println("Please enter the ID number of the game you would like to Observe -->");
        Scanner in = new Scanner(System.in);
        String gameID = in.nextLine();


        try{
            GameData gametoWatch = orderedMapOfGames.get(gameID);
            DrawChess.drawBoardBlack(out, gametoWatch.game().getBoard());
            DrawChess.drawBoardWhite(out, gametoWatch.game().getBoard());
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Game Listing failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }

}


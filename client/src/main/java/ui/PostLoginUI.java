package ui;

import model.GameData;
import request.CreateGameReq;
import request.JoinGameReq;
import response.ListGamesResp;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class PostLoginUI {
    private ServerFacade server;
    private String input;
    private UserStatus userStatus;
    private final PrintStream out;
    Map<String,GameData> orderedMapOfGames = new HashMap<>();



    PostLoginUI(ServerFacade server, String input, UserStatus userStatus, PrintStream out){
        this.server = server;
        this.input = input;
        this.userStatus = userStatus;
        this.out = out;
    }

    public UserStatus run() {
        while (userStatus != UserStatus.LOGGEDOUT) {
            switch (input) {
                case "help": help();
                    break;
                case "logout": quit();
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

        }
        return userStatus;
    }



    private void help(){
        setHelpText(out);
        out.print("\u2003" + "--help--" + "\u2003");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"logout\" to log out of the program");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"create game\" to create a new game");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"list games\" to list all the current games on the server");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"play game\" to play an existing game");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"observe game\" to observe an existing game");
        setBlack(out);
        out.println();
    }

    private void quit(){
        System.exit(0);
    }

    private UserStatus Logout(){
        setHelpText(out);

        try{
            server.logOut();
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
            server.createGame(newGameReq);
            out.println("Congratulations! You have successfully Created a game (" +gameName+"), happy dueling!");
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
            ListGamesResp listOfGamesResp = server.listGames();
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
        JoinGameReq joinGame = new JoinGameReq(gameID,teamCAPS);
        try{
            server.joinGame(joinGame);
            out.println("Congratulations! You sucessfuly Joined the game "+gameID+". Good Luck!");
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
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Game Listing failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }





    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setHelpText(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}


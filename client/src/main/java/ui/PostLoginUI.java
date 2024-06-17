package ui;

import com.google.gson.Gson;
import model.GameData;
import request.CreateGameReq;
import request.JoinGameReq;
import response.CreateGameResp;
import response.ListGamesResp;
import uiutils.DrawChess;
import uiutils.UserStatus;
import web.ServerFacade;
import web.ServerMessageObserver;
import web.WebSocketFacade;
import websocket.commands.UserGameCommand;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static uiutils.EscapeSequences.*;
import static uiutils.EscapeSequences.SET_TEXT_COLOR_WHITE;
import static ui.PreLoginUI.setHelpText;

public class PostLoginUI {
    private ServerFacade server;
    private String input;
    private UserStatus userStatus;
    private final PrintStream out;
    private int count;
    private AtomicReference<GameData> gameData;
    private WebSocketFacade websocket;
    Map<Integer,GameData> orderedMapOfGames = new HashMap<>();

    PostLoginUI(ServerFacade server, String input, UserStatus userStatus, PrintStream out, AtomicReference<GameData> gameData, WebSocketFacade websocket) {
        this.server = server;
        this.input = input;
        this.userStatus = userStatus;
        this.out = out;
        this.count = 1;
        this.gameData=gameData;
        this.websocket= websocket;

        try {
            orderedMapOfGames.clear();
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

    private UserStatus logOut(){
        setHelpText(out);

        try{
            server.logOut(InteractiveUI.currentToken);
            InteractiveUI.currentToken = null;
            gameData.set(null);
            return userStatus=UserStatus.LOGGEDOUT;
        } catch (Exception e) {
            toTerminal(out,"Logout failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }

    private UserStatus createGame(){


        setHelpText(out);
        toTerminal(out,"Please enter a name for your game -->");
        Scanner in = new Scanner(System.in);
        String gameName = in.nextLine();

        CreateGameReq newGameReq = new CreateGameReq(gameName);
        try{
            int thisGameID =-1;
            CreateGameResp createdGame = server.createGame(newGameReq, InteractiveUI.currentToken);
            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            count = 1;
            for (GameData game : listOfGames) {
                orderedMapOfGames.put(count, game);
                if (Objects.equals(game.gameID(), createdGame.gameID())){thisGameID = count;}
                count++;
            }

            toTerminal(out,"Congratulations! You have successfully Created a game (GameName: "+gameName +", GameID: " +thisGameID+"), happy dueling!");


            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            toTerminal(out,"Game creation failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }

    private UserStatus listGames(){
        setHelpText(out);

        orderedMapOfGames.clear();
        count = 1;

        try{
            ListGamesResp listOfGamesResp = server.listGames(InteractiveUI.currentToken);
            List<GameData> listOfGames = listOfGamesResp.games();
            for (GameData game : listOfGames){
                orderedMapOfGames.put(count, game);
                count++;
            }
            orderedMapOfGames.forEach((id, gameData) -> {
                if (!gameData.game().getGameOver()) {
                    toTerminal(out,
                            "Game ID: " + id +
                                    ", Game Name: " + gameData.gameName() +
                                    ", White Player: " + (gameData.whiteUsername() != null ? gameData.whiteUsername() : SET_TEXT_COLOR_GREEN+"{ AVAILABLE }"+SET_TEXT_COLOR_WHITE)+
                                    ", Black Player: " + (gameData.blackUsername() != null ? gameData.blackUsername() : SET_TEXT_COLOR_GREEN+"{ AVAILABLE }"+SET_TEXT_COLOR_WHITE));
                } else {
                    String winner = gameData.game().getWhoWonPlayerName();
                    toTerminal(out,
                            "Game ID: " + id + " Game Over! Winner: " +SET_TEXT_COLOR_ORANGE+ (winner != null ? winner : "TIE"+SET_TEXT_COLOR_WHITE)
                    );
                }
            });
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            toTerminal(out,"Game Listing failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }

    private UserStatus playGame(){
        setHelpText(out);

        toTerminal(out,"Please enter the ID number of the game you would like to join -->");
        try {
            Scanner in = new Scanner(System.in);
            String gameID = in.nextLine().toString();
            GameData gameToBeJoined = orderedMapOfGames.get(Integer.parseInt(gameID));
            String blackplayer = gameToBeJoined.blackUsername();
            String whitePlayer = gameToBeJoined.whiteUsername();

            if (blackplayer == null) {
                blackplayer = SET_TEXT_COLOR_GREEN+"{ AVAILABLE }"+SET_TEXT_COLOR_WHITE;
            }
            if (whitePlayer == null) {
                whitePlayer = SET_TEXT_COLOR_GREEN+"{ AVAILABLE }"+SET_TEXT_COLOR_WHITE;
            }


            toTerminal(out, "Perfect, now what team would you like to join? White = \"" + whitePlayer + "\" Black = \"" + blackplayer + "\"-->");
            String team = in.nextLine();

            String teamCAPS = team.toUpperCase();
            JoinGameReq joinGame = new JoinGameReq(teamCAPS, gameToBeJoined.gameID());


            server.joinGame(joinGame, InteractiveUI.currentToken);
            toTerminal(out, "Congratulations! You sucessfuly Joined the game " + gameID + ". Good Luck!");
            gameData.set(gameToBeJoined);

            UserGameCommand command = new UserGameCommand(InteractiveUI.currentToken);
            command.setCommandType(UserGameCommand.CommandType.CONNECT);
            command.setGameID(gameToBeJoined.gameID());
            websocket.send(new Gson().toJson(command));

            if (teamCAPS.equals("BLACK")) {
                //DrawChess.drawBoardBlack(out, gameToBeJoined.game().getBoard(), null);
                return userStatus = UserStatus.INGAME_BLACK;
            } else {
                //DrawChess.drawBoardWhite(out, gameToBeJoined.game().getBoard(), null);
                return userStatus = UserStatus.INGAME_WHITE;
            }

        } catch (Exception e) {
            toTerminal(out,"Game join failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;

        }
    }

    private UserStatus observeGame	(){
        setHelpText(out);
        toTerminal(out,"Please enter the ID number of the game you would like to Observe -->");
        try {
            Scanner in = new Scanner(System.in);
            out.print(SET_TEXT_BOLD+SET_TEXT_COLOR_WHITE+">>>");
            String gameID = in.nextLine();


            GameData gametoWatch = orderedMapOfGames.get(Integer.parseInt(gameID));

            UserGameCommand command = new UserGameCommand(InteractiveUI.currentToken);
            command.setCommandType(UserGameCommand.CommandType.CONNECT);
            command.setGameID(gametoWatch.gameID());
            websocket.send(new Gson().toJson(command));


            return userStatus = UserStatus.INGAME_GAMEOVER;
        } catch (Exception e) {
            out.println("Game Listing failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDIN;
        }
    }
}


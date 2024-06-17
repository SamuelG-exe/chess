package ui;

import com.google.gson.Gson;
import model.GameData;
import uiutils.DrawChess;
import uiutils.UserStatus;
import web.ServerFacade;
import web.WebSocketFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import web.ServerMessageObserver;
import websocket.messages.ServerMessageError;
import websocket.messages.ServerMessageLoadGme;
import websocket.messages.ServerMessageNotification;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static uiutils.EscapeSequences.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;


public class InteractiveUI {
    public static String currentToken;
    public static UserStatus userStatus;
    static AtomicReference<GameData> gameData = new AtomicReference<>();

    private static final ServerMessageObserver notificationManager = new ServerMessageObserver() {
        @Override
        public void notify(String message) {
            ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

            switch (serverMessage.getServerMessageType()) {


                case NOTIFICATION -> {
                    ServerMessageNotification notification = new Gson().fromJson(message, ServerMessageNotification.class);
                    toTerminal(new PrintStream(System.out, true, StandardCharsets.UTF_8), notification.getMessageText());
                }
                case ERROR -> {
                    ServerMessageError errorNotification = new Gson().fromJson(message, ServerMessageError.class);
                    toTerminalError(new PrintStream(System.out, true, StandardCharsets.UTF_8), errorNotification.getMessageText());
                }
                case LOAD_GAME -> {
                    ServerMessageLoadGme loadGame = new Gson().fromJson(message, ServerMessageLoadGme.class);

                    if (userStatus == UserStatus.INGAME_WHITE) {
                        gameData.set(loadGame.getGameData());
                        DrawChess.drawBoardWhite(new PrintStream(System.out, true, StandardCharsets.UTF_8), loadGame.getGameData().game().getBoard(), null);
                    } else if (userStatus == UserStatus.INGAME_BLACK) {
                        gameData.set(loadGame.getGameData());
                        DrawChess.drawBoardBlack(new PrintStream(System.out, true, StandardCharsets.UTF_8), loadGame.getGameData().game().getBoard(), null);
                    } else {
                        gameData.set(loadGame.getGameData());
                        DrawChess.drawBoth(new PrintStream(System.out, true, StandardCharsets.UTF_8), loadGame.getGameData().game().getBoard(), null);
                    }
                }

            }
        }
    };

    private static WebSocketFacade webSocketFacade;

    static {
        try {
            webSocketFacade = new WebSocketFacade("localhost:8080", notificationManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        var serverUrl = "http://localhost:8080";


        ServerFacade server = new ServerFacade(serverUrl);
        userStatus = UserStatus.LOGGEDOUT;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(System.in);

        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_WHITE);

        out.println("*******************************************************************************************************************************");
        out.println(BLACK_QUEEN + "                                                    Welcome To Chess                                                    " + BLACK_QUEEN);
        out.println("*******************************************************************************************************************************\n\n");


        while (true) {

            while (userStatus == UserStatus.LOGGEDOUT) {
                out.print(SET_BG_COLOR_BLUE);
                out.print(SET_TEXT_COLOR_BLACK);
                toTerminal(out, "Enter a command (help, quit, login, register): ");
                String input = scanner.nextLine().toString().toLowerCase();
                PreLoginUI preLoginUI = new PreLoginUI(server, input, userStatus, out);
                userStatus = preLoginUI.run();
            }

            while (userStatus == UserStatus.LOGGEDIN) {
                out.print(SET_BG_COLOR_BLUE);
                out.print(SET_TEXT_COLOR_BLACK);
                toTerminal(out, "Enter a command (help, logout, create game, list games, play game, observe game): ");

                String input = scanner.nextLine().toLowerCase();
                PostLoginUI postLoginUI = new PostLoginUI(server, input, userStatus, out, gameData, webSocketFacade);
                userStatus = postLoginUI.run();
            }

            while (userStatus == UserStatus.INGAME_WHITE || (userStatus == UserStatus.INGAME_BLACK) || (userStatus == UserStatus.INGAME_GAMEOVER)) {
                out.print(SET_BG_COLOR_BLUE);
                out.print(SET_TEXT_COLOR_BLACK);
                toTerminal(out, "Enter a command (help, Redraw Chess Board, Leave, Make Move, Resign, Highlight Legal Moves): ");

                String input = scanner.nextLine().toLowerCase();
                InGameUI inGameUI = new InGameUI(server, input, userStatus, out, gameData, webSocketFacade);
                userStatus = inGameUI.run();
            }
        }
    }
}


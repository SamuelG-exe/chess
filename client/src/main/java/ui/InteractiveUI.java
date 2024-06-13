package ui;

import model.GameData;
import uiutils.UserStatus;
import web.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static uiutils.EscapeSequences.*;


public class InteractiveUI {
    public static String currentToken;
    public static UserStatus userStatus;
    private static AtomicReference<GameData> gameData;

    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";

        gameData = new AtomicReference<>();

        ServerFacade server = new ServerFacade(serverUrl);
        userStatus = UserStatus.LOGGEDOUT;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(System.in);

        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_WHITE);

        out.println("*******************************************************************************************************************************");
        out.println(BLACK_QUEEN + "                                                    Welcome To Chess                                                    " + BLACK_QUEEN);
        out.println("*******************************************************************************************************************************\n\n");


        while(true) {

            while (userStatus == UserStatus.LOGGEDOUT) {
                out.print(SET_BG_COLOR_BLUE);
                out.print(SET_TEXT_COLOR_BLACK);
                toTerminal(out,"Enter a command (help, quit, login, register): ");
                String input = scanner.nextLine().toString().toLowerCase();
                PreLoginUI preLoginUI = new PreLoginUI(server, input, userStatus, out);
                userStatus = preLoginUI.run();
            }

            while (userStatus == UserStatus.LOGGEDIN) {
                out.print(SET_BG_COLOR_BLUE);
                out.print(SET_TEXT_COLOR_BLACK);
                toTerminal(out,"Enter a command (help, logout, create game, list games, play game, observe game): ");

                String input = scanner.nextLine().toLowerCase();
                PostLoginUI postLoginUI = new PostLoginUI(server, input, userStatus, out, gameData);
                userStatus = postLoginUI.run();
            }

            while (userStatus == UserStatus.INGAME_WHITE || (userStatus == UserStatus.INGAME_BLACK) ||(userStatus == UserStatus.INGAME_GAMEOVER)) {
                out.print(SET_BG_COLOR_BLUE);
                out.print(SET_TEXT_COLOR_BLACK);
                toTerminal(out,"Enter a command (help, Redraw Chess Board, Leave, Make Move, Resign, Highlight Legal Moves): ");

                String input = scanner.nextLine().toLowerCase();
                InGameUI inGameUI = new InGameUI(server, input, userStatus, out, gameData);
                userStatus = inGameUI.run();
            }
        }
    }
}

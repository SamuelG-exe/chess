package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class InteractiveUI {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";

        ServerFacade server = new ServerFacade(serverUrl);
        UserStatus userStatus = UserStatus.LOGGEDOUT;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Scanner scanner = new Scanner(System.in);



        while (userStatus == UserStatus.LOGGEDOUT) {
            out.print("Enter a command (help, quit, login, register): ");
            String input = scanner.nextLine().toLowerCase();
            PreLoginUI preLoginUI = new PreLoginUI(server, input, userStatus, out);
            userStatus=preLoginUI.run();
        }

        while (userStatus == UserStatus.LOGGEDIN) {
            out.print("Enter a command (help, logout, create game, list games, play game, observe game): ");
            String input = scanner.nextLine().toLowerCase();
            PostLoginUI postLoginUI = new PostLoginUI(server,input,userStatus,out);
            userStatus=postLoginUI.run();
        }
    }
}

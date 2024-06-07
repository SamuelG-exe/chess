package ui;

import request.LoginReq;
import request.RegisterReq;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;


import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;

public class PreLoginUI {
    private ServerFacade server;
    private String input;
    private UserStatus userStatus;
    private final PrintStream out;


    PreLoginUI(ServerFacade server, String input, UserStatus userStatus, PrintStream out){
        this.server = server;
        this.input = input;
        this.userStatus = userStatus;
        this.out = out;
    }

    public UserStatus run() {
        switch (input) {
            case "help":
                userStatus = help();
                break;
            case "quit":
                quit();
                break;
            case "login":
                userStatus = logIn();
                break;
            case "register":
                userStatus = register();
                break;
            default:
                setHelpText(out);
                out.println("Unknown Request. Type \"help\" for a list of available commands.");
                break;
        }
        return userStatus;
    }



    private UserStatus help(){
        int longetString ="Type \\\"register\\\" to create a new account with a unique username, password, and email".length();
        int helpLength = "----help----".length();
        int sizeOfHelp = longetString - helpLength;
        int buffer = sizeOfHelp/2;



        setHelpText(out);
        out.print("          ----help---- ");
        for(int i = 0; i <= buffer; i++){
            out.print("\u2003");
        }
        setBlack(out);
        out.println();

        setHelpText(out);
        buffer =( longetString - "Type \"quit\" to exit the program".length())/2;
        out.print("Type \"quit\" to exit the program       ");
        for(int i = 0; i <= buffer; i++){
            out.print("\u2003");
        }
        setBlack(out);
        out.println();

        setHelpText(out);
        buffer =( longetString - "Type \"login\" to Login by imputing your UserName and Password".length())/2;
        out.print("Type \"login\" to Login by imputing your UserName and Password  ");
        for(int i = 0; i <= buffer; i++){
            out.print("\u2003");
        }
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"register\" to create a new account with a unique username, password, and email");
        setBlack(out);
        out.println();

        return userStatus = UserStatus.LOGGEDOUT;
    }

    private void quit(){
        System.exit(0);
    }

    private UserStatus logIn(){
        setHelpText(out);
        out.println("Please enter your unique Username -->");
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();

        out.println("Please enter your unique Password -->");
        String passWord = in.nextLine();
        LoginReq loginRequest = new LoginReq(userName, passWord);
        try{
            server.login(loginRequest);
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Login failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDOUT;
        }
    }

    private UserStatus register(){
        setHelpText(out);
        out.println("Please enter your unique Username -->");
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();

        out.println("Please enter your unique Password -->");
        String passWord = in.nextLine();

        out.println("Please enter your unique Email -->");
        String email = in.nextLine();


        RegisterReq registerReq = new RegisterReq(userName, passWord, email);
        try{
            server.register(registerReq);
            out.println("Congratulations! You have successfully registered, go forth and serve a buttkicking!");
            out.println("LOGGED IN AS: "+userName);
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            out.println("Registration failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDOUT;

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

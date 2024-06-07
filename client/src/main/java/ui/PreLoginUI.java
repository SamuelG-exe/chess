package ui;

import request.LoginReq;

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
        while(userStatus==UserStatus.LOGGEDOUT) {
            switch (input) {
                case -> "help"; //call function
                case -> "quit"
                case -> "login"
                case -> "register"
            }

        }
    }

    private void help(){
        setHelpText(out);
        out.print("\u2003"+ "--help--"+ "\u2003");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"quit\" to exit the program");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"login\" to Login by inputing your UserName and Password");
        setBlack(out);
        out.println();

        setHelpText(out);
        out.print("Type \"register\" to create a new account with a unique username, password, and email");
        setBlack(out);
        out.println();
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
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
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

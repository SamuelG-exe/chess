package ui;

import request.LoginReq;
import request.RegisterReq;
import response.LoginResp;
import response.RegisterResp;
import uiutils.UserStatus;
import web.ServerFacade;

import java.io.PrintStream;
import java.util.Scanner;


import static uiutils.EscapeSequences.*;

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
        toTerminal(out,"          ----help---- ");
        for(int i = 0; i <= buffer; i++){
            out.print("\u2003");
        }
        out.println();

        setHelpText(out);
        buffer =( longetString - "Type \"quit\" to exit the program".length())/2;
        toTerminal(out,"Type \"quit\" to exit the program       ");
        for(int i = 0; i <= buffer; i++){
            out.print("\u2003");
        }
        out.println();

        setHelpText(out);
        buffer =( longetString - "Type \"login\" to Login by imputing your UserName and Password".length())/2;
        toTerminal(out,"Type \"login\" to Login by imputing your UserName and Password  ");
        for(int i = 0; i <= buffer; i++){
            out.print("\u2003");
        }
        out.println();

        setHelpText(out);
        toTerminal(out,"Type \"register\" to create a new account with a unique username, password, and email");
        out.println();

        return userStatus = UserStatus.LOGGEDOUT;
    }

    private void quit(){
        System.exit(0);
    }

    private UserStatus logIn(){
        setHelpText(out);
        toTerminal(out,"Please enter your unique Username -->");
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();


        toTerminal(out,"Please enter your unique Password -->");
        String passWord = in.nextLine();
        LoginReq loginRequest = new LoginReq(userName, passWord);
        try{
            LoginResp logedIn = server.login(loginRequest);
            toTerminal(out,"Congratulations! You have successfully logged in!");
            toTerminal(out,"LOGGED IN AS: "+userName);
            InteractiveUI.currentToken = logedIn.authToken();
            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            toTerminal(out,"Login failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDOUT;
        }
    }

    private UserStatus register(){
        setHelpText(out);
        toTerminal(out,"Please enter your unique Username -->");
        Scanner in = new Scanner(System.in);
        String userName = in.nextLine();

        toTerminal(out,"Please enter your unique Password -->");
        String passWord = in.nextLine();

        toTerminal(out,"Please enter your unique Email -->");
        String email = in.nextLine();


        RegisterReq registerReq = new RegisterReq(userName, passWord, email);
        try{
            RegisterResp response = server.register(registerReq);
            InteractiveUI.currentToken = response.authToken();
            toTerminal(out,"Congratulations! You have successfully registered, go forth and serve a buttkicking!");
            toTerminal(out,"LOGGED IN AS: "+userName);

            return userStatus=UserStatus.LOGGEDIN;
        } catch (Exception e) {
            toTerminal(out,"Registration failed: " + e.getMessage());
            return userStatus = UserStatus.LOGGEDOUT;

        }
    }
        public static void setHelpText(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}

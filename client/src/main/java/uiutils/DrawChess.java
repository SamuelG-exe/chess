package uiutils;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static uiutils.EscapeSequences.*;


public class DrawChess {
    static ChessBoard gameBoardInternal;
    private static final String EMPTY = EscapeSequences.EMPTY;


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


        //clear
        //out.print(ERASE_SCREEN);
        out.println();

        drawBoardWhite(out, gameBoardInternal, null);

        out.println();

        drawBoardBlack(out, gameBoardInternal, null);


    }
    public static void drawBoth(PrintStream out, ChessBoard gameBoard, Collection<ChessMove> possibleMoves) {
        out.println();
        drawBoardWhite(out, gameBoard, possibleMoves);
        out.println();
        drawBoardBlack(out, gameBoard, possibleMoves);
        out.println();
    }




        public static void drawBoardWhite(PrintStream out, ChessBoard gameBoard, Collection<ChessMove> possibleMoves) {
        gameBoardInternal = gameBoard;
        out.println();
        drawHeaderWhite(out);
        int count = 8;

        for (int i = 8; i >= 1; i--) {
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(SET_BG_COLOR_ALMOSTBLACK);
            out.print("\u2003" + Integer.toString(count) + "\u2003");
            for (int j = 1; j <= 8; j++) {
                drawBoardHelper(out, i, j, possibleMoves);

            }

            out.print(SET_BG_COLOR_ALMOSTBLACK);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print("\u2003" + Integer.toString(count) + "\u2003");
            count--;
            setBlack(out);
            out.println();
        }
        drawHeaderWhite(out);
    }

    public static void drawBoardBlack(PrintStream out, ChessBoard gameBoard, Collection<ChessMove> possibleMoves) {
        gameBoardInternal = gameBoard;
        out.println();
        drawHeaderBlack(out);
        int count = 1;

        for (int i = 1; i <= 8; i++) {
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(SET_BG_COLOR_ALMOSTBLACK);
            out.print("\u2003" + Integer.toString(count) + "\u2003");
            for (int j = 8; j >= 1; j--) {
                  drawBoardHelper(out, i, j, possibleMoves);

            }

            out.print(SET_BG_COLOR_ALMOSTBLACK);
            out.print(SET_TEXT_COLOR_WHITE);
            out.print("\u2003" + Integer.toString(count) + "\u2003");
            count++;
            setBlack(out);
            out.println();
        }
        drawHeaderBlack(out);
    }

    private static void drawBoardHelper(PrintStream out, int i, int j, Collection<ChessMove> possibleMoves) {
        ChessPosition cord = new ChessPosition(i, j);
        ChessPosition start = null;

        ChessPiece thisPiece = gameBoardInternal.getPiece(cord);
        String stringOfPiece;
        ChessGame.TeamColor teamColor = null;

        if (thisPiece == null) {
            stringOfPiece = EMPTY;
        } else {
            teamColor = thisPiece.getTeamColor();
            stringOfPiece = switch (thisPiece.getPieceType()) {
                case KING -> BLACK_KING;
                case QUEEN -> BLACK_QUEEN;
                case BISHOP -> BLACK_BISHOP;
                case KNIGHT -> BLACK_KNIGHT;
                case ROOK -> BLACK_ROOK;
                case PAWN -> BLACK_PAWN;
            };
        }
        if(possibleMoves != null) {
            Collection<ChessPosition> endLocations = new ArrayList<>();
            for (ChessMove move : possibleMoves) {
                endLocations.add(move.getEndPosition());
                start=move.getStartPosition();
            }
            if(cord.equals(start)){
                printSquareYellow(out, stringOfPiece, teamColor);
            }
            else if (endLocations.contains(cord)) {
                if ((i + j) % 2 == 1) {
                    printSquareGreen(out, stringOfPiece, teamColor);
                } else {
                    printSquareDarkGreen(out, stringOfPiece, teamColor);
                }
            } else if ((i + j) % 2 == 1) {
                printSquareWhite(out, stringOfPiece, teamColor);
            } else {
                printSquareBlack(out, stringOfPiece, teamColor);
            }
        }
        else {
            if ((i + j) % 2 == 1) {
                printSquareWhite(out, stringOfPiece, teamColor);
            } else {
                printSquareBlack(out, stringOfPiece, teamColor);
            }

        }
    }



    private static void drawHeaderWhite(PrintStream out) {
        out.print(RESET_TEXT_BOLD_FAINT);
        String[] headers = {"A", "B", "C", "D", "E", "F", "G", "H"};
        drawHeader(out, headers);

        setBlack(out);
        out.println();
    }

    private static void drawHeaderBlack(PrintStream out) {
        String[] headers = {"H", "G", "F", "E", "D", "C", "B", "A"};
        out.print(RESET_TEXT_BOLD_FAINT);
        drawHeader(out, headers);

        setBlack(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String[] headers) {
        out.print(ERASE_LINE);

        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_ALMOSTBLACK);

        out.print("\u2003" + "*" + "\u2003");

        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[0] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[1] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[2] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[3] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+headers[4] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[5] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[6] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");
        System.out.print("\u200A"+"\u200A"+"\u200A"+"\u200A"+ headers[7] +"\u200A"+"\u200A"+"\u200A"+"\u200A"+"\u200A");

        out.print("\u2003" + "*" + "\u2003");
    }

    private static void printSquareWhite(PrintStream out, String piece, ChessGame.TeamColor teamColor) {
        out.print(SET_BG_COLOR_LIGHT_BROWN);
        setPieceColor(out, piece, teamColor);
    }

    private static void printSquareBlack(PrintStream out, String piece, ChessGame.TeamColor teamColor) {
        out.print(SET_BG_COLOR_DARK_BROWN);
        setPieceColor(out, piece, teamColor);
    }

    private static void printSquareGreen(PrintStream out, String piece, ChessGame.TeamColor teamColor) {
        out.print(SET_BG_COLOR_GREEN);
        setPieceColor(out, piece, teamColor);
    }
    private static void printSquareDarkGreen(PrintStream out, String piece, ChessGame.TeamColor teamColor) {
        out.print(SET_BG_COLOR_DARK_GREEN_BETTER);
        setPieceColor(out, piece, teamColor);
    }
    private static void printSquareYellow(PrintStream out, String piece, ChessGame.TeamColor teamColor) {
        out.print(SET_BG_COLOR_YELLOW);
        setPieceColor(out, piece, teamColor);
    }


    private static void setPieceColor(PrintStream out, String piece, ChessGame.TeamColor teamColor) {
        if(teamColor == ChessGame.TeamColor.WHITE){
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(piece);
        }
        else{
            out.print(SET_TEXT_COLOR_BLACK);
            out.print(piece);}
    }
    
    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}



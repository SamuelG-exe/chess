package websocket.commands;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{

    private ChessMove move;


    public MakeMoveCommand(String authToken, String gameID, ChessMove move) {
        super(authToken);
        setGameID(gameID);
        this.move=move;

    }
    public ChessMove getMove(){return this.move;}


}

package uiutils;

import chess.ChessGame;
import model.GameData;

public enum UserStatus {
    LOGGEDOUT,
    LOGGEDIN,
    INGAME_WHITE(),
    INGAME_BLACK();

    private GameData gameData;

    public GameData getGameData() {
        return gameData;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
}

package uiutils;

import chess.ChessGame;
import model.GameData;
import ui.InGameUI;

public enum UserStatus {
    LOGGEDOUT,
    LOGGEDIN,
    INGAME_WHITE(),
    INGAME_BLACK(),
    INGAME_GAMEOVER;

}

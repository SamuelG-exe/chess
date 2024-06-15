package websocket.messages;

import model.GameData;

public class ServerMessageLoadGme extends ServerMessage{

    private GameData game;
    public ServerMessageLoadGme(GameData gameData) {
        super(ServerMessageType.LOAD_GAME);
        this.game=gameData;
    }
    public GameData getGameData(){
        return this.game;
    }
}

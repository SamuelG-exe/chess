package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;
import request.RegisterReq;
import response.RegisterResp;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    static ServerFacade facade;

    @BeforeAll
    public static void init()  {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    void resetDB() throws Exception {
        facade.ClearDB();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerPos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp registerResp = facade.register(registerReq);
        assertNotNull(registerResp);
        assertEquals(registerReq.username(), registerResp.username());
    }
    @Test
    void registerNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq(null, "password", "email");
        assertThrows(Exception.class, () -> {
            RegisterResp registerResp = facade.register(registerReq);
        });
    }

    @Test
    void LoginPos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp registerResp = facade.register(registerReq);
        assertNotNull(registerResp);
        assertEquals(registerReq.username(), registerResp.username());
    }


    @Test
    void clearTest() throws Exception {


    }
}

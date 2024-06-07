package client;

import org.junit.jupiter.api.*;
import request.CreateGameReq;
import request.JoinGameReq;
import request.LoginReq;
import request.RegisterReq;
import response.CreateGameResp;
import response.ListGamesResp;
import response.LoginResp;
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
        facade = new ServerFacade(Integer.toString(port));
    }

    @BeforeEach
    void resetDB() throws Exception {
        facade.ClearDataBase();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void testRegisterPos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp registerResp = facade.register(registerReq);
        assertNotNull(registerResp);
        assertEquals(registerReq.username(), registerResp.username());
    }
    @Test
    void testRegisterNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq(null, "password", "email");
        assertThrows(Exception.class, () -> {
            RegisterResp registerResp = facade.register(registerReq);
        });
    }

    @Test
    void testLoginPos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        facade.register(registerReq);

        LoginReq login = new LoginReq("username", "password");
        LoginResp loggedIn = facade.login(login);

        assertNotNull(loggedIn);
        assertEquals(loggedIn.username(), login.username());
    }

    @Test
    void testLoginNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        facade.register(registerReq);

        LoginReq login = new LoginReq("username", "WrongPassword");
        assertThrows(Exception.class, () -> {
            LoginResp loggedIn = facade.login(login);
        });
    }

    @Test
    void testCreateGamePos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        CreateGameResp newGame = facade.createGame(makegame, token);
        ListGamesResp listGamesResp = facade.listGames(token);

        assertEquals(newGame.gameID(), listGamesResp.games().getFirst().gameID());
    }

    @Test
    void testCreateGameNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        CreateGameResp newGame = facade.createGame(makegame, token);
        assertThrows(Exception.class, () -> {
            CreateGameResp newGameAgain = facade.createGame(makegame, token);
        });
    }

    @Test
    void testListGamesPos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        facade.createGame(makegame, token);

        ListGamesResp listGamesResp = facade.listGames(token);

        assertEquals(listGamesResp.games().size(), 1);
    }

    @Test
    void testListGamesNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        String wrongToken = "lolNope";
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        facade.createGame(makegame, token);

        assertThrows(Exception.class, () -> {
            facade.listGames(wrongToken);
        });
    }

    @Test
    void testJoinGamePos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        CreateGameResp newGame = facade.createGame(makegame, token);
        ListGamesResp listGamesResp = facade.listGames(token);


        JoinGameReq join = new JoinGameReq("WHITE", newGame.gameID());

        assertEquals("username", listGamesResp.games().getFirst().whiteUsername());
    }

    @Test
    void testJoinGameNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        CreateGameResp newGame = facade.createGame(makegame, token);
        ListGamesResp listGamesResp = facade.listGames(token);

        assertThrows(Exception.class, () -> {
            JoinGameReq join = new JoinGameReq("TACO", newGame.gameID());

        });

    }



    @Test
    void testClear() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        facade.createGame(makegame, token);

        ListGamesResp listGamesResp = facade.listGames(token);

        assertEquals(listGamesResp.games().size(), 1);
        facade.ClearDataBase();
        assertEquals(listGamesResp.games().size(), 0);

    }
}

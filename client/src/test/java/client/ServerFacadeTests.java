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
import web.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;

    static ServerFacade facade;

    @BeforeAll
    public static void init()  {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:"+port);
    }

    @BeforeEach
    void resetDB() throws Exception {
        facade.clearDataBase();
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
    void testLogOutPos() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();

        LoginReq login = new LoginReq("username", "password");
        LoginResp loggedIn = facade.login(login);
        facade.logOut(token);

        assertThrows(Exception.class, () -> {
            facade.listGames(token);
        });
    }

    @Test
    void testLogOutNeg() throws Exception {
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        String wrongtoken = "shouldntWork";
        LoginReq login = new LoginReq("username", "password");
        LoginResp loggedIn = facade.login(login);


        assertThrows(Exception.class, () -> {
            facade.logOut(wrongtoken);
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
            CreateGameResp newGameAgain = facade.createGame(makegame, "bananna");
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
        facade.clearDataBase();
        RegisterReq registerReq = new RegisterReq("username", "password", "email");
        RegisterResp resp = facade.register(registerReq);
        String token = resp.authToken();
        LoginReq login = new LoginReq("username", "password");
        facade.login(login);

        CreateGameReq makegame = new CreateGameReq("newGame");
        CreateGameResp newGame = facade.createGame(makegame, token);



        JoinGameReq join = new JoinGameReq("WHITE", newGame.gameID());
        facade.joinGame(join, token);
        ListGamesResp listGamesResp = facade.listGames(token);

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
            facade.joinGame(join, token);

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
        facade.clearDataBase();
        registerReq = new RegisterReq("username", "password", "email");
        resp = facade.register(registerReq);
        token = resp.authToken();
        ListGamesResp listGamesResp2 = facade.listGames(token);
        assertEquals(listGamesResp2.games().size(), 0);

    }
}

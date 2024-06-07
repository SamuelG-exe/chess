package ui;


import com.google.gson.Gson;
import model.AuthData;
import request.CreateGameReq;
import request.JoinGameReq;
import request.LoginReq;
import request.RegisterReq;
import response.*;

import java.io.*;
import java.net.*;
import java.util.List;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void ClearDB() throws Exception{
        this.makeRequest("DELETE", "/db",null, null, null);
    }
    public RegisterResp register(RegisterReq newUser) throws Exception {
        return this.makeRequest("POST", "/user", newUser, RegisterResp.class, null);
    }

    public LoginResp login(LoginReq returningUser) throws Exception {
        return this.makeRequest("POST", "/session", returningUser, LoginResp.class, null);
    }

    public void logOut(String authToken) throws Exception {
        this.makeRequest("DELETE", "/session", null, null, authToken);
    }

    public ListGamesResp listGames(String authToken) throws Exception {
        return this.makeRequest("GET", "/game", null, ListGamesResp.class, authToken);
    }

    public CreateGameResp createGame(CreateGameReq newGame, String authToken) throws Exception {
        return this.makeRequest("POST", "/game", newGame, CreateGameResp.class, authToken);
    }

    void joinGame(JoinGameReq gameJoin, String  authToken) throws Exception {
        this.makeRequest("PUT", "/game", gameJoin, null, authToken);
    }



    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String header) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.addRequestProperty("authorization", header);
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}




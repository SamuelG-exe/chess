package response;

import model.GameData;

import java.util.List;

public record ListGamesResp(List<GameData> games) {
}

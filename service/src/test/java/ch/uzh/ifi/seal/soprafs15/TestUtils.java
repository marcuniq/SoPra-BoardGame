package ch.uzh.ifi.seal.soprafs15;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.net.URL;

/**
 * Created by Cyrus on 17.04.2015.
 */
public class TestUtils {

    public static UserRequestBean toUserRequestBean(Integer age, String username) {

        UserRequestBean userRequest = new UserRequestBean();
        userRequest.setAge(age);
        userRequest.setUsername(username);

        return userRequest;
    }

    public static GameRequestBean toGameRequestBean(String name, String token) {

        GameRequestBean gameRequest = new GameRequestBean();
        gameRequest.setName(name);
        gameRequest.setToken(token);

        return gameRequest;
    }

    public static GamePlayerRequestBean toGamePlayerRequestBean(String token) {

        GamePlayerRequestBean gamePlayerRequest = new GamePlayerRequestBean();
        gamePlayerRequest.setToken(token);

        return gamePlayerRequest;
    }

    public static GameMoveRequestBean toGameMoveBean(String token, MoveEnum move, Boolean desertTileAsOasis, Integer position, Color color, Boolean raceBettingOnWinner) {

        GameMoveRequestBean gameMoveRequest = new GameMoveRequestBean();
        gameMoveRequest.setToken(token);
        gameMoveRequest.setMove(move);
        gameMoveRequest.setDesertTileAsOasis(desertTileAsOasis);
        gameMoveRequest.setDesertTilePosition(position);
        gameMoveRequest.setLegBettingTileColor(color);
        gameMoveRequest.setRaceBettingOnWinner(raceBettingOnWinner);

        return gameMoveRequest;
    }

    public static ResponseEntity<UserResponseBean> createUser(UserRequestBean request, RestTemplate template, URL base) {

        HttpEntity<UserRequestBean> httpEntity = new HttpEntity<UserRequestBean>(request);

        return template.exchange(base + "/users", HttpMethod.POST, httpEntity, UserResponseBean.class);
    }

    public static ResponseEntity<UserLoginLogoutResponseBean> loginUser(Integer id, RestTemplate template, URL base) {

        return template.exchange(base + "/users/" + id + "/login", HttpMethod.POST, null, UserLoginLogoutResponseBean.class);
    }

    public static ResponseEntity<GameCreateResponseBean> createGame(GameRequestBean request, RestTemplate template, URL base) {

        HttpEntity<GameRequestBean> httpEntity = new HttpEntity<GameRequestBean>(request);

        return template.exchange(base + "/games", HttpMethod.POST, httpEntity, GameCreateResponseBean.class);
    }

    public static ResponseEntity<GameAddPlayerResponseBean> addPlayer(GamePlayerRequestBean request, Integer id, RestTemplate template, URL base) {

        HttpEntity<GamePlayerRequestBean> playerRequestHttpEntity = new HttpEntity<GamePlayerRequestBean>(request);

        return template.exchange(base + "/games/" + id + "/players", HttpMethod.POST, playerRequestHttpEntity, GameAddPlayerResponseBean.class);
    }

    public static ResponseEntity<Boolean> clearRepositories(RestTemplate template, URL base) {

        return template.exchange(base + "/backend/reset", HttpMethod.POST, null, Boolean.class);
    }

}

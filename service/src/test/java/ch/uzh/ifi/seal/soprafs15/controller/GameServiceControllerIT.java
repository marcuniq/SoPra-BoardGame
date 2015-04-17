package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameStatus;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

/**
 * Created by Hakuna on 17.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class GameServiceControllerIT {

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    public void testCreateGameSuccess() {
        // Check if number of games is 0 BEFORE creation
        List<GameResponseBean> gamesBefore = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(0, gamesBefore.size());

        // Set up UserRequestBean Object
        UserRequestBean userRequest = new UserRequestBean();
        userRequest.setAge(43);
        userRequest.setUsername("mm");

        HttpEntity<UserRequestBean> httpEntity = new HttpEntity<UserRequestBean>(userRequest);

        // Create user
        ResponseEntity<UserResponseBean> userResponse = template.exchange(base + "/users", HttpMethod.POST, httpEntity, UserResponseBean.class);

        // Login user
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = template.exchange(base + "/users/1/login", HttpMethod.POST, null, UserLoginLogoutResponseBean.class);
        String token = loginResponse.getBody().getToken();

        // Set up GameRequestBean Object
        GameRequestBean gameRequest = new GameRequestBean();
        gameRequest.setName("TestGame");
        gameRequest.setToken(token);

        HttpEntity<GameRequestBean> httpEntity2 = new HttpEntity<GameRequestBean>(gameRequest);

        // Oracle values
        Long oracleId = (long)1;
        Long oracleNumberOfMoves = (long)0;
        Integer oracleNumberOfPlayers = 1;

        // Create Game
        ResponseEntity<GameResponseBean> gameResponse = template.exchange(base + "/games", HttpMethod.POST, httpEntity2, GameResponseBean.class);
        Assert.assertEquals(gameRequest.getName(), gameResponse.getBody().getName());
        Assert.assertEquals(userRequest.getUsername(), gameResponse.getBody().getOwner());
        Assert.assertEquals(oracleId, gameResponse.getBody().getId());
        Assert.assertEquals(oracleNumberOfPlayers, gameResponse.getBody().getNumberOfPlayers());
        //Assert.assertEquals(GameStatus.OPEN, gameResponse.getBody().getStatus());
        //Assert.assertEquals(oracleNumberOfMoves, gameResponse.getBody().getNumberOfMoves());

        // Check if number of games is 1 AFTER creation
        List<GameResponseBean> gamesAfter = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfter.size());
    }
}

package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
    @SuppressWarnings("unchecked")
    public void testCreateGameSuccess() {

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);

        List<GameResponseBean> gamesBefore = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(0, gamesBefore.size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(27, "TestUser");

        TestUtils.createUser(userRequest, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(1, template, base);
        String token = loginResponse.getBody().getToken();

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", token);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Calculate Length of Channel Name
        Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        Assert.assertEquals((long) oracleLength, gameResponse.getBody().getChannelName().length());
        Assert.assertEquals(gameRequest.getName(), gameResponse.getBody().getName());
        Assert.assertEquals(userRequest.getUsername(), gameResponse.getBody().getOwner());
        Assert.assertEquals(1, (long) gameResponse.getBody().getId());
        Assert.assertEquals(1, (long) gameResponse.getBody().getNumberOfPlayers());
        //Assert.assertEquals(GameStatus.OPEN, gameResponse.getBody().getStatus());
        //Assert.assertEquals(0, gameResponse.getBody().getNumberOfMoves());

        List<GameResponseBean> gamesAfter = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfter.size());

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreateGameFail() {

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);

        // Set up

        List<GameResponseBean> gamesBefore = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(0, gamesBefore.size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(27, "TestUser");

        TestUtils.createUser(userRequest, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(1, template, base);
        String token = loginResponse.getBody().getToken();

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", token);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        List<GameResponseBean> gamesAfter = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfter.size());

        // After First Game is created, create a 2nd user that tries to create new Game with same name (Conflict)

        UserRequestBean userRequest2 = TestUtils.toUserRequestBean(57, "Hanfred");

        TestUtils.createUser(userRequest2, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> loginResponse2 = TestUtils.loginUser(1, template, base);
        String token2 = loginResponse2.getBody().getToken();

        GameRequestBean gameRequest2 = TestUtils.toGameRequestBean("TestGame1", token);
        // TODO Exception GameAlreadyExistsResponseBean instead of GameCreateResponseBean
        ResponseEntity<GameCreateResponseBean> gameResponse2 = TestUtils.createGame(gameRequest2, template, base);

        List<GameResponseBean> gamesAfterSecond = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfterSecond.size());

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddPlayerSuccess() {

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);

        // Set up

        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(64, "TestOwner");

        TestUtils.createUser(ownerRequest, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> ownerLoginResponse = TestUtils.loginUser(1, template, base);
        String ownerToken = ownerLoginResponse.getBody().getToken();

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", ownerToken);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Create new player and add it to the created game

        List<GamePlayerResponseBean> playersBefore = template.getForObject(base + "/games/1/players", List.class);
        Assert.assertEquals(1, playersBefore.size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        TestUtils.createUser(userRequest, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(2, template, base);
        String playerToken = loginResponse.getBody().getToken();

        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, 1, template, base);

        // Calculate Length of Channel Name
        Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        Assert.assertEquals((long) oracleLength, playerResponse.getBody().getChannelName().length());

        List<GamePlayerResponseBean> playersAfter = template.getForObject(base + "/games/1/players", List.class);
        Assert.assertEquals(2, playersAfter.size());

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddPlayerFail() {

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);

        // Set up

        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(64, "TestOwner");

        TestUtils.createUser(ownerRequest, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> ownerLoginResponse = TestUtils.loginUser(1, template, base);
        String ownerToken = ownerLoginResponse.getBody().getToken();

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", ownerToken);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Create new player and add it to the created game

        List<GamePlayerResponseBean> playersBefore = template.getForObject(base + "/games/1/players", List.class);
        Assert.assertEquals(1, playersBefore.size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        TestUtils.createUser(userRequest, template, base);

        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(2, template, base);
        String playerToken = loginResponse.getBody().getToken();

        // Wrong game id: game does not exist

        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, 2, template, base);

        // Calculate Length of Channel Name
        Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        // TODO Exception Testing GameNotFoundException instead of GamePlayerResponseBean

        // Calculate Length of Channel Name
        //Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        //Assert.assertEquals((long) oracleLength, playerResponse.getBody().getChannelName().length());

        List<GamePlayerResponseBean> playersAfter = template.getForObject(base + "/games/1/players", List.class);
        Assert.assertEquals(1, playersAfter.size());

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);
    }
}

package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
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
    public void testCreateGameSuccess() throws Exception {

        // Make sure that no game is saved at Initialization
        List<GameResponseBean> gamesBefore = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(0, gamesBefore.size());

        // Create new user
        UserRequestBean userRequest = TestUtils.toUserRequestBean(27, "TestUser");
        ResponseEntity<UserResponseBean> userResponse= TestUtils.createUser(userRequest, template, base);

        // Login newly created user
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
        String token = loginResponse.getBody().getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", token);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Oracle Values and calculate Length of Channel Name
        Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();
        Long oracleId = (long) 1;

        Assert.assertEquals((long) oracleLength, gameResponse.getBody().getChannelName().length());
        Assert.assertEquals(gameRequest.getName(), gameResponse.getBody().getName());
        Assert.assertEquals(userRequest.getUsername(), gameResponse.getBody().getOwner());
        Assert.assertEquals((long) oracleId, (long) gameResponse.getBody().getId());
        Assert.assertEquals((long) userResponse.getBody().getId(), (long) gameResponse.getBody().getNumberOfPlayers());
        //Assert.assertEquals(GameStatus.OPEN, gameResponse.getBody().getStatus());
        //Assert.assertEquals(0, gameResponse.getBody().getNumberOfMoves());

        // Make sure that exactly one game is saved after Test
        List<GameResponseBean> gamesAfter = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfter.size());
    }

    /*@Test
    @SuppressWarnings("unchecked")
    public void testCreateGameFail() throws Exception {

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
    }*/

    @Test
    @SuppressWarnings("unchecked")
    public void testGetGameSuccess() throws Exception {

        // Make sure no game is saved at Initialization
        List<GameResponseBean> gamesBefore = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(0, gamesBefore.size());

        // Create user (owner) to create new game
        UserRequestBean userRequest = TestUtils.toUserRequestBean(65, "TestOwner");
        ResponseEntity<UserResponseBean> userResponse = TestUtils.createUser(userRequest, template, base);

        // Login user to get token for game creation
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
        String token = loginResponse.getBody().getToken();

        // Create New Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", token);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Make sure exactly one game is saved after Test
        List<GameResponseBean> gamesAfter = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfter.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStartGameSuccess() throws Exception {

        // Make sure no game is saved at Initialization
        List<GameResponseBean> gamesBefore = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(0, gamesBefore.size());

        // Create user (owner) to create new game
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(65, "TestOwner");
        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);

        // Login user to get token needed for game creation
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
        String token = loginResponse.getBody().getToken();

        // Create New Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", token);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Check if one game exists (After Game Creation)
        List<GameResponseBean> gamesAfter = template.getForObject(base + "/games", List.class);
        Assert.assertEquals(1, gamesAfter.size());

        //Create second user
        UserRequestBean secondUserRequest = TestUtils.toUserRequestBean(34,"Boris");
        ResponseEntity<UserResponseBean> secondUserResponse = TestUtils.createUser(secondUserRequest, template, base);

        // Login second user
        ResponseEntity<UserLoginLogoutResponseBean> secondLoginResponse = TestUtils.loginUser(secondUserResponse.getBody().getId(), template, base);
        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(secondLoginResponse.getBody().getToken());

        // Add this user as a player to the created game
        TestUtils.addPlayer(playerRequest, gameResponse.getBody().getId(), template, base);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(token);

        // Start Game
        HttpEntity<GamePlayerRequestBean> httpEntity = new HttpEntity<GamePlayerRequestBean>(ownerPlayerRequest);
        ResponseEntity<GameCreateResponseBean> result = template.exchange(base + "/games/1/start", HttpMethod.POST, httpEntity, GameCreateResponseBean.class);

        Assert.assertEquals(GameStatus.RUNNING, result.getBody().getStatus());
        Assert.assertEquals(gameResponse.getBody().getId(), result.getBody().getId());
        Assert.assertEquals(gameResponse.getBody().getOwner(), result.getBody().getOwner());
        Assert.assertEquals(gameRequest.getName(), result.getBody().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddPlayerSuccess() throws Exception {

        // Create new User
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(64, "TestOwner");
        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);

        // Login this User
        ResponseEntity<UserLoginLogoutResponseBean> ownerLoginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
        String ownerToken = ownerLoginResponse.getBody().getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", ownerToken);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Make sure that owner is already assigned to the game
        List<GamePlayerResponseBean> playersBefore = template.getForObject(base + "/games/1/players", List.class);
        Assert.assertEquals(1, playersBefore.size());

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        ResponseEntity<UserResponseBean> userResponse = TestUtils.createUser(userRequest, template, base);

        // Login 2nd User
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
        String playerToken = loginResponse.getBody().getToken();

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, gameResponse.getBody().getId(), template, base);

        // Calculate Length of Channel Name
        Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        Assert.assertEquals((long) oracleLength, playerResponse.getBody().getChannelName().length());

        // Make sure that exactly these two player are assigned to the game
        List<GamePlayerResponseBean> playersAfter = template.getForObject(base + "/games/1/players", List.class);
        Assert.assertEquals(2, playersAfter.size());
    }

//    @Test
//    @SuppressWarnings("unchecked")
//    public void testAddPlayerFail() throws Exception {
//
//        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(64, "TestOwner");
//
//        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);
//
//        ResponseEntity<UserLoginLogoutResponseBean> ownerLoginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
//        String ownerToken = ownerLoginResponse.getBody().getToken();
//
//        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame1", ownerToken);
//        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);
//
//        // Create new player and add it to the created game
//
//        List<GamePlayerResponseBean> playersBefore = template.getForObject(base + "/games/1/players", List.class);
//        Assert.assertEquals(1, playersBefore.size());
//
//        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
//        ResponseEntity<UserResponseBean> userResponse = TestUtils.createUser(userRequest, template, base);
//
//        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
//        String playerToken = loginResponse.getBody().getToken();
//
//        // Wrong game id: game does not exist
//
//        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
//        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, gameResponse.getBody().getId(), template, base);
//
//        // Calculate Length of Channel Name
//        Long oracleLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();
//
//        // TODO Exception Testing GameNotFoundException instead of GamePlayerResponseBean
//
//        //Assert.assertEquals((long) oracleLength, playerResponse.getBody().getChannelName().length());
//
//        List<GamePlayerResponseBean> playersAfter = template.getForObject(base + "/games/1/players", List.class);
//        Assert.assertEquals(1, playersAfter.size());
//    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLegBettingSuccess() throws Exception {

        // Create new user (needed for creating new game)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);

        // Login created user (to get Token for creating new game)
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
        String ownerToken = loginResponse.getBody().getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        ResponseEntity<UserResponseBean> userResponse = TestUtils.createUser(userRequest, template, base);

        // Login 2nd User
        ResponseEntity<UserLoginLogoutResponseBean> secondUserLoginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
        String playerToken = secondUserLoginResponse.getBody().getToken();

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, gameResponse.getBody().getId(), template, base);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        HttpEntity<GamePlayerRequestBean> httpEntity = new HttpEntity<GamePlayerRequestBean>(ownerPlayerRequest);
        ResponseEntity<GameCreateResponseBean> startGameResult = template.exchange(base + "/games/1/start", HttpMethod.POST, httpEntity, GameCreateResponseBean.class);

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = template.getForObject(base + "/games/1/moves", List.class);
        Assert.assertEquals(0, movesBefore.size());

        // Add move
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(ownerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        ResponseEntity<GameMoveResponseBean> addMoveResult = TestUtils.addMove(moveRequest, gameResponse.getBody().getId(), template, base);

        // Oracle values
        Long oracleMoveId = (long) 1;

        Assert.assertNull(addMoveResult.getBody().getDesertTileAsOasis());
        Assert.assertNull(addMoveResult.getBody().getDesertTilePosition());
        Assert.assertNull(addMoveResult.getBody().getRaceBettingOnWinner());
        Assert.assertNull(addMoveResult.getBody().getDie());

        Assert.assertEquals((long) oracleMoveId, (long) addMoveResult.getBody().getId());

        Assert.assertEquals(gameResponse.getBody().getId(), addMoveResult.getBody().getGameId());
        Assert.assertEquals(ownerResponse.getBody().getId(), addMoveResult.getBody().getUserId());
        Assert.assertEquals(MoveEnum.LEG_BETTING, addMoveResult.getBody().getMove());
        Assert.assertEquals(Color.BLUE, addMoveResult.getBody().getLegBettingTile().getColor());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = template.getForObject(base + "/games/1/moves", List.class);
        Assert.assertEquals(1, movesAfter.size());
    }

//    @Test
//    @SuppressWarnings("unchecked")
//    public void testDesertTilePlacingSuccess() throws Exception {
//
//        // Create new user (needed for creating new game)
//        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
//        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);
//
//        // Login created user (to get Token for creating new game)
//        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
//        String token = loginResponse.getBody().getToken();
//
//        // Create new game
//        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", token);
//        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);
//
//        // Game contains no moves after initialization (before we added any move)
//        List<GameMoveResponseBean> movesBefore = template.getForObject(base + "/games/1/moves", List.class);
//        Assert.assertEquals(0, movesBefore.size());
//
//        // Add move
//        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(token, MoveEnum.DESERT_TILE_PLACING, false, 7, null, null);
//
//        ResponseEntity<GameMoveResponseBean> result = TestUtils.addMove(moveRequest, gameResponse.getBody().getId(), template, base);
//
//        Assert.assertNull(result.getBody().getRaceBettingOnWinner());
//        Assert.assertNull(result.getBody().popLegBettingTile());
//        Assert.assertNull(result.getBody().getDie());
//
//        Assert.assertFalse(result.getBody().getDesertTileAsOasis());
//
//        Assert.assertEquals(7, (long) result.getBody().getDesertTilePosition());
//        Assert.assertEquals(1, (long) result.getBody().getId());
//        Assert.assertEquals(gameResponse.getBody().getId(), result.getBody().getGameId());
//        Assert.assertEquals(ownerResponse.getBody().getId(), result.getBody().getUserId());
//        Assert.assertEquals(MoveEnum.DESERT_TILE_PLACING, result.getBody().getMove());
//
//        // Game contains exactly one move (after we added one move)
//        List<GameMoveResponseBean> movesAfter = template.getForObject(base + "/games/1/moves", List.class);
//        Assert.assertEquals(1, movesAfter.size());
//    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRaceBettingSuccess() throws Exception {

        // Create new user (needed for creating new game)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);

        // Login created user (to get Token for creating new game)
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
        String ownerToken = loginResponse.getBody().getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        ResponseEntity<UserResponseBean> userResponse = TestUtils.createUser(userRequest, template, base);

        // Login 2nd User
        ResponseEntity<UserLoginLogoutResponseBean> secondUserLoginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
        String playerToken = secondUserLoginResponse.getBody().getToken();

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, gameResponse.getBody().getId(), template, base);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        HttpEntity<GamePlayerRequestBean> httpEntity = new HttpEntity<GamePlayerRequestBean>(ownerPlayerRequest);
        ResponseEntity<GameCreateResponseBean> startGameResult = template.exchange(base + "/games/1/start", HttpMethod.POST, httpEntity, GameCreateResponseBean.class);

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = template.getForObject(base + "/games/1/moves", List.class);
        Assert.assertEquals(0, movesBefore.size());

        // Add move

        // Maybe it's not the owner's turn! check playerId
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(ownerToken, MoveEnum.RACE_BETTING, null, null, null, true, Color.BLUE);
        ResponseEntity<GameMoveResponseBean> result = TestUtils.addMove(moveRequest, gameResponse.getBody().getId(), template, base);

        // Oracle values
        Long oracleMoveId = (long) 1;

        Assert.assertNull(result.getBody().getDesertTileAsOasis());
        Assert.assertNull(result.getBody().getDesertTilePosition());
        Assert.assertNull(result.getBody().getLegBettingTile());
        Assert.assertNull(result.getBody().getDie());

        Assert.assertTrue(result.getBody().getRaceBettingOnWinner());

        Assert.assertEquals((long) oracleMoveId, (long) result.getBody().getId());
        Assert.assertEquals(gameResponse.getBody().getId(), result.getBody().getGameId());
        Assert.assertEquals(ownerResponse.getBody().getId(), result.getBody().getUserId());
        Assert.assertEquals(MoveEnum.RACE_BETTING, result.getBody().getMove());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = template.getForObject(base + "/games/1/moves", List.class);
        Assert.assertEquals(1, movesAfter.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRollDiceSuccess() throws Exception {

        // Create new user (needed for creating new game)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        ResponseEntity<UserResponseBean> ownerResponse = TestUtils.createUser(ownerRequest, template, base);

        // Login created user (to get Token for creating new game)
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(ownerResponse.getBody().getId(), template, base);
        String ownerToken = loginResponse.getBody().getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        ResponseEntity<GameCreateResponseBean> gameResponse = TestUtils.createGame(gameRequest, template, base);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        ResponseEntity<UserResponseBean> userResponse = TestUtils.createUser(userRequest, template, base);

        // Login 2nd User
        ResponseEntity<UserLoginLogoutResponseBean> secondUserLoginResponse = TestUtils.loginUser(userResponse.getBody().getId(), template, base);
        String playerToken = secondUserLoginResponse.getBody().getToken();

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean playerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        ResponseEntity<GameAddPlayerResponseBean> playerResponse = TestUtils.addPlayer(playerRequest, gameResponse.getBody().getId(), template, base);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        HttpEntity<GamePlayerRequestBean> httpEntity = new HttpEntity<GamePlayerRequestBean>(ownerPlayerRequest);
        ResponseEntity<GameCreateResponseBean> startGameResult = template.exchange(base + "/games/1/start", HttpMethod.POST, httpEntity, GameCreateResponseBean.class);

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = template.getForObject(base + "/games/1/moves", List.class);
        Assert.assertEquals(0, movesBefore.size());

        // Oracle values
        Long oracleMoveId = (long) 1;

        // Add move

        // maybe it's not the owner's turn! check player id
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(ownerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);
        ResponseEntity<GameMoveResponseBean> result = TestUtils.addMove(moveRequest, gameResponse.getBody().getId(), template, base);

        Assert.assertNull(result.getBody().getDesertTileAsOasis());
        Assert.assertNull(result.getBody().getDesertTilePosition());
        Assert.assertNull(result.getBody().getRaceBettingOnWinner());
        Assert.assertNull(result.getBody().getLegBettingTile());

        Assert.assertNotNull(result.getBody().getDie());

        Assert.assertEquals((long) oracleMoveId, (long) result.getBody().getId());
        Assert.assertEquals(gameResponse.getBody().getId(), result.getBody().getGameId());
        Assert.assertEquals(ownerResponse.getBody().getId(), result.getBody().getUserId());
        Assert.assertEquals(MoveEnum.DICE_ROLLING, result.getBody().getMove());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = template.getForObject(base + "/games/1/moves", List.class);
        Assert.assertEquals(1, movesAfter.size());
    }
}

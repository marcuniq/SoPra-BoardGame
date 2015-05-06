package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;
import ch.uzh.ifi.seal.soprafs15.model.move.RaceBetting;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameFullException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotAuthorizedException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GamePlayerServiceUT {

    @Mock
    private GameRepository mockGameRepo;

    @Mock
    private UserRepository mockUserRepo;

    @InjectMocks
    @Autowired
    private GameService testGameService;

    @InjectMocks
    @Autowired
    private UserService testUserService;

    @InjectMocks
    @Autowired
    private GamePlayerService testGamePlayerService;

    @InjectMocks
    @Autowired
    private GameActionService testGameActionService;

    @InjectMocks
    @Autowired
    private GameMoveService testGameMoveService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testAddPlayerGameNotFoundFail() throws Exception {

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(82, "Ulrich");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GamePlayerRequestBean playerRequestBean = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());

        Long falseGameId = (long) 1;

        testGamePlayerService.addPlayer(falseGameId, playerRequestBean);
    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testAddPlayerUserNotFoundFail() throws Exception {

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(82, "Ulrich");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("Test Game", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        GamePlayerRequestBean falseRequest = TestUtils.toGamePlayerRequestBean("False Token");

        testGamePlayerService.addPlayer(gameResponse.getId(), falseRequest);
    }

    @Test(expected = GameFullException.class)
    @SuppressWarnings("unchecked")
    public void testAddPlayerGameFullFail() throws Exception {

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean firstUserRequest = TestUtils.toUserRequestBean(12, "Hanfred");
        UserRequestBean secondUserRequest = TestUtils.toUserRequestBean(23, "Ueli");
        UserRequestBean thirdUserRequest = TestUtils.toUserRequestBean(34, "Kushtrim");
        UserRequestBean fourthUserRequest = TestUtils.toUserRequestBean(45, "Ursula");
        UserRequestBean fifthUserRequest = TestUtils.toUserRequestBean(56, "Herbert");
        UserRequestBean sixthUserRequest = TestUtils.toUserRequestBean(67, "Erich");
        UserRequestBean seventhUserRequest = TestUtils.toUserRequestBean(78, "Blerim");

        UserResponseBean firstUserResponse = testUserService.addUser(firstUserRequest);
        UserResponseBean secondUserResponse = testUserService.addUser(secondUserRequest);
        UserResponseBean thirdUserResponse = testUserService.addUser(thirdUserRequest);
        UserResponseBean fourthUserResponse = testUserService.addUser(fourthUserRequest);
        UserResponseBean fifthUserResponse = testUserService.addUser(fifthUserRequest);
        UserResponseBean sixthUserResponse = testUserService.addUser(sixthUserRequest);
        UserResponseBean seventhUserResponse = testUserService.addUser(seventhUserRequest);

        UserLoginLogoutResponseBean firstLoginResponse = testUserService.login(firstUserResponse.getId());
        UserLoginLogoutResponseBean secondLoginResponse = testUserService.login(secondUserResponse.getId());
        UserLoginLogoutResponseBean thirdLoginResponse = testUserService.login(thirdUserResponse.getId());
        UserLoginLogoutResponseBean fourthLoginResponse = testUserService.login(fourthUserResponse.getId());
        UserLoginLogoutResponseBean fifthLoginResponse = testUserService.login(fifthUserResponse.getId());
        UserLoginLogoutResponseBean sixthLoginResponse = testUserService.login(sixthUserResponse.getId());
        UserLoginLogoutResponseBean seventhLoginResponse = testUserService.login(seventhUserResponse.getId());

        String firstToken = firstLoginResponse.getToken();
        String secondToken = secondLoginResponse.getToken();
        String thirdToken = thirdLoginResponse.getToken();
        String fourthToken = fourthLoginResponse.getToken();
        String fifthToken = fifthLoginResponse.getToken();
        String sixthToken = sixthLoginResponse.getToken();
        String seventhToken = seventhLoginResponse.getToken();

        GamePlayerRequestBean secondPlayerRequest = TestUtils.toGamePlayerRequestBean(secondToken);
        GamePlayerRequestBean thirdPlayerRequest = TestUtils.toGamePlayerRequestBean(thirdToken);
        GamePlayerRequestBean fourthPlayerRequest = TestUtils.toGamePlayerRequestBean(fourthToken);
        GamePlayerRequestBean fifthPlayerRequest = TestUtils.toGamePlayerRequestBean(fifthToken);
        GamePlayerRequestBean sixthPlayerRequest = TestUtils.toGamePlayerRequestBean(sixthToken);
        GamePlayerRequestBean seventhPlayerRequest = TestUtils.toGamePlayerRequestBean(seventhToken);

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", firstToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        testGamePlayerService.addPlayer(gameResponse.getId(), secondPlayerRequest);
        testGamePlayerService.addPlayer(gameResponse.getId(), thirdPlayerRequest);
        testGamePlayerService.addPlayer(gameResponse.getId(), fourthPlayerRequest);
        testGamePlayerService.addPlayer(gameResponse.getId(), fifthPlayerRequest);
        testGamePlayerService.addPlayer(gameResponse.getId(), sixthPlayerRequest);
        testGamePlayerService.addPlayer(gameResponse.getId(), seventhPlayerRequest);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListPlayer() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(93, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(ownerResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());
        assertEquals(1, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(1, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(74, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = new GamePlayerRequestBean();
        addPlayerRequest.setToken(playerLoginResponse.getToken());

        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        assertEquals((long) oracleChannelNameLength, addPlayerResponse.getChannelName().length());

        List<GamePlayerResponseBean> result = testGamePlayerService.listPlayer(gameResponse.getId());

        assertEquals(ownerResponse.getId(), result.get(0).getId());
        assertEquals(playerResponse.getId(), result.get(1).getId());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddPlayer() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(93, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(ownerResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());
        assertEquals(1, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(1, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(74, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = new GamePlayerRequestBean();
        addPlayerRequest.setToken(playerLoginResponse.getToken());

        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        assertEquals((long) oracleChannelNameLength, addPlayerResponse.getChannelName().length());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());
    }



    @Test
    @SuppressWarnings("unchecked")
    public void testGetPlayer() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(61, "TestUser");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(22, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), startGameRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        Long currentPlayerUserId = (long) -1;
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = loginResponse.getToken();
                    currentPlayerUserId = userResponse.getId();
                } else {
                    currentPlayerToken = playerLoginResponse.getToken();
                    currentPlayerUserId = playerResponse.getId();
                }
            }
        }

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());

        // the id of the user and playerId of the user are not the same anymore
        GamePlayerResponseBean result = testGamePlayerService.getPlayer(gameResponse.getId(), currentPlayerId);

        assertEquals(currentPlayerUserId, result.getId());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetPlayerGameNotFoundFail() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(61, "TestUser");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(22, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), startGameRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        Long currentPlayerUserId = (long) -1;
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = loginResponse.getToken();
                    currentPlayerUserId = userResponse.getId();
                } else {
                    currentPlayerToken = playerLoginResponse.getToken();
                    currentPlayerUserId = playerResponse.getId();
                }
            }
        }

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());

        // the id of the user and playerId of the user are not the same anymore
        GamePlayerResponseBean result = testGamePlayerService.getPlayer(gameResponse.getId() + 1, currentPlayerId);

        assertEquals(currentPlayerUserId, result.getId());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());
    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetPlayerUserNotFoundFail() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames(null).size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(61, "TestUser");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(22, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), startGameRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        Long currentPlayerUserId = (long) -1;
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = loginResponse.getToken();
                    currentPlayerUserId = userResponse.getId();
                } else {
                    currentPlayerToken = playerLoginResponse.getToken();
                    currentPlayerUserId = playerResponse.getId();
                }
            }
        }

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());

        // the id of the user and playerId of the user are not the same anymore
        GamePlayerResponseBean result = testGamePlayerService.getPlayer(gameResponse.getId(), 7);

        assertEquals(currentPlayerUserId, result.getId());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames(null).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingCards() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Oracle Values
        Long oracleUserId = (long) -1;

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = playerToken;
                    oracleUserId = userResponse.getId();
                } else {
                    currentPlayerToken = ownerToken;
                    oracleUserId = ownerResponse.getId();
                }
            }
        }

        // Test
        GamePlayerRequestBean getCardsRequest = TestUtils.toGamePlayerRequestBean(currentPlayerToken);
        List<RaceBettingCard> result = testGamePlayerService.getRaceBettingCards(gameResponse.getId(), currentPlayerId, getCardsRequest);

        // Make sure that it contains all 5 RaceBettingCards
        Assert.assertEquals(5, result.size());
    }

    @Test(expected = NotAuthorizedException.class)
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingCardsNotAuthorizedFail() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = playerToken;
                    notCurrentPlayerToken = ownerToken;
                } else {
                    currentPlayerToken = ownerToken;
                    notCurrentPlayerToken = playerToken;
                }
            }
        }

        // Test
        GamePlayerRequestBean getCardsRequest = TestUtils.toGamePlayerRequestBean(notCurrentPlayerToken);
        List<RaceBettingCard> result = testGamePlayerService.getRaceBettingCards(gameResponse.getId(), currentPlayerId, getCardsRequest);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingCardsGameNotFoundFail() throws Exception {

        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Oracle Values
        Long oracleUserId = (long) -1;

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = playerToken;
                    oracleUserId = userResponse.getId();
                } else {
                    currentPlayerToken = ownerToken;
                    oracleUserId = ownerResponse.getId();
                }
            }
        }

        // Test
        GamePlayerRequestBean getCardsRequest = TestUtils.toGamePlayerRequestBean(currentPlayerToken);
        List<RaceBettingCard> result = testGamePlayerService.getRaceBettingCards(gameResponse.getId() + 1, currentPlayerId, getCardsRequest);
    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingCardsUserNotFoundFail() throws Exception {

        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Oracle Values
        Long oracleUserId = (long) -1;

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = playerToken;
                    oracleUserId = userResponse.getId();
                } else {
                    currentPlayerToken = ownerToken;
                    oracleUserId = ownerResponse.getId();
                }
            }
        }

        // Test
        GamePlayerRequestBean getCardsRequest = TestUtils.toGamePlayerRequestBean(currentPlayerToken);
        List<RaceBettingCard> result = testGamePlayerService.getRaceBettingCards(gameResponse.getId(), 3, getCardsRequest);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testRemovePlayerGameNotFoundFail() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Test call
        GamePlayerRequestBean removeUserRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        testGamePlayerService.removePlayer(gameResponse.getId() + 1, userResponse.getId().intValue(), removeUserRequest, true);
    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testRemovePlayerUserNotFoundFail() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Test call
        GamePlayerRequestBean removeUserRequest = TestUtils.toGamePlayerRequestBean("falseToken");
        testGamePlayerService.removePlayer(gameResponse.getId(), userResponse.getId().intValue(), removeUserRequest, true);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemovePlayerWithUserId() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        List<GamePlayerResponseBean> playersBefore = testGamePlayerService.listPlayer(gameResponse.getId());
        Assert.assertEquals(2, playersBefore.size());

        // Test call
        GamePlayerRequestBean removeUserRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        testGamePlayerService.removePlayer(gameResponse.getId(), userResponse.getId().intValue(), removeUserRequest, true);

        List<GamePlayerResponseBean> playersAfter = testGamePlayerService.listPlayer(gameResponse.getId());
        Assert.assertEquals(1, playersAfter.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemovePlayerWithPlayerId() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = testUserService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = testGameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = testGamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = playerToken;
                    notCurrentPlayerToken = ownerToken;
                } else {
                    currentPlayerToken = ownerToken;
                    notCurrentPlayerToken = playerToken;
                }
            }
        }

        List<GamePlayerResponseBean> playersBefore = testGamePlayerService.listPlayer(gameResponse.getId());
        Assert.assertEquals(2, playersBefore.size());

        // Test call
        GamePlayerRequestBean removeUserRequest = TestUtils.toGamePlayerRequestBean(currentPlayerToken);
        testGamePlayerService.removePlayer(gameResponse.getId(), currentPlayerId, removeUserRequest, false);

        List<GamePlayerResponseBean> playersAfter = testGamePlayerService.listPlayer(gameResponse.getId());
        Assert.assertEquals(1, playersAfter.size());
    }
}
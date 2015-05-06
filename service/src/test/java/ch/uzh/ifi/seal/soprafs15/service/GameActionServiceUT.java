package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotEnoughPlayerException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.OwnerNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by Hakuna on 04.05.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameActionServiceUT {

    @Mock
    protected GameRepository gameRepository;

    @Mock
    private UserRepository mockUserRepo;

    @InjectMocks
    @Autowired
    protected GameLogicService gameLogicService;

    @InjectMocks
    @Autowired
    private GameService gameService;

    @InjectMocks
    @Autowired
    private UserService userService;

    @InjectMocks
    @Autowired
    private GamePlayerService gamePlayerService;

    @InjectMocks
    @Autowired
    private GameActionService gameActionService;

    @InjectMocks
    @Autowired
    private GameMoveService gameMoveService;

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testStartGameOwnerNotFoundFail() {
        // Create new User (OWNER)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(66, "Eric");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login OWNER
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerLoginResponse.getToken());
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Create 2nd user (PLAYER)
        UserRequestBean playerRequest = TestUtils.toUserRequestBean(29, "Nick");
        UserResponseBean playerResponse = userService.addUser(playerRequest);

        // Login PLAYER
        UserLoginLogoutResponseBean playerLoginResponse = userService.login(playerResponse.getId());

        // Add PLAYER to Game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);


        // Start Game
        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean("asdf");
        GameResponseBean result = gameActionService.startGame(gameResponse.getId(), startGameRequest);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStartGame() throws Exception {
        // Create new User (OWNER)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(66, "Eric");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login OWNER
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerLoginResponse.getToken());
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Create 2nd user (PLAYER)
        UserRequestBean playerRequest = TestUtils.toUserRequestBean(29, "Nick");
        UserResponseBean playerResponse = userService.addUser(playerRequest);

        // Login PLAYER
        UserLoginLogoutResponseBean playerLoginResponse = userService.login(playerResponse.getId());

        // Add PLAYER to Game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        Assert.assertEquals(GameStatus.OPEN, gameResponse.getStatus());

        // Start Game
        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(ownerLoginResponse.getToken());
        GameResponseBean result = gameActionService.startGame(gameResponse.getId(), startGameRequest);

        Assert.assertEquals(ownerRequest.getUsername(), result.getOwner());
        Assert.assertEquals(gameResponse.getId(), result.getId());
        Assert.assertEquals(gameRequest.getName(), result.getName());
        Assert.assertSame(GameStatus.RUNNING, result.getStatus());
        Assert.assertEquals(gameMoveService.listMoves(gameResponse.getId()).size(), (int) result.getNumberOfMoves());
    }

    @Test(expected = NotEnoughPlayerException.class)
    @SuppressWarnings("unchecked")
    public void testStartGameNotEnoughPlayerFail() throws Exception {
        // Create new User (OWNER)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(66, "Eric");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login OWNER
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerLoginResponse.getToken());
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        Assert.assertEquals(GameStatus.OPEN, gameResponse.getStatus());

        // Start Game (with only One Player)
        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(ownerLoginResponse.getToken());
        GameResponseBean result = gameActionService.startGame(gameResponse.getId(), startGameRequest);

        Assert.assertEquals(GameStatus.OPEN, gameResponse.getStatus());
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testStartGameGameNotFoundFail() throws Exception {
        Assert.assertEquals(0, gameService.listGames(null).size());

        // Create new User (OWNER)
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(66, "Eric");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login OWNER
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerLoginResponse.getToken());
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Oracle Values
        Long notGameId = (long) 2;

        // Assert that only one game exists (the one that we created)
        Assert.assertEquals(1, gameService.listGames(null).size());
        Assert.assertEquals(gameService.listGames(null).size(), (long) gameResponse.getId());
        Assert.assertEquals(GameStatus.OPEN, gameResponse.getStatus());
        Assert.assertNotEquals(notGameId, gameResponse.getId());

        // Start Game (with only One Player)
        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(ownerLoginResponse.getToken());
        GameResponseBean result = gameActionService.startGame(notGameId, startGameRequest);

        Assert.assertEquals(GameStatus.OPEN, gameResponse.getStatus());
    }
}


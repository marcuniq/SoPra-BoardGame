package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameAreaServiceUT {

    @Mock
    private GameRepository mockGameRepo;

    @Mock
    private UserRepository mockUserRepo;

    @InjectMocks
    @Autowired
    private GameService gameService;

    @InjectMocks
    @Autowired
    private UserService userService;

    @InjectMocks
    @Autowired
    private GameAreaService gameAreaService;

    @InjectMocks
    @Autowired
    private GamePlayerService gamePlayerService;

    @InjectMocks
    @Autowired
    private GameActionService gameActionService;

    @InjectMocks
    @Autowired
    private GameMapperService gameMapperService;

    @InjectMocks
    @Autowired
    private GameMoveService gameMoveService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingArea() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = userService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = userService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Start Game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Test Call
        GameRaceBettingAreaResponseBean result = gameAreaService.getRaceBettingArea(gameResponse.getId());

        // Assertions at Initialization
        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(0, (long) result.getNrOfLoserBetting());
        Assert.assertEquals(0, (long) result.getNrOfWinnerBetting());

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String firstPlayerToken = new String();
        String secondPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    firstPlayerToken = playerToken;
                    secondPlayerToken = ownerToken;
                } else {
                    firstPlayerToken = ownerToken;
                    secondPlayerToken = playerToken;
                }
            }
        }

        // Make a Winner Betting Move
        GameMoveRequestBean winnerBettingRequest = TestUtils.toGameMoveRequestBean(firstPlayerToken, MoveEnum.RACE_BETTING, null, null, null, true, Color.BLUE);
        GameMoveResponseBean winnerBettingResponse = gameMoveService.addMove(gameResponse.getId(), winnerBettingRequest);

        // Test Call 2
        result = gameAreaService.getRaceBettingArea(gameResponse.getId());

        // Assertions after one Winner Betting
        Assert.assertEquals(0, (long) result.getNrOfLoserBetting());
        Assert.assertEquals(1, (long) result.getNrOfWinnerBetting());

        // Make a Loser Betting Move
        GameMoveRequestBean loserBettingRequest = TestUtils.toGameMoveRequestBean(secondPlayerToken, MoveEnum.RACE_BETTING, null, null, null, false, Color.ORANGE);
        GameMoveResponseBean loserBettingResponse = gameMoveService.addMove(gameResponse.getId(), loserBettingRequest);

        // Test Call 3
        result = gameAreaService.getRaceBettingArea(gameResponse.getId());

        // Assertions after one Winner Betting
        Assert.assertEquals(1, (long) result.getNrOfLoserBetting());
        Assert.assertEquals(1, (long) result.getNrOfWinnerBetting());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetDiceArea() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = userService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = userService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Start Game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Test Call
        GameDiceAreaResponseBean result = gameAreaService.getDiceArea(gameResponse.getId());

        // Assertions at Initialization
        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(0, (long) result.getRolledDice().size());

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String firstPlayerToken = new String();
        String secondPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    firstPlayerToken = playerToken;
                    secondPlayerToken = ownerToken;
                } else {
                    firstPlayerToken = ownerToken;
                    secondPlayerToken = playerToken;
                }
            }
        }

        // Make a Dice Rolling Move
        GameMoveRequestBean diceRollingRequest = TestUtils.toGameMoveRequestBean(firstPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);
        GameMoveResponseBean diceRollingResponse = gameMoveService.addMove(gameResponse.getId(), diceRollingRequest);

        // Test Call 2
        result = gameAreaService.getDiceArea(gameResponse.getId());

        // Assertions after Dice Rolling
        Assert.assertEquals(1, (long) result.getRolledDice().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetLegBettingArea() throws Exception {
        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = userService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = userService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Start Game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        GameLegBettingAreaResponseBean result = gameAreaService.getLegBettingArea(gameResponse.getId());

        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(5, result.getTopLegBettingTiles().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceTrack() throws Exception {

        // Create new user
        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(44, "TestOwner");
        UserResponseBean ownerResponse = userService.addUser(ownerRequest);

        // Login created user (to get Token for creating new game)
        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());
        String ownerToken = ownerLoginResponse.getToken();

        // Create new Game
        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", ownerToken);
        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);

        // Create 2nd User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(33, "TestPlayer");
        UserResponseBean userResponse = userService.addUser(userRequest);

        // Login 2nd User
        UserLoginLogoutResponseBean playerLoginResponse = userService.login(userResponse.getId());
        String playerToken = playerLoginResponse.getToken();

        // Add 2nd user to game
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Start Game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        GameRaceTrackResponseBean result = gameAreaService.getRaceTrack(gameResponse.getId());

        Assert.assertEquals(1, (long) result.getId());
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetRaceTrackGameNotFoundFail() throws Exception {
        Assert.assertNull(gameService.getGame((long) 1));
        gameAreaService.getRaceTrack((long) 1);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetLegBettingAreaGameNotFoundFail() throws Exception {
        Assert.assertNull(gameService.getGame((long) 1));
        gameAreaService.getLegBettingArea((long) 1);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingAreaGameNotFoundFail() throws Exception {
        Assert.assertNull(gameService.getGame((long) 1));
        gameAreaService.getRaceBettingArea((long) 1);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetDiceAreaGameNotFoundFail() throws Exception {
        Assert.assertNull(gameService.getGame((long) 1));
        gameAreaService.getDiceArea((long) 1);
    }
}
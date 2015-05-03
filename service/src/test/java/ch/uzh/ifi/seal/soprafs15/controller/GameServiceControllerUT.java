package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.service.*;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotYourTurnException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by Hakuna on 02.05.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameServiceControllerUT {

    @Autowired
    @InjectMocks
    protected GameService gameService;

    @Autowired
    @InjectMocks
    protected UserService userService;

    @Autowired
    @InjectMocks
    protected GameMoveService gameMoveService;

    @Autowired
    @InjectMocks
    protected GamePlayerService gamePlayerService;

    @Autowired
    @InjectMocks
    protected GameActionService gameActionService;

    @Autowired
    @InjectMocks
    protected GameAreaService gameAreaService;

    @Test(expected = NotYourTurnException.class)
    @SuppressWarnings("unchecked")
    public void testLegBettingFail() throws Exception {

        // Create new user (needed for creating new game)
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

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(!player.getUsername().equals(userResponse.getUsername())) {
                    notCurrentPlayerToken = playerToken;
                } else {
                    notCurrentPlayerToken = ownerToken;
                }
            }
        }

        // Add move with Token of player who is not currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null);
        gameMoveService.addMove(gameResponse.getId(), moveRequest);
    }

    @Test(expected = NotYourTurnException.class)
    @SuppressWarnings("unchecked")
    public void testDesertTilePlacingFail() throws Exception {

        // Create new user (needed for creating new game)
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

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(!player.getUsername().equals(userResponse.getUsername())) {
                    notCurrentPlayerToken = playerToken;
                } else {
                    notCurrentPlayerToken = ownerToken;
                }
            }
        }

        // Add move with Token of player who is not currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.DESERT_TILE_PLACING, false, 7, null, null);
        gameMoveService.addMove(gameResponse.getId(), moveRequest);
    }

    @Test(expected = NotYourTurnException.class)
    @SuppressWarnings("unchecked")
    public void testRollDiceFail() throws Exception {

        // Create new user (needed for creating new game)
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

        // Create GamePlayerRequestBean with Token from 2nd User and assign it to the game as well
        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerToken);
        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        // Create GamePlayerRequestBean with Token of owner for Starting the game
        GamePlayerRequestBean ownerPlayerRequest = TestUtils.toGamePlayerRequestBean(ownerToken);

        // Start Game
        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), ownerPlayerRequest);

        // Get PlayerId of currentPlayer
        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        // Get Token of not currentPlayer
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(!player.getUsername().equals(userResponse.getUsername())) {
                    notCurrentPlayerToken = playerToken;
                } else {
                    notCurrentPlayerToken = ownerToken;
                }
            }
        }

        // Add move with Token of player who is not currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null);;
        gameMoveService.addMove(gameResponse.getId(), moveRequest);
    }
}

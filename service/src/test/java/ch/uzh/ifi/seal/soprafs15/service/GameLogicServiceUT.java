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
import ch.uzh.ifi.seal.soprafs15.service.exceptions.InvalidMoveException;
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

import java.util.List;

/**
 * Created by Hakuna on 04.05.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameLogicServiceUT {

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

    @Test(expected = InvalidMoveException.class)
    @SuppressWarnings("unchecked")
    public void testLegBettingInvalidMoveFail() throws Exception {

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
        String currentPlayerToken = new String();
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move Nr 1
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        GameMoveResponseBean addMoveResult = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter.size());

        // Add Move Nr 2
        GameMoveRequestBean secondMoveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        GameMoveResponseBean addSecondMoveResult = gameMoveService.addMove(gameResponse.getId(), secondMoveRequest);

        // Game contains exactly two moves (after we added two moves)
        List<GameMoveResponseBean> movesAfterSecond = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(2, movesAfterSecond.size());

        // Add Move Nr 3
        GameMoveRequestBean thirdMoveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        GameMoveResponseBean addThirdMoveResult = gameMoveService.addMove(gameResponse.getId(), thirdMoveRequest);

        // Game contains exactly three moves (after we added three moves)
        List<GameMoveResponseBean> movesAfterThird = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(3, movesAfterThird.size());

        // Add Invalid Move
        GameMoveRequestBean invalidMoveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        GameMoveResponseBean invalidMoveResult = gameMoveService.addMove(gameResponse.getId(), invalidMoveRequest);

        // Game contains exactly three moves (after we tried to add an Invalid Move)
        List<GameMoveResponseBean> movesAfterInvalidMove = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(3, movesAfterInvalidMove.size());
    }

    @Test(expected = InvalidMoveException.class)
    @SuppressWarnings("unchecked")
    public void testDesertTilePlacingInvalidMoveFail() throws Exception {

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
        String currentPlayerToken = new String();
        String notCurrentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move Nr 1
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.DESERT_TILE_PLACING, false, 7, null, null, null);
        GameMoveResponseBean addMoveResult = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter.size());

        // Add Move Nr 2 (Place Desert Tile on another DesertTile => Invalid Move)
        GameMoveRequestBean secondMoveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.DESERT_TILE_PLACING, false, 7, null, null, null);
        GameMoveResponseBean addSecondMoveResult = gameMoveService.addMove(gameResponse.getId(), secondMoveRequest);


        // Game contains exactly one move (after we tried to add an Invalid Move one move)
        List<GameMoveResponseBean> movesAfter2 = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter2.size());
    }

    @Test(expected = InvalidMoveException.class)
    @SuppressWarnings("unchecked")
    public void testRaceBettingInvalidMoveFail() throws Exception {
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
        String currentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    currentPlayerToken = playerToken;
                } else {
                    currentPlayerToken = ownerToken;
                }
            }
        }

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.RACE_BETTING, null, null, null, true, null);
        GameMoveResponseBean result = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains still no Move (after we tried to add an Invalid Move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesAfter.size());
    }
}

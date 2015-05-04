package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.*;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.NotYourTurnException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Hakuna on 02.05.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameServiceControllerUT {

    @Mock
    protected GameRepository gameRepository;

    @Mock
    private UserRepository mockUserRepo;

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

    @Test
    @SuppressWarnings("unchecked")
    public void testLegBetting() throws Exception {

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

        // Oracle Values
        Long oracleUserId = (long) -1;
        Long oracleMoveId = (long) 1;

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        GameMoveResponseBean addMoveResult = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        Assert.assertNull(addMoveResult.getDesertTileAsOasis());
        Assert.assertNull(addMoveResult.getDesertTilePosition());
        Assert.assertNull(addMoveResult.getRaceBettingOnWinner());
        Assert.assertNull(addMoveResult.getDie());
        Assert.assertEquals((long) oracleMoveId, (long) addMoveResult.getId());
        Assert.assertEquals(gameResponse.getId(), addMoveResult.getGameId());
        Assert.assertEquals(oracleUserId, addMoveResult.getUserId());
        Assert.assertEquals(MoveEnum.LEG_BETTING, addMoveResult.getMove());
        Assert.assertEquals(Color.BLUE, addMoveResult.getLegBettingTile().getColor());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDesertTilePlacing() throws Exception {

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

        // Oracle values
        Long oracleUserId = (long) -1;
        Long oracleMoveId = (long) 1;

        // Get Token of not currentPlayer
        String currentPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.DESERT_TILE_PLACING, false, 7, null, null, null);
        GameMoveResponseBean addMoveResult = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        Assert.assertNull(addMoveResult.getRaceBettingOnWinner());
        Assert.assertNull(addMoveResult.getDie());
        Assert.assertFalse(addMoveResult.getDesertTileAsOasis());
        Assert.assertEquals((long) oracleMoveId, (long) addMoveResult.getId());
        Assert.assertEquals(7, (long) addMoveResult.getDesertTilePosition());
        Assert.assertEquals(1, (long) addMoveResult.getId());
        Assert.assertEquals(gameResponse.getId(), addMoveResult.getGameId());
        Assert.assertEquals(oracleUserId, addMoveResult.getUserId());
        Assert.assertEquals(MoveEnum.DESERT_TILE_PLACING, addMoveResult.getMove());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRollDice() throws Exception {

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

        // Oracle values
        Long oracleUserId = (long) -1;
        Long oracleMoveId = (long) 1;

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);;
        GameMoveResponseBean result = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        Assert.assertNull(result.getDesertTileAsOasis());
        Assert.assertNull(result.getDesertTilePosition());
        Assert.assertNull(result.getRaceBettingOnWinner());
        Assert.assertNull(result.getLegBettingTile());
        Assert.assertNotNull(result.getDie());
        Assert.assertEquals((long) oracleMoveId, (long) result.getId());
        Assert.assertEquals(gameResponse.getId(), result.getGameId());
        Assert.assertEquals(oracleUserId, result.getUserId());
        Assert.assertEquals(MoveEnum.DICE_ROLLING, result.getMove());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRaceBetting() throws Exception {

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

        // Oracle values
        Long oracleUserId = (long) -1;
        Long oracleMoveId = (long) 1;

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(currentPlayerToken, MoveEnum.RACE_BETTING, null, null, Color.ORANGE, true, Color.ORANGE);
        GameMoveResponseBean result = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        Assert.assertNull(result.getDesertTileAsOasis());
        Assert.assertNull(result.getDesertTilePosition());
        Assert.assertNull(result.getLegBettingTile());
        Assert.assertNull(result.getDie());
        Assert.assertTrue(result.getRaceBettingOnWinner());
        Assert.assertEquals((long) oracleMoveId, (long) result.getId());
        Assert.assertEquals(gameResponse.getId(), result.getGameId());
        Assert.assertEquals(oracleUserId, result.getUserId());
        Assert.assertEquals(MoveEnum.RACE_BETTING, result.getMove());

        // Game contains exactly one move (after we added one move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(1, movesAfter.size());
    }

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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add move with Token of player who is not currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.LEG_BETTING, null, null, Color.BLUE, null, null);
        gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains no moves after Test (after we tried to add a move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesAfter.size());
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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add move with Token of player who is not currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.DESERT_TILE_PLACING, false, 7, null, null, null);
        gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains no moves after Test (after we tried to add a move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesAfter.size());
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

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add move with Token of player who is not currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);;
        gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains no moves after Test (after we tried to add a move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesAfter.size());
    }

    @Test(expected = NotYourTurnException.class)
    @SuppressWarnings("unchecked")
    public void testRaceBettingFail() throws Exception {

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

        // Oracle values
        Long oracleUserId = (long) -1;
        Long oracleMoveId = (long) 1;

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(!player.getUsername().equals(userResponse.getUsername())) {
                    notCurrentPlayerToken = playerToken;
                    oracleUserId = userResponse.getId();
                } else {
                    notCurrentPlayerToken = ownerToken;
                    oracleUserId = ownerResponse.getId();
                }
            }
        }

        // Game contains no moves after initialization (before we added any move)
        List<GameMoveResponseBean> movesBefore = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesBefore.size());

        // Add Move with player Token who is NOT currentPlayer
        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(notCurrentPlayerToken, MoveEnum.RACE_BETTING, null, null, Color.ORANGE, true, Color.ORANGE);
        GameMoveResponseBean result = gameMoveService.addMove(gameResponse.getId(), moveRequest);

        // Game contains still no move (after we tried to add a move)
        List<GameMoveResponseBean> movesAfter = gameMoveService.listMoves(gameResponse.getId());
        Assert.assertEquals(0, movesAfter.size());
    }
}

package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.game.Color;
import ch.uzh.ifi.seal.soprafs15.model.game.RaceBettingCard;
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
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    protected GameServiceController gameServiceController;

    @Autowired
    @InjectMocks
    protected UserServiceController userServiceController;

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
    public void testListGames() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        List<GameResponseBean> result = gameServiceController.listGames(null);

        Assert.assertEquals(1, userServiceController.listUsers().size());
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(userResponse.getId(), result.get(0).getId());
        Assert.assertEquals(gameRequest.getName(), result.get(0).getName());
        Assert.assertEquals(userRequest.getUsername(), result.get(0).getOwner());
        Assert.assertEquals(gameResponse.getCurrentPlayerId(), result.get(0).getCurrentPlayerId());
        Assert.assertEquals(0, (long) result.get(0).getNumberOfMoves());
        Assert.assertEquals(1, (long) result.get(0).getNumberOfPlayers());
        Assert.assertEquals(GameStatus.OPEN, result.get(0).getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddGame() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean result = gameServiceController.addGame(gameRequest);

        Assert.assertEquals(1, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(userResponse.getId(), result.getId());
        Assert.assertEquals(1, (long) result.getCurrentPlayerId());
        Assert.assertEquals(1, (long) result.getNumberOfPlayers());
        Assert.assertEquals(0, (long) result.getNumberOfMoves());
        Assert.assertEquals(userRequest.getUsername(), result.getOwner());
        Assert.assertEquals(gameRequest.getName(), result.getName());
        Assert.assertEquals(GameStatus.OPEN, result.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetGame() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        GameResponseBean result = gameServiceController.getGame(gameResponse.getId());

        Assert.assertNotNull(gameServiceController.getGame(gameResponse.getId()));
        Assert.assertEquals(1, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(userResponse.getId(), result.getId());
        Assert.assertEquals(1, (long) result.getCurrentPlayerId());
        Assert.assertEquals(1, (long) result.getNumberOfPlayers());
        Assert.assertEquals(0, (long) result.getNumberOfMoves());
        Assert.assertEquals(userRequest.getUsername(), result.getOwner());
        Assert.assertEquals(gameRequest.getName(), result.getName());
        Assert.assertEquals(GameStatus.OPEN, result.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStartGame() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean result = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        Assert.assertEquals(2, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(userResponse.getId(), result.getId());
        Assert.assertEquals(1, (long) result.getCurrentPlayerId());
        Assert.assertEquals(2, (long) result.getNumberOfPlayers());
        Assert.assertEquals(0, (long) result.getNumberOfMoves());
        Assert.assertEquals(userRequest.getUsername(), result.getOwner());
        Assert.assertEquals(gameRequest.getName(), result.getName());
        Assert.assertEquals(GameStatus.RUNNING, result.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStartFastMode() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        GamePlayerRequestBean startFastModeRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean result = gameServiceController.startFastMode(gameResponse.getId(), startFastModeRequest);

        Assert.assertEquals(6, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(userResponse.getId(), result.getId());
        Assert.assertEquals(1, (long) result.getCurrentPlayerId());
        Assert.assertEquals(5, (long) result.getNumberOfPlayers());
        Assert.assertEquals(userRequest.getUsername(), result.getOwner());
        Assert.assertEquals(gameRequest.getName(), result.getName());
        Assert.assertEquals(GameStatus.RUNNING, result.getStatus());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testTriggerMoveInFastMode() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        GamePlayerRequestBean startFastModeRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startFastModeResponse = gameServiceController.startFastMode(gameResponse.getId(), startFastModeRequest);

        Assert.assertEquals(6, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(gameResponse.getId(), startFastModeResponse.getId());
        Assert.assertEquals(1, (long) startFastModeResponse.getCurrentPlayerId());
        Assert.assertEquals(5, (long) startFastModeResponse.getNumberOfPlayers());
        Assert.assertEquals(userRequest.getUsername(), startFastModeResponse.getOwner());
        Assert.assertEquals(gameRequest.getName(), startFastModeResponse.getName());
        Assert.assertEquals(GameStatus.RUNNING, startFastModeResponse.getStatus());

        GamePlayerRequestBean stopGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean stopGameResponse = gameServiceController.stopGame(gameResponse.getId(), stopGameRequest);

        Integer numberOfMovesBefore = stopGameResponse.getNumberOfMoves();

        GamePlayerRequestBean triggerMoveRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameMoveResponseBean result = gameServiceController.triggerMoveInFastMode(gameResponse.getId(), triggerMoveRequest);

        Assert.assertEquals(numberOfMovesBefore + 1, (long) result.getId());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddMove() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        Assert.assertEquals(0, (long) startGameResponse.getNumberOfMoves());

        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        String firstPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    firstPlayerToken = loginResponse.getToken();
                } else {
                    firstPlayerToken = playerLoginResponse.getToken();
                }
            }
        }

        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(firstPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);
        GameMoveResponseBean result = gameServiceController.addMove(gameResponse.getId(), moveRequest);

        GameResponseBean game = gameServiceController.getGame(gameResponse.getId());

        Assert.assertEquals(1, (long) game.getNumberOfMoves());
        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(MoveEnum.DICE_ROLLING, result.getMove());
        Assert.assertEquals(gameResponse.getId(), result.getGameId());
        Assert.assertEquals(gameResponse.getCurrentPlayerId(), result.getPlayerId());
        Assert.assertNull(result.getDesertTileAsOasis());
        Assert.assertNull(result.getDesertTilePosition());
        Assert.assertNull(result.getLegBettingTile());
        Assert.assertNull(result.getRaceBettingOnWinner());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetMove() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        Assert.assertEquals(0, (long) startGameResponse.getNumberOfMoves());

        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        String firstPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    firstPlayerToken = loginResponse.getToken();
                } else {
                    firstPlayerToken = playerLoginResponse.getToken();
                }
            }
        }

        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(firstPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);
        GameMoveResponseBean moveResponse = gameServiceController.addMove(gameResponse.getId(), moveRequest);

        GameResponseBean game = gameServiceController.getGame(gameResponse.getId());

        GameMoveResponseBean result = gameServiceController.getMove(gameResponse.getId(), moveResponse.getId());

        Assert.assertEquals(1, (long) game.getNumberOfMoves());
        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(MoveEnum.DICE_ROLLING, result.getMove());
        Assert.assertEquals(gameResponse.getId(), result.getGameId());
        Assert.assertEquals(gameResponse.getCurrentPlayerId(), result.getPlayerId());
        Assert.assertNull(result.getDesertTileAsOasis());
        Assert.assertNull(result.getDesertTilePosition());
        Assert.assertNull(result.getLegBettingTile());
        Assert.assertNull(result.getRaceBettingOnWinner());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListMoves() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        Assert.assertEquals(0, (long) startGameResponse.getNumberOfMoves());
        Assert.assertEquals(0, gameServiceController.listMoves(gameResponse.getId()).size());

        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        String firstPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    firstPlayerToken = loginResponse.getToken();
                } else {
                    firstPlayerToken = playerLoginResponse.getToken();
                }
            }
        }

        GameMoveRequestBean moveRequest = TestUtils.toGameMoveRequestBean(firstPlayerToken, MoveEnum.DICE_ROLLING, null, null, null, null, null);
        GameMoveResponseBean moveResponse = gameServiceController.addMove(gameResponse.getId(), moveRequest);

        GameResponseBean game = gameServiceController.getGame(gameResponse.getId());

        List<GameMoveResponseBean> result = gameServiceController.listMoves(gameResponse.getId());

        Assert.assertEquals(1, gameServiceController.listMoves(gameResponse.getId()).size());
        Assert.assertEquals(1, (long) game.getNumberOfMoves());
        Assert.assertEquals(1, (long) result.get(0).getId());
        Assert.assertEquals(MoveEnum.DICE_ROLLING, result.get(0).getMove());
        Assert.assertEquals(gameResponse.getId(), result.get(0).getGameId());
        Assert.assertEquals(gameResponse.getCurrentPlayerId(), result.get(0).getPlayerId());
        Assert.assertNull(result.get(0).getDesertTileAsOasis());
        Assert.assertNull(result.get(0).getDesertTilePosition());
        Assert.assertNull(result.get(0).getLegBettingTile());
        Assert.assertNull(result.get(0).getRaceBettingOnWinner());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddPlayer() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean result = gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        Assert.assertEquals(2, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(playerResponse.getId(), result.getId());
        Assert.assertEquals(0, (long) result.getNumberOfMoves());
        Assert.assertEquals(gameResponse.getChannelName(), result.getChannelName());
        Assert.assertEquals(playerRequest.getUsername(), result.getUsername());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListPlayers() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean addPlayerResponse = gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        List<GamePlayerResponseBean> result = gameServiceController.listPlayers(gameResponse.getId());

        Assert.assertEquals(2, userServiceController.listUsers().size());
        Assert.assertEquals(1, gameServiceController.listGames(null).size());
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(userRequest.getUsername(), result.get(0).getUsername());
        Assert.assertEquals(playerRequest.getUsername(), result.get(1).getUsername());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetPlayer() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean addPlayerResponse = gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerResponseBean result = gameServiceController.getPlayer(gameResponse.getId(), addPlayerResponse.getPlayerId());

//        Assert.assertEquals(2, userServiceController.listUsers().size());
//        Assert.assertEquals(1, gameServiceController.listGames(null).size());
//        Assert.assertEquals(playerResponse.getId(), result.getId());
//        Assert.assertEquals(playerRequest.getUsername(), result.getUsername());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRemovePlayer() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        Assert.assertEquals(1, gameServiceController.listPlayers(gameResponse.getId()).size());

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        GameAddPlayerResponseBean addPlayerResponse = gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        Assert.assertEquals(2, gameServiceController.listPlayers(gameResponse.getId()).size());

        GamePlayerRequestBean removePlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.removePlayer(gameResponse.getId(), playerResponse.getId().intValue(), removePlayerRequest, true);

        Assert.assertEquals(1, gameServiceController.listPlayers(gameResponse.getId()).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingCards() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        Integer currentPlayerId = startGameResponse.getCurrentPlayerId();

        String firstPlayerToken = new String();
        List<GamePlayerResponseBean> players = gamePlayerService.listPlayer(gameResponse.getId());

        for(GamePlayerResponseBean player : players) {
            if(player.getPlayerId() == currentPlayerId) {
                if(player.getUsername().equals(userResponse.getUsername())) {
                    firstPlayerToken = loginResponse.getToken();
                } else {
                    firstPlayerToken = playerLoginResponse.getToken();
                }
            }
        }

        GamePlayerRequestBean getRBCsRequest = TestUtils.toGamePlayerRequestBean(firstPlayerToken);
        List<RaceBettingCard> result = gameServiceController.getRaceBettingCards(gameResponse.getId(), currentPlayerId, getRBCsRequest);

        Assert.assertEquals(5, result.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceTrack() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        GameRaceTrackResponseBean result = gameServiceController.getRaceTrack(gameResponse.getId());

        Assert.assertEquals(1, (long) result.getId());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetLegBettingArea() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        GameLegBettingAreaResponseBean result = gameServiceController.getLegBettingArea(gameResponse.getId());

        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(5, result.getTopLegBettingTiles().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetDiceArea() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        GameDiceAreaResponseBean result = gameServiceController.getDiceArea(gameResponse.getId());

        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(0, result.getRolledDice().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingArea() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(gameServiceController);
        Assert.assertEquals(0, userServiceController.listUsers().size());
        Assert.assertEquals(0, gameServiceController.listGames(null).size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Olivia");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", loginResponse.getToken());
        GameCreateResponseBean gameResponse = gameServiceController.addGame(gameRequest);

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(10, "Tanja");
        UserResponseBean playerResponse = userServiceController.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = userServiceController.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
        gameServiceController.addPlayer(gameResponse.getId(), addPlayerRequest);

        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(loginResponse.getToken());
        GameResponseBean startGameResponse = gameServiceController.startGame(gameResponse.getId(), startGameRequest);

        GameRaceBettingAreaResponseBean result = gameServiceController.getRaceBettingArea(gameResponse.getId());

        Assert.assertEquals(1, (long) result.getId());
        Assert.assertEquals(0, (long) result.getNrOfLoserBetting());
        Assert.assertEquals(0, (long) result.getNrOfWinnerBetting());
    }

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

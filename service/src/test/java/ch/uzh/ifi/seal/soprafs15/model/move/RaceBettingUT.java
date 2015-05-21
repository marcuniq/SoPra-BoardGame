package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.game.GameActionService;
import ch.uzh.ifi.seal.soprafs15.service.game.GameMoveService;
import ch.uzh.ifi.seal.soprafs15.service.game.GamePlayerService;
import ch.uzh.ifi.seal.soprafs15.service.game.GameService;
import ch.uzh.ifi.seal.soprafs15.service.user.UserService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Created by Hakuna on 04.05.2015.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RaceBettingUT {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private MoveRepository moveRepository;

    @InjectMocks
    @Autowired
    protected UserService userService;

    @InjectMocks
    @Autowired
    protected GameService gameService;

    @InjectMocks
    @Autowired
    protected GamePlayerService gamePlayerService;

    @InjectMocks
    @Autowired
    protected GameActionService gameActionService;

    @InjectMocks
    @Autowired
    protected GameMoveService gameMoveService;

//    @Test
//    public void testIsValid() throws Exception {
//
//        // set up a race betting card
//        RaceBettingCard rbc = new RaceBettingCard();
//        rbc.setId((long) 1);
//        rbc.setColor(Color.GREEN);
//
//        // set up a test user
//        User testUser = new User();
//        testUser.setId((long) 1);
//        testUser.setUsername("Heinrich");
//        testUser.setAge(99);
//        testUser.setRaceBettingCards(Map<Color.GREEN, rbc>);
//
//        // set up a race betting area
//        RaceBettingArea rba = new RaceBettingArea();
//        rba.setId((long) 1);
//
//        // set up a race betting card
//        RaceBettingCard rbc = new RaceBettingCard();
//        rbc.setId((long) 1);
//        rbc.setColor(Color.GREEN);
//
//        // set up a game state
//        GameState testState = new GameState();
//        testState.setId((long) 1);
//        testState.setRaceBettingArea(rba);
//
//        // set up a state manager
//        StateManager testManager = new StateManager();
//        testManager.setId((long) 1);
//        testManager.setGameState(testState);
//
//        // set up a test game
//        Game testGame = new Game();
//        testGame.setId((long) 1);
//        testGame.setOwner(testUser.getUsername());
//        testGame.setStateManager(testManager);
//
//        // set up a race betting
//        RaceBetting rb = new RaceBetting();
//        rb.setColor(Color.GREEN);
//
//        Assert.assertEquals(true, rb.isValid());
//
//    }

//    @Test
//    public void testIsValid() throws Exception {
//        // Create Owner
//        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(33, "Ueli");
//        UserResponseBean ownerResponse = userService.addUser(ownerRequest);
//
//        // Login Owner
//        UserLoginLogoutResponseBean ownerLoginResponse = userService.login(ownerResponse.getId());
//
//        // Create Player
//        UserRequestBean playerRequest = TestUtils.toUserRequestBean(29, "Röbi");
//        UserResponseBean playerResponse = userService.addUser(playerRequest);
//
//        // Login Player
//        UserLoginLogoutResponseBean playerLoginResponse = userService.login(playerResponse.getId());
//
//        // Create Game
//        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestSpiel", ownerLoginResponse.getToken());
//        GameCreateResponseBean gameResponse = gameService.addGame(gameRequest);
//
//        // Add Player to game
//        GamePlayerRequestBean addPlayerRequest = TestUtils.toGamePlayerRequestBean(playerLoginResponse.getToken());
//        GameAddPlayerResponseBean addPlayerResponse = gamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);
//
//        // Start game
//        GamePlayerRequestBean startGameRequest = TestUtils.toGamePlayerRequestBean(ownerLoginResponse.getToken());
//        GameResponseBean startGameResponse = gameActionService.startGame(gameResponse.getId(), startGameRequest);
//
//
//
//        // Add 1st move
//        GameMoveRequestBean firstMoveRequest = TestUtils.toGameMoveRequestBean()
//        //gameMoveService.addMove(gameResponse.getId(), )
//    }
}

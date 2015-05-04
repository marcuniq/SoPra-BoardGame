package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.*;
import ch.uzh.ifi.seal.soprafs15.model.move.DesertTilePlacing;
import ch.uzh.ifi.seal.soprafs15.model.move.DiceRolling;
import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

//Load Spring context
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameMapperServiceUT {

    //Create MockRepo
    @Mock
    private UserRepository mockUserRepo;
    @Mock
    private GameRepository mockGameRepo;
    @Mock
    private MoveRepository mockMoveRepo;

    @InjectMocks
    @Autowired
    private GameMapperService testMapperService;

    @InjectMocks
    @Autowired
    private UserService testUserService;

    @Test
    @SuppressWarnings("unchecked")
    public void testToGame() throws Exception {

        //oracle objects
        Game oracleGame = new Game();
        oracleGame.setName("testGame");
        oracleGame.setOwner("owner");

        //Assert testUserService has been initialized, create/add User, log him in and create GameRequestBean
        assertNotNull(testUserService);
        UserResponseBean userResponse = testUserService.addUser(TestUtils.toUserRequestBean(10,"owner"));
        UserLoginLogoutResponseBean llResponse = testUserService.login(userResponse.getId());
        GameRequestBean gameRequestBean = TestUtils.toGameRequestBean("testGame", llResponse.getToken());

        //Assert testMapperService has been initialized and call method to be tested
        assertNotNull(testMapperService);
        Game result = testMapperService.toGame(gameRequestBean);

        //Assertions
        assertEquals(oracleGame.getName(), result.getName());
        assertEquals(oracleGame.getOwner(), result.getOwner());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameResponseBean() throws Exception {

        //oracle values
        Long oracleId = (long) 1;
        String oracleName = "testGame";
        String oracleOwner = "testOwner";
        GameStatus oracleStatus = GameStatus.OPEN;
        Integer oracleNumberOfMoves = 1;
        Integer oracleNumberOfPlayers = 1;

        //create object to test with
        Game testGame = new Game();
        testGame.setId(oracleId);
        testGame.setName(oracleName);
        testGame.setOwner(oracleOwner);
        testGame.setStatus(oracleStatus);

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        GameResponseBean result = testMapperService.toGameResponseBean(testGame);

        //Assertions
        assertEquals(oracleId, result.getId());
        assertEquals(oracleName, result.getName());
        assertEquals(oracleOwner, result.getOwner());
        assertEquals(oracleStatus, result.getStatus());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameCreateResponseBean() throws Exception {

        //oracle values
        Long oracleId = (long) 1;
        String oracleName = "testGame";
        String oracleOwner = "testOwner";
        GameStatus oracleStatus = GameStatus.OPEN;
        Integer oracleNumberOfMoves = 1;
        Integer oracleNumberOfPlayers = 1;
        String oracleChannelName = "testChannel";

        //create object to test with
        Game testGame = new Game();
        testGame.setId(oracleId);
        testGame.setName(oracleName);
        testGame.setOwner(oracleOwner);
        testGame.setStatus(oracleStatus);
        testGame.setPusherChannelName(oracleChannelName);

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        GameCreateResponseBean result = testMapperService.toGameCreateResponseBean(testGame);

        //Assertions
        assertEquals(oracleId, result.getId());
        assertEquals(oracleName, result.getName());
        assertEquals(oracleOwner, result.getOwner());
        // following assertion is omitted for the time being, because it's not implemented yet
        // assertEquals(oracleStatus, result.getStatus());
        assertEquals(oracleChannelName, result.getChannelName());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameResponseBeanList() throws Exception {

        //oracle values
        Long oracleId = (long) 1;
        String oracleName = "testGame";
        String oracleOwner = "testOwner";
        GameStatus oracleStatus = GameStatus.OPEN;
        Integer oracleNumberOfMoves = 1;
        Integer oracleNumberOfPlayers = 1;

        //create object to test with
        Game testGame = new Game();
        testGame.setId(oracleId);
        testGame.setName(oracleName);
        testGame.setOwner(oracleOwner);
        testGame.setStatus(oracleStatus);
        List<Game> testList = new ArrayList<>();
        testList.add(testGame);


        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        List<GameResponseBean> result = testMapperService.toGameResponseBean(testList);

        //Assertions
        assertEquals(oracleId, result.get(0).getId());
        assertEquals(oracleName, result.get(0).getName());
        assertEquals(oracleOwner, result.get(0).getOwner());
        assertEquals(oracleStatus, result.get(0).getStatus());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToUser() throws Exception {
        
        //oracle values
        int oracleAge = 10;
        String oracleUsername = "testOwner2";
        
        //Assert testUserService has been initialized, create/add User
        assertNotNull(testUserService);
        UserResponseBean userResponse = testUserService.addUser(TestUtils.toUserRequestBean(oracleAge, oracleUsername));
        
        //login newly created User, create a GamePlayerRequestBean, and set the token
        UserLoginLogoutResponseBean llResponse = testUserService.login(userResponse.getId());
        
        GamePlayerRequestBean testRequest = new GamePlayerRequestBean();
        testRequest.setToken(llResponse.getToken());

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        User result = testMapperService.toUser(testRequest);
        
        //Assertions
        assertEquals(oracleAge,(int) result.getAge());
        assertEquals(oracleUsername, result.getUsername());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGamePlayerResponseBean() throws Exception {

        assertNotNull(testMapperService);

        List moves = new ArrayList<Move>();

        //oracle values
        Long oracleId = (long) 1;
        //Integer oracleNumberOfMoves = 0;
        Integer oracleNumberOfMoves = moves.size();

        User player = new User();
        player.setId((long) 1);
        player.setAge(86);
        player.setMoves(moves);

        GamePlayerResponseBean result = testMapperService.toGamePlayerResponseBean(player);

        assertEquals(player.getId(), result.getId());
        assertEquals(oracleNumberOfMoves, result.getNumberOfMoves());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGamePlayerResponseBean1() throws Exception {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToMoveDesertTilePlacing() throws Exception {

        //set up testing objects (arguments in the method to be tested)
        Game testGame = new Game();
        testGame.setName("testGame");
        testGame.setOwner("testOwner");

        User testUser = new User();
        testUser.setId((long) 1);
        testUser.setUsername("testUser");
        testUser.setAge(15);
        testUser.setToken("testToken");

        GameMoveRequestBean testRequest = new GameMoveRequestBean();
        testRequest.setToken("testToken");
        testRequest.setMove(MoveEnum.DESERT_TILE_PLACING);
        testRequest.setDesertTileAsOasis(true);
        testRequest.setDesertTilePosition(5);

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        DesertTilePlacing result = (DesertTilePlacing) testMapperService.toMove(testGame, testUser, testRequest);


        //Assertions
        assertEquals(testGame.getName(), result.getGame().getName());
        assertEquals(testGame.getOwner(), result.getGame().getOwner());
        assertEquals(testUser.getAge(), result.getUser().getAge());
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testUser.getToken(), result.getUser().getToken());
        assertEquals(testRequest.getDesertTileAsOasis(), result.getIsOasis());
        assertEquals(testRequest.getDesertTilePosition(), result.getPosition());
        assertEquals(DesertTilePlacing.class, result.getClass());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToMoveDiceRolling () throws Exception{

        //set up testing objects (arguments in the method to be tested)
        Game testGame = new Game();
        testGame.setName("testGame");
        testGame.setOwner("testOwner");

        User testUser = new User();
        testUser.setId((long) 1);
        testUser.setUsername("testUser");
        testUser.setAge(15);
        testUser.setToken("testToken");

        GameMoveRequestBean testRequest = new GameMoveRequestBean();
        testRequest.setToken("testToken");
        testRequest.setMove(MoveEnum.DICE_ROLLING);

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        DiceRolling result = (DiceRolling) testMapperService.toMove(testGame, testUser, testRequest);


        //Assertions
        assertEquals(testGame.getName(), result.getGame().getName());
        assertEquals(testGame.getOwner(), result.getGame().getOwner());
        assertEquals(testUser.getAge(), result.getUser().getAge());
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testUser.getToken(), result.getUser().getToken());
        assertEquals(DiceRolling.class, result.getClass());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameMoveResponseBeanDiceRolling() throws Exception {

        //set up testing objects (arguments in the method to be tested)

        Game testGame = new Game();
        testGame.setId((long)1);
        testGame.setName("testName");

        User testUser = new User();
        testUser.setId((long) 1);
        testUser.setToken("testToken");
        testUser.setAge(10);

        Die testDie = new Die();
        testDie.setColor(Color.BLUE);

        DiceRolling testMove = new DiceRolling();
        testMove.setDie(testDie);
        testMove.setGame(testGame);
        testMove.setUser(testUser);

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        GameMoveResponseBean result = testMapperService.toGameMoveResponseBean(testMove);

        //Assertions
        assertEquals(MoveEnum.DICE_ROLLING, result.getMove());
        assertEquals(testDie.getColor(), result.getDie().getColor());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameMoveResponseBeanList() throws Exception {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameAddPlayerResponseBean() throws Exception {

        //set up testing objects (arguments in the method to be tested)
        User player = new User();
        player.setUsername("testOwner");

        Game testGame = new Game();
        testGame.setOwner(player.getUsername());
        testGame.setName("testName");
        testGame.setPusherChannelName("pusherChannelName");
        testGame.setId((long)1);

        //Assert testMapperService has been initialized and call method to test
        assertNotNull(testMapperService);
        GameAddPlayerResponseBean result = testMapperService.toGameAddPlayerResponseBean(player, testGame);

        //Assertions
        assertEquals(testGame.getPusherChannelName(), result.getChannelName());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToRaceTrackResponseBean() throws Exception {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameLegBettingAreaResponseBean() throws Exception {

        assertNotNull(testMapperService);

        //Map<Color, LegBettingTileStack> map = new Map<Color, LegBettingTileStack>();

        LegBettingArea legBettingArea = new LegBettingArea();
        legBettingArea.setId((long) 1);
        //legBettingArea.setLegBettingTiles();

        GameLegBettingAreaResponseBean result = testMapperService.toGameLegBettingAreaResponseBean(legBettingArea);

        assertEquals(legBettingArea.getId(), result.getId());
        assertEquals(legBettingArea.topLegBettingTiles(), result.getTopLegBettingTiles());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameRaceBettingAreaResponseBean() throws Exception {

        assertNotNull(testMapperService);

        List<RaceBettingCard> loserBets = new ArrayList<RaceBettingCard>();
        List<RaceBettingCard> winnerBets = new ArrayList<RaceBettingCard>();

        RaceBettingCardStack loserStack = new RaceBettingCardStack();
        loserStack.setStack(loserBets);
        RaceBettingCardStack winnerStack = new RaceBettingCardStack();
        winnerStack.setStack(winnerBets);

        Integer oracleLoserSize = loserBets.size();
        Integer oracleWinnerSize = winnerBets.size();

        RaceBettingArea raceBettingArea = new RaceBettingArea();
        raceBettingArea.setId((long) 1);
        raceBettingArea.setLoserBetting(loserStack);
        raceBettingArea.setWinnerBetting(winnerStack);

        GameRaceBettingAreaResponseBean result = testMapperService.toGameRaceBettingAreaResponseBean(raceBettingArea);

        assertEquals(raceBettingArea.getId(), result.getId());
        assertEquals(raceBettingArea.getNrOfLoserBetting(), result.getNrOfLoserBetting());
        assertEquals(raceBettingArea.getNrOfWinnerBetting(), result.getNrOfWinnerBetting());
        assertEquals(oracleLoserSize, result.getNrOfLoserBetting());
        assertEquals(oracleWinnerSize, result.getNrOfWinnerBetting());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToGameDiceAreaResponseBean() throws Exception {

        assertNotNull(testMapperService);

        List<Die> dice = new ArrayList<Die>();

        DiceArea diceArea = new DiceArea();
        diceArea.setId((long) 1);
        diceArea.setRolledDice(dice);

        GameDiceAreaResponseBean result = testMapperService.toGameDiceAreaResponseBean(diceArea);

        assertEquals(diceArea.getId(), result.getId());
        assertEquals(diceArea.getRolledDice(), result.getRolledDice());
    }
}
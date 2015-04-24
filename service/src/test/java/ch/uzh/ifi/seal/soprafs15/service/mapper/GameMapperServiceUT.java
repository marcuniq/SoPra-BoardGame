package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
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
    public void testToGamePlayerResponseBean() throws Exception {

        //oracle values
        Long oracleId = (long) 0;
        Integer oracleNumberOfMoves = 0;




    }

    @Test
    public void testToGamePlayerResponseBean1() throws Exception {

    }

    @Test
    public void testToMove() throws Exception {

    }

    @Test
    public void testToGameMoveResponseBean() throws Exception {

    }

    @Test
    public void testToGameMoveResponseBean1() throws Exception {

    }

    @Test
    public void testToGameAddPlayerResponseBean() throws Exception {

    }

    @Test
    public void testToRaceTrackResponseBean() throws Exception {

    }

    @Test
    public void testToGameLegBettingAreaResponseBean() throws Exception {

    }

    @Test
    public void testToGameRaceBettingAreaResponseBean() throws Exception {

    }

    @Test
    public void testToGameDiceAreaResponseBean() throws Exception {

    }
}
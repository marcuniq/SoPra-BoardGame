package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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
    private GameMapperService gameMapperService;

    @Autowired
    @InjectMocks
    private GameAreaService testService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceTrack() throws Exception {

        //oracle values
        List<GameRaceTrackObjectResponseBean> oracleFields = new ArrayList<>();
        GameRaceTrackObjectResponseBean fieldResponse1 = new GameRaceTrackObjectResponseBean();
        //TODO: initialize a testing field to check afterwards

        //create new user and log him in (also assert that UserService instance has been initialized)
        UserRequestBean userRequestBean = TestUtils.toUserRequestBean(20,"testUser");
        assertNotNull(userService);
        UserResponseBean userResponseBean = userService.addUser(userRequestBean);
        UserLoginLogoutResponseBean llResponseBean = userService.login(userResponseBean.getId());

        //create new game (and assert GameService instance has been initialized)
        GameRequestBean gameRequestBean = TestUtils.toGameRequestBean("testGame",llResponseBean.getToken());
        assertNotNull(gameService);
        GameResponseBean gameResponseBean = gameService.addGame(gameRequestBean);

        //argument that we pass on to the method that we are testing
        long testGameId = gameResponseBean.getId();

        //Assert that instance of GameAreaService has been initialized and call method to be tested
        assertNotNull(testService);
        GameRaceTrackResponseBean result = testService.getRaceTrack(testGameId);

        //Assertions
        assertEquals(testGameId, (long) result.getGameId());
        //TODO: assert that fields match

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetLegBettingArea() throws Exception {

        //create new user and log him in (also assert that UserService instance has been initialized)
        UserRequestBean userRequestBean = TestUtils.toUserRequestBean(20,"testUser");
        assertNotNull(userService);
        UserResponseBean userResponseBean = userService.addUser(userRequestBean);
        UserLoginLogoutResponseBean llResponseBean = userService.login(userResponseBean.getId());

        //create new game (and assert GameService instance has been initialized)
        GameRequestBean gameRequestBean = TestUtils.toGameRequestBean("testGame",llResponseBean.getToken());
        assertNotNull(gameService);
        GameResponseBean gameResponseBean = gameService.addGame(gameRequestBean);

        //argument that we pass on to the method that we are testing
        long testGameId = gameResponseBean.getId();

        //Assert that instance of GameAreaService has been initialized and call method to be tested
        assertNotNull(testService);
        GameLegBettingAreaResponseBean result = testService.getLegBettingArea(testGameId);

        //Assertions
        //TODO: assert that TopLegBettingTiles and ID's match

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetRaceBettingArea() throws Exception {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetDiceArea() throws Exception {

    }
}
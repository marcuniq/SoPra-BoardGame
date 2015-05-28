package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.game.GameLogicService;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.pusher.PusherService;
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

import java.util.Map;

import static org.junit.Assert.*;

//Load Spring context
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class GameLogicServiceTest {

    @Mock
    private UserRepository mockUserRepo;

    @Mock
    private GameRepository mockGameRepo;

    @Mock
    private MoveRepository moveRepository;

    @Autowired
    @InjectMocks
    private GameMapperService gameMapperService;

    @Autowired
    @InjectMocks
    private PusherService pusherService;

    @Autowired
    @InjectMocks
    private GameLogicService testService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePlayerSequence() throws Exception {

        //set up test objects to pass on as a argument for the tested method
        User testUser1 = new User();
        testUser1.setAge(15);
        testUser1.setUsername("Rudolf");
        testUser1.setId((long)1);

        User testUser2 = new User();
        testUser2.setAge(89);
        testUser2.setUsername("Gerald");
        testUser2.setId((long)2);

        Game testGame = new Game();
        testGame.setId((long) 1);
        testGame.setName("testGame");
        testGame.addPlayer(testUser1);
        testGame.addPlayer(testUser2);

        //assert that the service that is undergoing testing is initialized and call method to be tested
        assertNotNull(testService);
        Map<Long, Integer> result = testService.createPlayerSequence(testGame);

        //Assertions
        assertTrue(result.containsKey((long)1));
        assertTrue(result.containsKey((long)2));
        assertTrue(result.containsValue(1));
        assertTrue(result.containsValue(2));
        assertTrue(result.get((long)1).equals(1) || result.get((long)1).equals(2));
        assertTrue(result.get((long)2).equals(1) || result.get((long)2).equals(2));

    }

    @Test
    public void testProcessMove() throws Exception {



    }

    @Test
    public void testStartFastMode() throws Exception {

    }

    @Test
    public void testStopFastMode() throws Exception {

    }

    @Test
    public void testTriggerMoveInFastMode() throws Exception {

    }
}
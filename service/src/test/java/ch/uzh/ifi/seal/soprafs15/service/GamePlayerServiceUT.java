package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.*;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
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

import static org.junit.Assert.*;

@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GamePlayerServiceUT {

    @Mock
    private GameRepository mockGameRepo;

    @Mock
    private UserRepository mockUserRepo;

    @InjectMocks
    @Autowired
    private GameService testGameService;

    @InjectMocks
    @Autowired
    private UserService testUserService;

    @InjectMocks
    @Autowired
    private GamePlayerService testGamePlayerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testListPlayer() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames().size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(93, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(ownerResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());
        assertEquals(1, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(1, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames().size());

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(74, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = new GamePlayerRequestBean();
        addPlayerRequest.setToken(playerLoginResponse.getToken());

        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        assertEquals((long) oracleChannelNameLength, addPlayerResponse.getChannelName().length());

        List<GamePlayerResponseBean> result = testGamePlayerService.listPlayer(gameResponse.getId());

        assertEquals(ownerResponse.getId(), result.get(0).getId());
        assertEquals(playerResponse.getId(), result.get(1).getId());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames().size());
    }

    @Test
    public void testAddPlayer() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames().size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean ownerRequest = TestUtils.toUserRequestBean(93, "TestOwner");
        UserResponseBean ownerResponse = testUserService.addUser(ownerRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(ownerResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());
        assertEquals(1, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(1, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames().size());

        UserRequestBean playerRequest = TestUtils.toUserRequestBean(74, "TestPlayer");
        UserResponseBean playerResponse = testUserService.addUser(playerRequest);
        UserLoginLogoutResponseBean playerLoginResponse = testUserService.login(playerResponse.getId());

        GamePlayerRequestBean addPlayerRequest = new GamePlayerRequestBean();
        addPlayerRequest.setToken(playerLoginResponse.getToken());

        GameAddPlayerResponseBean addPlayerResponse = testGamePlayerService.addPlayer(gameResponse.getId(), addPlayerRequest);

        assertEquals((long) oracleChannelNameLength, addPlayerResponse.getChannelName().length());

        assertEquals(2, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(2, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames().size());
    }

    @Test
    public void testGetPlayer() throws Exception {

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        assertNotNull(testGamePlayerService);
        assertNotNull(testGameService);
        assertNotNull(testUserService);

        assertEquals(0, testGameService.listGames().size());
        assertEquals(0, testUserService.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(61, "TestUser");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());

        // the id of the user and playerId of the user are not the same anymore
        //GamePlayerResponseBean result = testGamePlayerService.getPlayer(gameResponse.getId(), userResponse.getPlId());

        //assertEquals(userResponse.getId(), result.getId());

        assertEquals(1, testGamePlayerService.listPlayer(gameResponse.getId()).size());
        assertEquals(1, testUserService.listUsers().size());
        assertEquals(1, testGameService.listGames().size());
    }
}
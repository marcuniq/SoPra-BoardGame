package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCreateResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.GameNotFoundException;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakuna on 24.04.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameServiceUT {

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = GameNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetGameFail() throws Exception {

        Assert.assertEquals(0, testGameService.listGames().size());

        Assert.assertNotNull(testGameService);

        testGameService.getGame((long) 1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListGames() throws Exception {

        Assert.assertEquals(0, testGameService.listGames().size());
        Assert.assertEquals(0, testUserService.listUsers().size());

        List<GameResponseBean> oracleResponseList = new ArrayList<GameResponseBean>();
        oracleResponseList.add(TestUtils.toGameResponseBean((long) 1, "TestGame", "TestOwner"));

        Assert.assertNotNull(testGameService);
        Assert.assertNotNull(testUserService);

        UserRequestBean userRequest = TestUtils.toUserRequestBean(22, "TestOwner");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        testGameService.addGame(TestUtils.toGameRequestBean("TestGame", loginResponse.getToken()));
        List<GameResponseBean> result = testGameService.listGames();

        Assert.assertEquals(oracleResponseList.get(0).getId(), result.get(0).getId());
        Assert.assertEquals(oracleResponseList.get(0).getName(), result.get(0).getName());
        Assert.assertEquals(oracleResponseList.get(0).getOwner(), result.get(0).getOwner());
        Assert.assertEquals(oracleResponseList.size(), result.size());

        Assert.assertEquals(1, testGameService.listGames().size());
        Assert.assertEquals(1, testUserService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddGame() throws Exception {

        Assert.assertEquals(0, testGameService.listGames().size());
        Assert.assertEquals(0, testUserService.listUsers().size());

        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        Assert.assertNotNull(testUserService);
        Assert.assertNotNull(testGameService);

        UserRequestBean userRequest = TestUtils.toUserRequestBean(88, "Hanfred");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        Assert.assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());

        Assert.assertEquals(1, testGameService.listGames().size());
        Assert.assertEquals(1, testUserService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetGame() throws Exception {

        Assert.assertEquals(0, testGameService.listGames().size());
        Assert.assertEquals(0, testUserService.listUsers().size());

        GameResponseBean oracleResponse = new GameResponseBean();
        oracleResponse.setOwner("TestOwner");
        oracleResponse.setName("TestUser");
        oracleResponse.setId((long) 1);
        Long oracleChannelNameLength = (long) "9b5eabcc-781b-483a-8eed-30d7eacb1567".length();

        Assert.assertNotNull(testGameService);
        Assert.assertNotNull(testUserService);

        UserRequestBean userRequest = TestUtils.toUserRequestBean(66, "TestOwner");
        UserResponseBean userResponse = testUserService.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        GameRequestBean gameRequest = TestUtils.toGameRequestBean("TestGame", loginResponse.getToken());
        GameCreateResponseBean gameResponse = testGameService.addGame(gameRequest);

        testGameService.getGame(gameResponse.getId());

        Assert.assertEquals(userRequest.getUsername(), gameResponse.getOwner());
        Assert.assertEquals(gameRequest.getName(), gameResponse.getName());
        Assert.assertEquals(oracleResponse.getId(), gameResponse.getId());
        Assert.assertEquals((long) oracleChannelNameLength, gameResponse.getChannelName().length());

        Assert.assertEquals(1, testGameService.listGames().size());
        Assert.assertEquals(1, testUserService.listUsers().size());
    }
}

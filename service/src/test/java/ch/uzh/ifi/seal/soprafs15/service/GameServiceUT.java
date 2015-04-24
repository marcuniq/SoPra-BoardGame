package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCreateResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
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

    @Test
    public void testListGames() {

        List<GameResponseBean> oracleResponseList = new ArrayList<GameResponseBean>();
        oracleResponseList.add(TestUtils.toGameResponseBean((long) 1, "TestGame", "TestOwner"));

        Assert.assertNotNull(testGameService);
        Assert.assertNotNull(testUserService);

        testUserService.addUser

        testGameService.addGame(TestUtils.toGameRequestBean("TestGame", "TestOwner"));

        List<GameResponseBean> result = testGameService.listGames();

        Assert.assertEquals(oracleResponseList.get(0).getId(), result.get(0).getId());
        Assert.assertEquals(oracleResponseList.get(0).getName(), result.get(0).getName());
        Assert.assertEquals(oracleResponseList.get(0).getOwner(), result.get(0).getOwner());
        Assert.assertEquals(oracleResponseList.size(), result.size());
    }
}

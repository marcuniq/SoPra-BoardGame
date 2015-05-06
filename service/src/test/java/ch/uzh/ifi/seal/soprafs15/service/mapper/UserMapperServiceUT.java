package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakuna on 14.04.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserMapperServiceUT {

    protected UserRepository userRepository;

    protected UserMapperService userMapperService = new UserMapperServiceImpl(userRepository);

    @Test
    @SuppressWarnings("unchecked")
    public void testToUser() throws Exception {
        UserRequestBean testBean = new UserRequestBean();
        testBean.setAge(20);
        testBean.setUsername("TestUsername");


        User oracleUser = new User();
        oracleUser.setUsername("TestUsername");
        oracleUser.setAge(20);
        oracleUser.setStatus(UserStatus.OFFLINE);
        String oracleToken = "067e6162-3b6f-4ae2-a171-2470b63dff00";

        User result = userMapperService.toUser(testBean);

        Assert.assertEquals(result.getAge(), oracleUser.getAge());
        Assert.assertEquals(result.getUsername(), oracleUser.getUsername());
        Assert.assertEquals(result.getStatus(), oracleUser.getStatus());
        Assert.assertEquals(result.getToken().length(), oracleToken.length());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToUserResponseBean() throws Exception {


        User testUser = new User();
        testUser.setAge(30);
        testUser.setUsername("TestUser");
        //testUser.setGame(testGame);
        testUser.setId((long) 0);

        Game testGame = new Game();
        testGame.setName("TestGame");
        testGame.setOwner(testUser);
        testGame.setId((long) 0);

        UserResponseBean oracleUserResponseBean = new UserResponseBean();
        oracleUserResponseBean.setUsername("TestUser");
        oracleUserResponseBean.setAge(30);
        oracleUserResponseBean.setGame("TestGame");
        oracleUserResponseBean.setId((long) 0);

        UserResponseBean result = userMapperService.toUserResponseBean(testUser);

        Assert.assertEquals(result.getUsername(), oracleUserResponseBean.getUsername());
        Assert.assertEquals(result.getAge(), oracleUserResponseBean.getAge());
        //Assert.assertEquals(result.getGame(), oracleUserResponseBean.getGame());
        Assert.assertEquals(result.getId(), oracleUserResponseBean.getId());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToUserResponseBeanList() throws Exception {


        User testUser0 = new User();
        User testUser1 = new User();

        Game testGame = new Game();
        testGame.setName("TestGame");
        testGame.setOwner(testUser0);
        testGame.setId((long) 0);

        List<User> testList = new ArrayList();
        testList.add(testUser0);
        testList.add(testUser1);

        Integer count = 0;

        for(User user : testList) {
            user.setAge(30 + count);
            user.setUsername("TestUsername" + count.toString());
            user.setId((long)0 + count);
            if(count % 2 == 0) {
                //user.setGame(testGame);
            }
            count++;
        }

        UserResponseBean oracleUserResponseBean0 = new UserResponseBean();
        UserResponseBean oracleUserResponseBean1 = new UserResponseBean();

        List<UserResponseBean> oracleList = new ArrayList();
        oracleList.add(oracleUserResponseBean0);
        oracleList.add(oracleUserResponseBean1);

        count = 0;

        for(UserResponseBean userResponseBean : oracleList) {
            userResponseBean.setAge(30 + count);
            userResponseBean.setUsername("TestUsername" + count.toString());
            userResponseBean.setId((long)0 + count);
            if(count % 2 == 0) {
                userResponseBean.setGame("TestGame");
            }
            count++;
        }

        List<UserResponseBean> result = userMapperService.toUserResponseBean(testList);

        count = 0;

        for(UserResponseBean userResponseBean : result) {
            Assert.assertEquals(userResponseBean.getAge(), oracleList.get(count).getAge());
            Assert.assertEquals(userResponseBean.getId(), oracleList.get(count).getId());
            //Assert.assertEquals(userResponseBean.getGame(), oracleList.get(count).getGame());
            Assert.assertEquals(userResponseBean.getUsername(), oracleList.get(count).getUsername());

            count++;
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToLLResponseBean() throws Exception {
        User testUser = new User();
        testUser.setToken("TestToken");

        UserLoginLogoutResponseBean oracleBean = new UserLoginLogoutResponseBean();
        oracleBean.setToken("TestToken");

        UserLoginLogoutResponseBean result = userMapperService.toLLResponseBean(testUser);

        Assert.assertEquals(result.getToken(), oracleBean.getToken());
    }
}

package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hakuna on 14.04.2015.
 */
public class UserMapperServiceUT {

    protected UserRepository userRepository;

    protected UserMapperService userMapperService = new UserMapperServiceImpl(userRepository);

    @Test
    public void testToUser() {
        UserRequestBean testBean = new UserRequestBean();
        testBean.setAge(20);
        testBean.setUsername("TestUsername");

        User oracleUser = new User();
        oracleUser.setUsername("TestUsername");
        oracleUser.setAge(20);
        oracleUser.setStatus(UserStatus.OFFLINE);

        User result = userMapperService.toUser(testBean);

        Assert.assertEquals(result.getAge(), oracleUser.getAge());
        Assert.assertEquals(result.getUsername(), oracleUser.getUsername());
        Assert.assertEquals(result.getStatus(), oracleUser.getStatus());
    }

    @Test
    public void testToUserResponseBean() {
        Game testGame = new Game();
        testGame.setName("TestGame");
        testGame.setOwner("TestOwner");
        testGame.setId((long)0);

        User testUser = new User();
        testUser.setAge(30);
        testUser.setUsername("TestUser");
        testUser.setGame(testGame);
        testUser.setId((long)0);

        UserResponseBean oracleUserResponseBean = new UserResponseBean();
        oracleUserResponseBean.setUsername("TestUser");
        oracleUserResponseBean.setAge(30);
        oracleUserResponseBean.setGame("TestGame");
        oracleUserResponseBean.setId((long) 0);

        UserResponseBean result = userMapperService.toUserResponseBean(testUser);

        Assert.assertEquals(result.getUsername(), oracleUserResponseBean.getUsername());
        Assert.assertEquals(result.getAge(), oracleUserResponseBean.getAge());
        Assert.assertEquals(result.getGame(), oracleUserResponseBean.getGame());
        Assert.assertEquals(result.getId(), oracleUserResponseBean.getId());
    }

    @Test
    public void testToUserResponseBeanList() {
        Game testGame = new Game();
        testGame.setName("TestGame");
        testGame.setOwner("TestOwner");
        testGame.setId((long)0);

        User testUser0 = new User();
        User testUser1 = new User();

        List<User> testList = new ArrayList();
        testList.add(testUser0);
        testList.add(testUser1);

        Integer count = 0;

        for(User user : testList) {
            user.setAge(30 + count);
            user.setUsername("TestUsername" + count.toString());
            user.setId((long)0 + count);
            if(count % 2 == 0) {
                user.setGame(testGame);
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
            Assert.assertEquals(userResponseBean.getGame(), oracleList.get(count).getGame());
            Assert.assertEquals(userResponseBean.getUsername(), oracleList.get(count).getUsername());

            count++;
        }
    }

    @Test
    public void testToLLResponseBean() {
        User testUser = new User();
        testUser.setToken("TestToken");

        UserLoginLogoutResponseBean oracleBean = new UserLoginLogoutResponseBean();
        oracleBean.setToken("TestToken");

        UserLoginLogoutResponseBean result = userMapperService.toLLResponseBean(testUser);

        Assert.assertEquals(result.getToken(), oracleBean.getToken());
    }
}

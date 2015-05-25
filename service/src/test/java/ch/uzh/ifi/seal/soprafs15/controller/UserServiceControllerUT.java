package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
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

/**
 * Created by Hakuna on 08.05.2015.
 */
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceControllerUT {

    @Mock
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserServiceController userServiceController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListUsers() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(userRepository);

        Assert.assertEquals(0, userServiceController.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(29, "Heinrich");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);

        Assert.assertEquals(1, userServiceController.listUsers().size());

        List<UserResponseBean> result = userServiceController.listUsers();

        Assert.assertEquals(userResponse.getId(), result.get(userResponse.getId().intValue()-1).getId());
        Assert.assertEquals(userRequest.getAge(), result.get(userResponse.getId().intValue()-1).getAge());
        Assert.assertEquals(userRequest.getUsername(), result.get(userResponse.getId().intValue()-1).getUsername());

        UserRequestBean secondUserRequest = TestUtils.toUserRequestBean(43, "Friedrich");
        UserResponseBean secondUserResponse = userServiceController.addUser(secondUserRequest);

        Assert.assertEquals(2, userServiceController.listUsers().size());

        result = userServiceController.listUsers();

        Assert.assertEquals(userResponse.getId(), result.get(userResponse.getId().intValue()-1).getId());
        Assert.assertEquals(userRequest.getAge(), result.get(userResponse.getId().intValue()-1).getAge());
        Assert.assertEquals(userRequest.getUsername(), result.get(userResponse.getId().intValue()-1).getUsername());

        Assert.assertEquals(secondUserResponse.getId(), result.get(secondUserResponse.getId().intValue()-1).getId());
        Assert.assertEquals(secondUserRequest.getAge(), result.get(secondUserResponse.getId().intValue()-1).getAge());
        Assert.assertEquals(secondUserRequest.getUsername(), result.get(secondUserResponse.getId().intValue()-1).getUsername());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddUser() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(userRepository);

        Assert.assertEquals(0, userServiceController.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(87, "Berta");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);

        Assert.assertEquals(1, userServiceController.listUsers().size());

        Assert.assertEquals(1, (long) userResponse.getId());
        Assert.assertEquals(userRequest.getAge(), userResponse.getAge());
        Assert.assertEquals(userRequest.getUsername(), userResponse.getUsername());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetUser() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(userRepository);

        Assert.assertEquals(0, userServiceController.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(25, "Ursula");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);

        Assert.assertEquals(1, userServiceController.listUsers().size());

        UserResponseBean result = userServiceController.getUser(userResponse.getId());

        Assert.assertEquals(userResponse.getId(), result.getId());
        Assert.assertEquals(userRequest.getAge(), result.getAge());
        Assert.assertEquals(userRequest.getUsername(), result.getUsername());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLogin() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(userRepository);

        Assert.assertEquals(0, userServiceController.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(25, "Ursula");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);

        Assert.assertEquals(1, userServiceController.listUsers().size());

        String oracleString = "067e6162-3b6f-4ae2-a171-2470b63dff00";

        UserLoginLogoutResponseBean result = userServiceController.login(userResponse.getId());

        Assert.assertEquals(oracleString.length(), result.getToken().length());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteUser() throws Exception {

        Assert.assertNotNull(userServiceController);
        Assert.assertNotNull(userRepository);

        Assert.assertEquals(0, userServiceController.listUsers().size());

        UserRequestBean userRequest = TestUtils.toUserRequestBean(25, "Ursula");
        UserResponseBean userResponse = userServiceController.addUser(userRequest);
        UserLoginLogoutResponseBean loginResponse = userServiceController.login(userResponse.getId());

        Assert.assertEquals(1, userServiceController.listUsers().size());

        UserLoginLogoutRequestBean deleteUserRequest = TestUtils.toUserLLRequestBean(loginResponse.getToken());
        userServiceController.deleteUser(userResponse.getId(), deleteUserRequest);

        Assert.assertEquals(0, userServiceController.listUsers().size());
    }
}

package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameCreateResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserExistsException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.GameMapperService;
import ch.uzh.ifi.seal.soprafs15.service.mapper.UserMapperService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


//Load Spring context
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserServiceUT {

    @Autowired
    private UserService testUserService;

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testLoginFail() throws Exception {

        assertNotNull(testUserService);

        assertEquals(0, testUserService.listUsers().size());

        testUserService.login((long) 1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListUsers() throws Exception {

        assertEquals(0, testUserService.listUsers().size());

        //oracle values
        List<UserResponseBean> oracleList = new ArrayList<>();
        oracleList.add(TestUtils.toUserResponseBean(12, "jakob"));

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testUserService);
        testUserService.addUser(TestUtils.toUserRequestBean(12, "jakob"));
        List<UserResponseBean> result = testUserService.listUsers();

        assertEquals(oracleList.get(0).getAge(), result.get(0).getAge());
        assertEquals(oracleList.get(0).getUsername(), result.get(0).getUsername());
        assertEquals(oracleList.size(), testUserService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddUser() throws Exception {

        // assertions at init
        assertNotNull(testUserService);
        assertEquals(0, testUserService.listUsers().size());
        //when(mockUserRepo.save(any(User.class))).thenReturn(Long.valueOf(1));

        // set up test object
        UserRequestBean requestBean = TestUtils.toUserRequestBean(15, "peter");

        // set up oracle values
        UserResponseBean oracleResponse = TestUtils.toUserResponseBean(15, "peter");

        UserResponseBean responseBean = testUserService.addUser(requestBean);

        // assertions after test
        assertEquals(oracleResponse.getAge(), responseBean.getAge());
        assertEquals(oracleResponse.getUsername(), responseBean.getUsername());
        assertEquals(1, testUserService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetUser() throws Exception {

        assertEquals(0, testUserService.listUsers().size());

        //oracle values
        UserResponseBean oracleResponse = TestUtils.toUserResponseBean(21, "hans");

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testUserService);
        UserResponseBean response = testUserService.addUser(TestUtils.toUserRequestBean(21, "hans"));
        UserResponseBean result = testUserService.getUser(response.getId());

        assertEquals(oracleResponse.getAge(), result.getAge());
        assertEquals(oracleResponse.getUsername(), result.getUsername());
        assertEquals(1, testUserService.listUsers().size());
    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testGetUserUserNotFoundFail() throws Exception {

        assertEquals(0, testUserService.listUsers().size());

        //oracle values
        UserResponseBean oracleResponse = TestUtils.toUserResponseBean(21, "hans");

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testUserService);
        UserResponseBean response = testUserService.addUser(TestUtils.toUserRequestBean(21, "hans"));
        UserResponseBean result = testUserService.getUser(response.getId() + 1);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testDeleteUser() throws Exception {

        assertEquals(0, testUserService.listUsers().size());

        // Create new User
        UserRequestBean userRequest = TestUtils.toUserRequestBean(92, "Troll");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login User
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());
        UserLoginLogoutRequestBean deleteRequest = TestUtils.toUserLLRequestBean(loginResponse.getToken());

        assertEquals(1, testUserService.listUsers().size());

        // Delete User
        testUserService.deleteUser(userResponse.getId(), deleteRequest);

        assertEquals(0, testUserService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLogin() throws Exception {

        assertEquals(0, testUserService.listUsers().size());

        //oracle values
        int oracleLength = "111e6162-3b6f-4ae2-a171-2470b63dff00".length();

        //create new User and add it
        UserRequestBean request = TestUtils.toUserRequestBean(55, "paul");
        UserResponseBean response = testUserService.addUser(request);

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testUserService);
        UserLoginLogoutResponseBean result = testUserService.login(response.getId());

        //Assertions
        assertEquals(oracleLength, result.getToken().length());
        assertEquals(1, testUserService.listUsers().size());
        //assertEquals(UserStatus.ONLINE, user.getStatus());
    }

//    @Test
//    @SuppressWarnings("unchecked")
//    public void testLogout() throws Exception {
//        // Create user
//        UserRequestBean userRequest = TestUtils.toUserRequestBean(54, "Rolf");
//        UserResponseBean userResponse = testService.addUser(userRequest);
//
//        // Assert that user status is OFFLINE (before Login)
//        User userBeforeLogin = mockUserRepo.findByUsername(userRequest.getUsername());
//        assertEquals(UserStatus.OFFLINE, userBeforeLogin.getStatus());
//
//        // Login user
//        UserLoginLogoutResponseBean loginResponse = testService.login(userResponse.getId());
//
//        // Assert that user status is ONLINE (after login)
//        User userBeforeLogout = mockUserRepo.findByToken(loginResponse.getToken());
//        assertEquals(userResponse.getId(), userBeforeLogout.getId());
//        assertEquals(UserStatus.ONLINE, userBeforeLogout.getStatus());
//
//        // Logout user
//        UserLoginLogoutRequestBean logoutRequest = TestUtils.toUserLLRequestBean(loginResponse.getToken());
//        testService.logout(userResponse.getId(), logoutRequest);
//
//        // Assert that user status is OFFLINE again (after Logout)
//        User userAfterLogout = mockUserRepo.findByToken(loginResponse.getToken());
//        assertEquals(UserStatus.OFFLINE, userAfterLogout.getStatus());
//    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testLogoutUserNotFoundFail() throws Exception {
        // Create user
        UserRequestBean userRequest = TestUtils.toUserRequestBean(54, "Rudolf");
        UserResponseBean userResponse = testUserService.addUser(userRequest);

        // Login user
        UserLoginLogoutResponseBean loginResponse = testUserService.login(userResponse.getId());

        // Logout user
        UserLoginLogoutRequestBean logoutRequest = TestUtils.toUserLLRequestBean(loginResponse.getToken());
        testUserService.logout(userResponse.getId() + 1, logoutRequest);
    }

    @Test(expected = UserExistsException.class)
    @SuppressWarnings("unchecked")
    public void testAddUserUserExistsFail() throws Exception {
        assertNotNull(testUserService);
        assertEquals(0, testUserService.listUsers().size());

        // create first user
        UserRequestBean firstUserRequest = TestUtils.toUserRequestBean(29, "Hansueli");
        testUserService.addUser(firstUserRequest);

        assertEquals(1, testUserService.listUsers().size());

        // create second user (with same username)
        UserRequestBean invalidRequest = TestUtils.toUserRequestBean(85, firstUserRequest.getUsername());
        testUserService.addUser(invalidRequest);

        assertEquals(1, testUserService.listUsers().size());
    }
}
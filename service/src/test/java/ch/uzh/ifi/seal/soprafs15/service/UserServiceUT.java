package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
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

    //Create MockRepo
    @Mock
    private UserRepository mockUserRepo;

    @InjectMocks
    @Autowired
    private UserService testService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = UserNotFoundException.class)
    @SuppressWarnings("unchecked")
    public void testLoginUserFail() throws Exception {

        assertNotNull(testService);

        assertEquals(0, testService.listUsers().size());

        testService.login((long) 1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testListUsers() throws Exception {

        assertEquals(0, testService.listUsers().size());

        //oracle values
        List<UserResponseBean> oracleList = new ArrayList<>();
        oracleList.add(TestUtils.toUserResponseBean(12, "jakob"));

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testService);
        testService.addUser(TestUtils.toUserRequestBean(12, "jakob"));
        List<UserResponseBean> result = testService.listUsers();

        assertEquals(oracleList.get(0).getAge(), result.get(0).getAge());
        assertEquals(oracleList.get(0).getUsername(), result.get(0).getUsername());
        assertEquals(oracleList.size(), testService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddUser() throws Exception {

        assertEquals(0, testService.listUsers().size());
        //when(mockUserRepo.save(any(User.class))).thenReturn(Long.valueOf(1));

        //create new UserRequestBean to test with
        UserRequestBean requestBean = TestUtils.toUserRequestBean(15, "peter");

        //oracle values
        UserResponseBean oracleResponse = TestUtils.toUserResponseBean(15, "peter");

        User testUser = new User();
        when(mockUserRepo.save(any(User.class))).thenReturn(testUser);

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testService);
        UserResponseBean responseBean = testService.addUser(requestBean);

        assertEquals(oracleResponse.getAge(), responseBean.getAge());
        assertEquals(oracleResponse.getUsername(), responseBean.getUsername());
        assertEquals(1, testService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetUser() throws Exception {

        assertEquals(0, testService.listUsers().size());

        //oracle values
        UserResponseBean oracleResponse = TestUtils.toUserResponseBean(21, "hans");

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testService);
        UserResponseBean response = testService.addUser(TestUtils.toUserRequestBean(21, "hans"));
        UserResponseBean result = testService.getUser(response.getId());

        assertEquals(oracleResponse.getAge(), result.getAge());
        assertEquals(oracleResponse.getUsername(), result.getUsername());
        assertEquals(1, testService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testUpdateUser() throws Exception {
        // not implemented yet
    }

    @Test
    //method not yet implemented
    public void testDeleteUser() throws Exception {

        //create new User and and add it
        UserRequestBean request = TestUtils.toUserRequestBean(67,"karl");
        UserResponseBean response = testService.addUser(request);

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testService);
        testService.deleteUser(response.getId(), request);

        //Assertions
        assertNull(mockUserRepo.findByUsername("karl"));


    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLogin() throws Exception {

        assertEquals(0, testService.listUsers().size());

        //oracle values
        int oracleLength = "111e6162-3b6f-4ae2-a171-2470b63dff00".length();

        //create new User and add it
        UserRequestBean request = TestUtils.toUserRequestBean(55,"paul");
        UserResponseBean response = testService.addUser(request);

        //Assert testService has been initialized and call method to be tested
        assertNotNull(testService);
        UserLoginLogoutResponseBean result = testService.login(response.getId());

        //Assertions
        assertEquals(oracleLength, result.getToken().length());
        assertEquals(1, testService.listUsers().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLogout() throws Exception {
        //void method, difficult to test, omitted for time being
    }
}
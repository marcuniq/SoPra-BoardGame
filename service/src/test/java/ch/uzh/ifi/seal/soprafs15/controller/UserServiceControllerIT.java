package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@IntegrationTest({"server.port=0"})
public class UserServiceControllerIT {

    @Value("${local.server.port}")
    private int port;

    private URL base;
    private RestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }
	
	@Test
	@SuppressWarnings("unchecked")
	public void testCreateUserSuccess() throws Exception {

        // Make sure that no user is saved at Initialization
		List<UserResponseBean> usersBefore = template.getForObject(base + "/users", List.class);
		Assert.assertEquals(0, usersBefore.size());

        // Create new User
		UserRequestBean request = TestUtils.toUserRequestBean(43, "TestUser1");
        ResponseEntity<UserResponseBean> response = TestUtils.createUser(request, template, base);

        // Oracle values
        Long oracleUserId = (long) 1;

		Assert.assertEquals(request.getAge(), response.getBody().getAge());
        Assert.assertEquals(request.getUsername(), response.getBody().getUsername());
        Assert.assertEquals((long) oracleUserId, (long) response.getBody().getId());

        // Make sure that exactly one user is saved after Test
	    List<UserResponseBean> usersAfter = template.getForObject(base + "/users", List.class);
		Assert.assertEquals(1, usersAfter.size());
	}

//    @Test
//    @SuppressWarnings("unchecked")
//    public void testCreateUserFail() throws Exception {
//
//        List<UserResponseBean> usersBefore = template.getForObject(base + "/users", List.class);
//        Assert.assertEquals(0, usersBefore.size());
//
//        UserRequestBean request = TestUtils.toUserRequestBean(43, "TestUser1");
//
//        ResponseEntity<UserResponseBean> response = TestUtils.createUser(request, template, base);
//
//        Assert.assertEquals(request.getAge(), response.getBody().getAge());
//        Assert.assertEquals(request.getUsername(), response.getBody().getUsername());
//        Assert.assertEquals(1, (long) response.getBody().getId());
//
//        List<UserResponseBean> usersAfter = template.getForObject(base + "/users", List.class);
//        Assert.assertEquals(1, usersAfter.size());
//
//        // Create second user with same name as first user --> Fail
//
//        UserRequestBean secondUserRequest = TestUtils.toUserRequestBean(23, "TestUser1");
//
//        ResponseEntity<UserResponseBean> secondUserResponse = TestUtils.createUser(secondUserRequest, template, base);
//
//        // TODO UserAlreadyExistsException instead of UserResponseBean
//
//        List<UserResponseBean> usersAfterSecond = template.getForObject(base + "/users", List.class);
//        Assert.assertEquals(1, usersAfterSecond.size());
//    }

    @Test
    @SuppressWarnings("unchecked")
    public void testLoginUserSuccess() throws Exception {

        // Make sure that no user is saved at Initialization
        List<UserResponseBean> usersBefore = template.getForObject(base + "/users", List.class);
        Assert.assertEquals(0, usersBefore.size());

        // Create new User
        UserRequestBean request = TestUtils.toUserRequestBean(43, "TestUser1");
        ResponseEntity<UserResponseBean> response = TestUtils.createUser(request, template, base);

        // Login this User
        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(response.getBody().getId(), template, base);

        // Oracle values: TokenLength
        Long oracleTokenLength = (long) "067e6162-3b6f-4ae2-a171-2470b63dff00".length();

        Assert.assertEquals((long) oracleTokenLength, loginResponse.getBody().getToken().length());
    }
}

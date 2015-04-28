package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
import ch.uzh.ifi.seal.soprafs15.TestUtils;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
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
	public void testCreateUserSuccess() {

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);

		List<UserResponseBean> usersBefore = template.getForObject(base + "/users", List.class);
		Assert.assertEquals(0, usersBefore.size());

		UserRequestBean request = TestUtils.toUserRequestBean(43, "TestUser1");

        ResponseEntity<UserResponseBean> response = TestUtils.createUser(request, template, base);

		Assert.assertEquals(request.getAge(), response.getBody().getAge());
        Assert.assertEquals(request.getUsername(), response.getBody().getUsername());
        Assert.assertEquals(1, (long) response.getBody().getId());
		
	    List<UserResponseBean> usersAfter = template.getForObject(base + "/users", List.class);
		Assert.assertEquals(1, usersAfter.size());

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);
	}

    @Test
    @SuppressWarnings("unchecked")
    public void testLoginUserSuccess() {

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);

        // Set up

        UserRequestBean request = TestUtils.toUserRequestBean(43, "TestUser1");

        ResponseEntity<UserResponseBean> response = TestUtils.createUser(request, template, base);

        // Test Login

        ResponseEntity<UserLoginLogoutResponseBean> loginResponse = TestUtils.loginUser(1, template, base);

        Long oracleTokenLength = (long) "067e6162-3b6f-4ae2-a171-2470b63dff00".length();

        Assert.assertEquals((long) oracleTokenLength, loginResponse.getBody().getToken().length());

        // Server Reset: Clean Repos
        //TestUtils.clearRepositories(template, base);
    }



}

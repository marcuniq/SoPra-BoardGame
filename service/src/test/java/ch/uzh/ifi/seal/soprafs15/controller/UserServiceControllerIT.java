package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.Application;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
		List<UserResponseBean> usersBefore = template.getForObject(base + "/users", List.class);
		Assert.assertEquals(0, usersBefore.size());
		
		UserRequestBean request = new UserRequestBean();
		request.setAge(43);
		request.setUsername("mm");

		HttpEntity<UserRequestBean> httpEntity = new HttpEntity<UserRequestBean>(request);
		
		ResponseEntity<UserResponseBean> response = template.exchange(base + "/users", HttpMethod.POST, httpEntity, UserResponseBean.class);
		Assert.assertEquals(request.getAge(), response.getBody().getAge());
        Assert.assertEquals(request.getUsername(), response.getBody().getUsername());
		
	    List<UserResponseBean> usersAfter = template.getForObject(base + "/users", List.class);
		Assert.assertEquals(1, usersAfter.size());
	}

}

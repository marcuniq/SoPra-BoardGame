package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.service.UserService;
import ch.uzh.ifi.seal.soprafs15.service.mapper.UserMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserServiceController extends GenericService {

	Logger logger = LoggerFactory.getLogger(UserServiceController.class);
	
	static final String CONTEXT = "/users";

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserMapperService userMapperService;


    /*
     *	Context: /users
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT)
	@ResponseStatus(HttpStatus.OK)
	public List<UserResponseBean> listUsers() {
		logger.debug("listUsers");

        try {
            List<User> users = userService.listUsers();

            List<UserResponseBean> result = userMapperService.toUserResponseBean(users);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /users
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public UserResponseBean addUser(@RequestBody UserRequestBean userRequestBean) {
		logger.debug("addUser: " + userRequestBean);

        try {
            User user = userMapperService.toUser(userRequestBean);

            user = userService.addUser(user);

            UserResponseBean result = userMapperService.toUserResponseBean(user);

            return result;
        } catch(Exception e){
            return null;
        }
	}


    /*
     *	Context: /users/{userId}
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{userId}")
	@ResponseStatus(HttpStatus.OK)
	public UserResponseBean getUser(@PathVariable Long userId) {
		logger.debug("getUser: " + userId);

        try {
            User user = userService.getUser(userId);

            UserResponseBean result = userMapperService.toUserResponseBean(user);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /users/{userId}/login
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{userId}/login")
	@ResponseStatus(HttpStatus.OK)
	public UserLoginLogoutResponseBean login(@PathVariable Long userId) {
		logger.debug("login: " + userId);

        try {
            User user = userService.login(userId);

            UserLoginLogoutResponseBean result = userMapperService.toLLResponseBean(user);

            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /users/{userId}/logout
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{userId}/logout")
	@ResponseStatus(HttpStatus.OK)
	public void logout(@PathVariable Long userId, @RequestBody UserLoginLogoutRequestBean userLoginLogoutRequestBean) {
		logger.debug("getUser: " + userId);

        try{
            User user = userMapperService.toUser(userLoginLogoutRequestBean);

            userService.logout(userId, user);

        } catch (Exception e){

        }
	}
}

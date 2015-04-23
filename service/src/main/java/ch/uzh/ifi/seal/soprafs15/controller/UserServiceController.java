package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
/**
 * @author Marco
 */
@RestController
public class UserServiceController extends GenericService {

	private final Logger logger = LoggerFactory.getLogger(UserServiceController.class);

    @Autowired
    protected UserService userService;

    static final String CONTEXT = "/users";


    /*
     *	Context: /users
     *  Description:
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT)
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public List<UserResponseBean> listUsers() {
		logger.debug("listUsers");

        try {
            List<UserResponseBean> result = userService.listUsers();
            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /users
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public UserResponseBean addUser(@RequestBody @Valid UserRequestBean userRequestBean) {
		logger.debug("addUser: " + userRequestBean);

        try {
            UserResponseBean result = userService.addUser(userRequestBean);
            return result;
        } catch(Exception e){
            return null;
        }
	}


    /*
     *	Context: /users/{userId}
     *  Description:
     */
	@RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/{userId}")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public UserResponseBean getUser(@PathVariable Long userId) {
		logger.debug("getUser: " + userId);

        try {
            UserResponseBean result = userService.getUser(userId);
            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /users/{userId}/login
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{userId}/login")
	@ResponseStatus(HttpStatus.OK)
    @ResponseBody
	public UserLoginLogoutResponseBean login(@PathVariable Long userId) {
		logger.debug("login: " + userId);

        try {
            UserLoginLogoutResponseBean result = userService.login(userId);
            return result;
        } catch (Exception e){
            return null;
        }
	}


    /*
     *	Context: /users/{userId}/logout
     *  Description:
     */
	@RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/{userId}/logout")
	@ResponseStatus(HttpStatus.OK)
	public void logout(@PathVariable Long userId, @RequestBody @Valid UserLoginLogoutRequestBean userLoginLogoutRequestBean) {
		logger.debug("getUser: " + userId);

        try{
            userService.logout(userId, userLoginLogoutRequestBean);

        } catch (Exception e){

        }
	}
}

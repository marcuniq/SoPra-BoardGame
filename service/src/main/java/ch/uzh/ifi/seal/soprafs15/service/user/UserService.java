package ch.uzh.ifi.seal.soprafs15.service.user;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;

import java.util.List;

/**
 * @author Marco
 */
public abstract class UserService extends GenericService {

    public abstract List<UserResponseBean> listUsers();
    public abstract UserResponseBean addUser(UserRequestBean bean);
    public abstract UserResponseBean getUser(Long userId);
    //public abstract UserResponseBean updateUser(Long userId, UserLoginLogoutRequestBean bean);
    public abstract void deleteUser(Long userId, UserLoginLogoutRequestBean bean);
    public abstract UserLoginLogoutResponseBean login(Long userId);
    public abstract void logout(Long userId, UserLoginLogoutRequestBean bean);
}

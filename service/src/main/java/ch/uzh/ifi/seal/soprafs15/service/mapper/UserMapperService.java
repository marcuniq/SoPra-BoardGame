package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserLoginLogoutResponseBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserResponseBean;
import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */
public abstract class UserMapperService extends GenericService {

    public abstract User toUser(UserRequestBean bean);
    public abstract UserResponseBean toUserResponseBean(User user);
    public abstract  List<UserResponseBean> toUserResponseBean(List<User> users);

    public abstract User toUser(UserLoginLogoutRequestBean bean);
    public abstract UserLoginLogoutResponseBean toLLResponseBean(User user);
}

package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Marco
 *
 *  Responsible for mapping between Beans and Domain Models of the User
 */
@Service("userMapperService")
public class UserMapperServiceImpl extends UserMapperService {

    Logger logger = LoggerFactory.getLogger(UserMapperServiceImpl.class);

    protected UserRepository userRepository;

    @Autowired
    public UserMapperServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User toUser(UserRequestBean bean) {
        User user = new User();
        user.setAge(bean.getAge());
        user.setUsername(bean.getUsername());
        user.setStatus(UserStatus.OFFLINE);
        user.setToken(UUID.randomUUID().toString());
        return user;
    }

    @Override
    public UserResponseBean toUserResponseBean(User user) {
        UserResponseBean userResponseBean = new UserResponseBean();
        if(user != null) {
            userResponseBean.setId(user.getId());
            userResponseBean.setAge(user.getAge());
            userResponseBean.setUsername(user.getUsername());
            if(user.getGame() != null)
                userResponseBean.setGame(user.getGame().getName());
        }

        return userResponseBean;
    }

    @Override
    public List<UserResponseBean> toUserResponseBean(List<User> users) {
        List<UserResponseBean> result = new ArrayList<>();

        UserResponseBean tmpUserResponseBean;

        for(User user : users) {
            tmpUserResponseBean = new UserResponseBean();

            tmpUserResponseBean.setId(user.getId());
            tmpUserResponseBean.setAge(user.getAge());
            tmpUserResponseBean.setUsername(user.getUsername());
            if(user.getGame() != null)
                tmpUserResponseBean.setGame(user.getGame().getName());

            result.add(tmpUserResponseBean);
        }
        return result;
    }

    @Override
    public User toUser(UserLoginLogoutRequestBean bean) {
        return userRepository.findByToken(bean.getToken());
    }

    @Override
    public UserLoginLogoutResponseBean toLLResponseBean(User user) {
        UserLoginLogoutResponseBean bean = new UserLoginLogoutResponseBean();
        bean.setToken(user.getToken());
        return bean;
    }
}

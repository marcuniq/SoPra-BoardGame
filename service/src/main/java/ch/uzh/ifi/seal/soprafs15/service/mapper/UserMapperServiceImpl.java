package ch.uzh.ifi.seal.soprafs15.service.mapper;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.*;
import ch.uzh.ifi.seal.soprafs15.model.Game;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */
@Component("userMapperService")
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

            List<String>games = new ArrayList<>();
            for(Game game : user.getGames()) {
                games.add(game.getName());
            }
            userResponseBean.setGames(games);
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

            List<String>games = new ArrayList<>();
            for(Game game : user.getGames()) {
                games.add(game.getName());
            }
            tmpUserResponseBean.setGames(games);

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
        return null;
    }
}

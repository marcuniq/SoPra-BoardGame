package ch.uzh.ifi.seal.soprafs15.service.user;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.*;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserExistsException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UserNotFoundException;
import ch.uzh.ifi.seal.soprafs15.service.mapper.UserMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author Marco
 */
@Transactional
@Service("userService")
public class UserServiceImpl extends UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    protected UserRepository userRepository;
    protected UserMapperService userMapperService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapperService userMapperService){
        this.userRepository = userRepository;
        this.userMapperService = userMapperService;
    }

    @Override
    public List<UserResponseBean> listUsers() {
        List<User> users = (List<User>) userRepository.findAll();

        return userMapperService.toUserResponseBean(users);
    }

    @Override
    public UserResponseBean addUser(UserRequestBean bean) {

        if(userRepository.findByUsername(bean.getUsername()) != null) {
            throw new UserExistsException(userRepository.findByUsername(bean.getUsername()), UserServiceImpl.class);
        }

        User user = userMapperService.toUser(bean);
        user =  userRepository.save(user);
        return userMapperService.toUserResponseBean(user);
    }

    @Override
    public UserResponseBean getUser(Long userId) {
        User user = userRepository.findOne(userId);

        if(user == null)
            throw new UserNotFoundException(userId, UserServiceImpl.class);

        return userMapperService.toUserResponseBean(user);
    }

    @Override
    public void deleteUser(Long userId, UserLoginLogoutRequestBean bean) {
        User userFromBean = userMapperService.toUser(bean);
        User userFromId = userRepository.findOne(userId);

        if(userFromId == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        if(userFromBean == null) {
            throw new UserNotFoundException(bean.getToken(), UserServiceImpl.class);
        }

        if(userFromId.getId() == userFromBean.getId()) {
            userRepository.delete(userId);
        }
    }

    @Override
    public UserLoginLogoutResponseBean login(Long userId) {
        User user = userRepository.findOne(userId);

        if(user == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        user.setToken(UUID.randomUUID().toString());
        user.setStatus(UserStatus.ONLINE);
        user = userRepository.save(user);

        return userMapperService.toLLResponseBean(user);
    }

    @Override
    public void logout(Long userId, UserLoginLogoutRequestBean bean) {
        User userFromBean = userMapperService.toUser(bean);
        User userFromId = userRepository.findOne(userId);

        if(userFromId == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        if(userFromBean == null) {
            throw new UserNotFoundException(bean.getToken(), UserServiceImpl.class);
        }

        if(userFromId.getId() == userFromBean.getId()) {
            userFromId.setStatus(UserStatus.OFFLINE);
            userRepository.save(userFromId);
        }
    }
}

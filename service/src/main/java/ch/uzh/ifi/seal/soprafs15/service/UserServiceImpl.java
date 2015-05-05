package ch.uzh.ifi.seal.soprafs15.service;

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
            throw new UserExistsException(userMapperService.toUser(bean), UserServiceImpl.class);
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
    public UserResponseBean updateUser(Long userId, UserLoginLogoutRequestBean bean) {
        User user = userMapperService.toUser(bean);
        User user_1 = userRepository.findOne(userId);

        if(user_1 == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        if(user_1 != null && user_1.getToken().equals(user.getToken())) {

            user_1.setId(user.getId());
            user_1.setAge(user.getAge());
            user_1.setUsername(user.getUsername());
            user_1.setGameState(user.getGameState());
            user_1.setMoves(user.getMoves());
            user_1.setToken(user.getToken());
            user_1.setStatus(user.getStatus());
            user_1.setRaceBettingCards(user.getRaceBettingCards());
            user_1.setMoney(user.getMoney());
            user_1.setLegBettingTiles(user.getLegBettingTiles());

            userRepository.save(user_1);

            return userMapperService.toUserResponseBean(user_1);
        }
        return null;
    }

    @Override
    public void deleteUser(Long userId, UserLoginLogoutRequestBean bean) {
        User userFromBean = userMapperService.toUser(bean);
        User userFromId = userRepository.findOne(userId);

        if(userFromId == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        if(userFromId.getToken().equals(userFromBean.getToken())) {
            userRepository.delete(userId);
        }
    }

    @Override
    public UserLoginLogoutResponseBean login(Long userId) {
        User user = userRepository.findOne(userId);

        if(user == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        if(user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            user = userRepository.save(user);

            return userMapperService.toLLResponseBean(user);
        }
        return null;
    }

    @Override
    public void logout(Long userId, UserLoginLogoutRequestBean bean) {
        User user = userMapperService.toUser(bean);
        User user_1 = userRepository.findOne(userId);

        if(user_1 == null) {
            throw new UserNotFoundException(userId, UserServiceImpl.class);
        }

        if(user_1 != null && user_1.getToken().equals(user.getToken())) {
            user_1.setStatus(UserStatus.OFFLINE);
            userRepository.save(user_1);
        }
    }
}

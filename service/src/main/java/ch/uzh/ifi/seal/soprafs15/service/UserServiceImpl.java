package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserStatus;
import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author Marco
 */

@Service("userService")
public class UserServiceImpl extends UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    protected UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public List<User> listUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findOne(userId);
    }

    @Override
    public User updateUser(Long userId, User user) {
        User user_1 = userRepository.findOne(userId);

        if(user_1 != null && user_1.getToken().equals(user.getToken())) {
            user_1.setRaceBettingCards(user.getRaceBettingCards());
            user_1.setMoney(user.getMoney());
            user_1.setLegBettingTiles(user.getLegBettingTiles());
            user_1.setId(user.getId());
            user_1.setAge(user.getAge());
            user_1.setUsername(user.getUsername());
            user_1.setGame(user.getGame());
            user_1.setMoves(user.getMoves());
            user_1.setToken(user.getToken());
            user_1.setStatus(user.getStatus());

            return user_1;
        }
        return null;
    }

    @Override
    public void deleteUser(Long userId, User user) {
        User user_1 = userRepository.findOne(userId);

        if(user_1 != null && user_1.getToken().equals(user.getToken())) {
            userRepository.delete(userId);
        }
    }

    @Override
    public User login(Long userId) {
        User user = userRepository.findOne(userId);

        if(user != null) {
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            user = userRepository.save(user);

            return user;
        }
        return null;
    }

    @Override
    public void logout(Long userId, User user) {
        User user_1 = userRepository.findOne(userId);

        if(user_1 != null && user_1.getToken().equals(user.getToken())) {
            user_1.setStatus(UserStatus.OFFLINE);
            userRepository.save(user_1);
        }
    }
}

package ch.uzh.ifi.seal.soprafs15.service;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.User;

import java.util.List;

/**
 * @author Marco
 */
public abstract class UserService extends GenericService {

    public abstract List<User> listUsers();
    public abstract User addUser(User user);
    public abstract User getUser(Long userId);
    public abstract User updateUser(Long userId, User user);
    public abstract void deleteUser(Long userId, User user);
    public abstract User login(Long userId);
    public abstract void logout(Long userId, User user);
}

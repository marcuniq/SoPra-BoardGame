package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.User;

/**
 * @author Marco
 */
public class UserNotFoundException extends Exception {
    public UserNotFoundException(User player) {
    }
}

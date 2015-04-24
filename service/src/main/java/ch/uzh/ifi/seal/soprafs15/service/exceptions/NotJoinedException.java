package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * In case when a player tries to do a move, but didn't join the game. HttpStatus is 403 FORBIDDEN.
 */
public class NotJoinedException extends UncheckedException {

    public NotJoinedException(String message, Class invoker) {
        super(message, invoker, HttpStatus.FORBIDDEN);
    }

    public NotJoinedException(Integer id, Class invoker) {
        this("Player with id " + id + " has not joined the game.", invoker);
    }
}

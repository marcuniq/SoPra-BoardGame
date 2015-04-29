package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Marco
 */
public class GameAlreadyStartedException extends UncheckedException {
    public GameAlreadyStartedException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public GameAlreadyStartedException(Long gameId, Class invoker) {
        this("The game with id " + gameId + "has already started.", invoker, HttpStatus.PRECONDITION_FAILED);
    }
}

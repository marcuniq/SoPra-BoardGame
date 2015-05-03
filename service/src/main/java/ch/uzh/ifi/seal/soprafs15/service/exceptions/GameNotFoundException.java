package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * In case that a game couldn't be found.
 *
 */
public class GameNotFoundException extends UncheckedException {

    public GameNotFoundException(String message, Class invoker) {
        super(message, invoker, HttpStatus.NOT_FOUND);
    }

    public GameNotFoundException(Long gameId, Class invoker) {
        this("Game with id " + gameId + " could not be found!", invoker);
    }
}

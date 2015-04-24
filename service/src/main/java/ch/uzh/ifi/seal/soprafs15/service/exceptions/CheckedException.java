package ch.uzh.ifi.seal.soprafs15.service.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * Base class for all checked exception classes. This applies always when we have exceptions that may happen and are
 * treated accordingly (e.g. Game full) -> to be used for all cases that can happen as an ordinary part of the game
 *
 * @see ch.uzh.ifi.seal.soprafs15.service.exceptions.UncheckedException
 */
public abstract class CheckedException extends AbstractException {

    public CheckedException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public CheckedException(String message, Class invoker) {
        this(message, invoker, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

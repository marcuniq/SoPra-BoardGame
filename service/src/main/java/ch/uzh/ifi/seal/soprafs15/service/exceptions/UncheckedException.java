package ch.uzh.ifi.seal.soprafs15.service.exceptions;


import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * Base class for all unchecked exception classes. This applies to all cases when something internally is not working properly.
 *
 * @see ch.uzh.ifi.seal.soprafs15.service.exceptions.CheckedException
 *
 */
public abstract class UncheckedException extends AbstractException {

    public UncheckedException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public UncheckedException(String message, Class invoker) {
        this(message, invoker, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

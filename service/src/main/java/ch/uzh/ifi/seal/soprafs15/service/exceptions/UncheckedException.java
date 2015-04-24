package ch.uzh.ifi.seal.soprafs15.service.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * Base class for all unchecked exception classes. This applies to all cases when something internally is not working properly.
 *
 * @see ch.uzh.ifi.seal.soprafs15.service.exceptions.CheckedException
 *
 */
public class UncheckedException extends RuntimeException {

    private final HttpStatus status;

    public UncheckedException(String message, Class invoker, HttpStatus status) {
        super(message);
        this.status = status;
        Logger logger = LoggerFactory.getLogger(invoker);
        logger.debug(message);
    }

    public UncheckedException(String message, Class invoker) {
        this(message, invoker, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

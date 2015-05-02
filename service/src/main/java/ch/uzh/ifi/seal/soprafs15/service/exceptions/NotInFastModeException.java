package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Marco
 */
public class NotInFastModeException extends UncheckedException{

    public NotInFastModeException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public NotInFastModeException(Class invoker) {
        this("The game is not in fast mode", invoker, HttpStatus.EXPECTATION_FAILED);
    }
}

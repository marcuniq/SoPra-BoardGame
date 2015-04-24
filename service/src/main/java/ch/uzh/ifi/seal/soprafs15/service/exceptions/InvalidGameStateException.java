package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 */
public class InvalidGameStateException extends UncheckedException {

    public InvalidGameStateException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public InvalidGameStateException(String message, Class invoker) {
        super(message, invoker);
    }
}

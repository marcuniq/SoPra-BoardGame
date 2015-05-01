package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Marco
 */
public class MoveUndoException extends UncheckedException {

    public MoveUndoException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }

    public MoveUndoException(String message, Class invoker) {
        this(message, invoker, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

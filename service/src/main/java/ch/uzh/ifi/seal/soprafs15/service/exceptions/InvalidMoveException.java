package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.model.move.Move;
import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * This class is a wrapper for all Exceptions that can possibly occur while Executing a move.
 */
public class InvalidMoveException extends CheckedException {

    public InvalidMoveException(Move move, Class invoker) {
        this("Invalid Move. Reason(s): " + move.getInvalidReasons().toString(), invoker, HttpStatus.BAD_REQUEST);
    }

    public InvalidMoveException(String message, Class invoker, HttpStatus status) {
        super(message, invoker, status);
    }
}

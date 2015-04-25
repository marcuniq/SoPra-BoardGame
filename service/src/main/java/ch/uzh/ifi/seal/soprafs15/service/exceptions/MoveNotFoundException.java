package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 25.04.2015.
 */
public class MoveNotFoundException extends UncheckedException {

    public MoveNotFoundException(String message, Class invoker) {
        super(message, invoker, HttpStatus.NOT_FOUND);
    }

    public MoveNotFoundException(Long moveId, Class invoker) {
        this("Move with id " + moveId + " could not be found!", invoker);
    }
}

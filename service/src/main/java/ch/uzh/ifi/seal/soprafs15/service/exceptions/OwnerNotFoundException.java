package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 25.04.2015.
 */
public class OwnerNotFoundException extends UncheckedException {

    public OwnerNotFoundException(String message, Class invoker) {
        super(message, invoker, HttpStatus.NOT_FOUND);
    }

    public OwnerNotFoundException(Long ownerId, Class invoker) {
        this("Owner with id " + ownerId + " could not be found!", invoker);
    }
}

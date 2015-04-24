package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * If a player tries to add more than one game. This is unchecked, since it shouldn't be allowed from the GUI to do that.
 * HttpStatus is 403 FORBIDDEN
 */
public class MultipleGameException extends UncheckedException {

    public MultipleGameException(String message, Class invoker) {
        super(message, invoker, HttpStatus.FORBIDDEN);
    }
}

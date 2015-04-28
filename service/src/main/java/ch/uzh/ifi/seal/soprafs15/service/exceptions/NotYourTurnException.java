package ch.uzh.ifi.seal.soprafs15.service.exceptions;

/**
 * Created by Hakuna on 13.04.2015.
 */
public class NotYourTurnException extends UncheckedException {

    public NotYourTurnException(String message, Class invoker) {
        super(message, invoker);
    }

    public NotYourTurnException(Class invoker) {
        this("It's not your turn!", invoker);
    }
}

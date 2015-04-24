package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Hakuna on 13.04.2015.
 *
 * This exception is thrown if a bean contains invalid parameters.
 */
public class InvalidBeanException extends UncheckedException {

    public InvalidBeanException(String message, Class invoker) {
        super(message, invoker, HttpStatus.BAD_REQUEST);
    }

    public InvalidBeanException(Object bean, Class invoker) {
        this("Invalid bean (" + bean.toString() + ") of type " + bean.getClass(), invoker);
    }
}

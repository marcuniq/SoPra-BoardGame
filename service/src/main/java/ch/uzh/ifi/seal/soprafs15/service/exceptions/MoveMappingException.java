package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author Marco
 */
public class MoveMappingException extends UncheckedException {

    public MoveMappingException(String message, Class invoker){
        super(message, invoker, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public MoveMappingException(Class invoker) {
        this("Mapping from bean to move failed", invoker);
    }
}

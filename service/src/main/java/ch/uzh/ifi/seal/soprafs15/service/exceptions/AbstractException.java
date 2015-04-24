package ch.uzh.ifi.seal.soprafs15.service.exceptions;

import ch.uzh.ifi.seal.soprafs15.controller.beans.ExceptionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @author Marco
 */
public abstract class AbstractException extends RuntimeException {

    protected String message;
    protected HttpStatus status;
    protected Class invoker;

    public AbstractException(String message, Class invoker, HttpStatus status){
        super(message);
        this.message = message;
        this.invoker = invoker;
        this.status = status;

        Logger logger = LoggerFactory.getLogger(invoker);
        logger.debug(message);
    }

    public HttpStatus getStatus(){
        return status;
    }

    public ExceptionBean toExceptionBean(){
        return new ExceptionBean(message, invoker, status.name());
    }
}

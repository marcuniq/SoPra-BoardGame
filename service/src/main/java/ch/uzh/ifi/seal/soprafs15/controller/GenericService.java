package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.controller.beans.ExceptionBean;
import ch.uzh.ifi.seal.soprafs15.controller.beans.JsonUriWrapper;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.AbstractException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.CheckedException;
import ch.uzh.ifi.seal.soprafs15.service.exceptions.UncheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class GenericService {
	
	Logger logger = LoggerFactory.getLogger(GenericService.class);
	
	protected JsonUriWrapper getJsonUrl(String uri) {
		JsonUriWrapper wrapper = new JsonUriWrapper();
		wrapper.setUri(uri);
		
		return wrapper;
	}
	
	@ExceptionHandler(TransactionSystemException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public void handleTransactionSystemException(Exception exception, HttpServletRequest request) {
		logger.error("", exception);
	}
	
/*	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(Exception exception, HttpServletRequest request) {
		logger.error("", exception);
	}*/


    /*
     *  Handles exceptions if request beans are not valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        logger.error("got invalid bean, request:  " + request.toString());

        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        logger.error("got invalid bean, errors: " + errors.toString());

        return "invalid bean";
    }

    /*
     *  Handles our exceptions
     */
    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    public ResponseEntity<ExceptionBean> handleAbstractException(AbstractException e, HttpServletRequest request) {
        logger.error("got exception");

        ResponseEntity<ExceptionBean> result = new ResponseEntity<ExceptionBean>(e.toExceptionBean(), e.getStatus());

        return result;
    }

}

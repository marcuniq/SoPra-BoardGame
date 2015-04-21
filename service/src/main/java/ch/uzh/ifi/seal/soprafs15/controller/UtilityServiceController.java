package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.service.UserService;
import ch.uzh.ifi.seal.soprafs15.service.UtilityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Marco
 *
 * Exposing utility functions
 */
@Controller
public class UtilityServiceController extends GenericService {

    private final Logger logger = LoggerFactory.getLogger(UtilityServiceController.class);

    @Autowired
    protected UtilityService utilityService;

    private static final String CONTEXT = "/backend";

    /*
     *	Context: /backend/reset
     *  Description: easy way for integration tests to delete all entities
     */
    @RequestMapping(method = RequestMethod.POST, value = CONTEXT + "/reset")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean clean() {
        return utilityService.reset();
    }
}

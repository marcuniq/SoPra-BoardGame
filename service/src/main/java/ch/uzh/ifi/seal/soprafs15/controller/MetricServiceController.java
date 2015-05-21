package ch.uzh.ifi.seal.soprafs15.controller;

import ch.uzh.ifi.seal.soprafs15.service.metric.MetricService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Marco
 */
@RestController
public class MetricServiceController extends GenericService{

    private final Logger logger = LoggerFactory.getLogger(MetricServiceController.class);

    private final String CONTEXT = "/metrics";

    @Autowired
    private MetricService metricService;

    /*
	 *	Context: /metrics/tts
     *  Description: Get time to start for every running or finished game
	 */
    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/tts")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Long> listTimeToStart(){
        return metricService.listTimeToStart();
    }

    /*
     *	Context: /metrics/tts/avg
     *  Description: Get average time to start
     */
    @RequestMapping(method = RequestMethod.GET, value = CONTEXT + "/tts/avg")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Double getAvgTimeToStart(){
        return metricService.getAvgTimeToStart();
    }
}

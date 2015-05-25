package ch.uzh.ifi.seal.soprafs15.service.metric;

import java.util.List;

/**
 * @author Marco
 */
public abstract class MetricService {
    public abstract List<Long> listTimeToStart();
    public abstract Double getAvgTimeToStart();
}

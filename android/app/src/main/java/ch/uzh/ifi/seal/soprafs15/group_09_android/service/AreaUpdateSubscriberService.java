package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.AbstractArea;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.AreaName;

/**
 * @author Marco
 *
 * Pattern: Singleton, Observer
 */
public class AreaUpdateSubscriberService {

    private static AreaUpdateSubscriberService instance;

    private HashMap<AreaName, List<AreaUpdateSubscriber>> subscriber;

    private AreaUpdateSubscriberService(){
        subscriber = new HashMap<>();

        for(AreaName n : AreaName.values())
            subscriber.put(n, new ArrayList<AreaUpdateSubscriber>());
    }

    public void addSubscriber(AreaName areaName, AreaUpdateSubscriber areaSubscriber){
        if(!subscriber.get(areaName).contains(areaSubscriber))
            subscriber.get(areaName).add(areaSubscriber);
    }

    public void removeSubscriber(AreaName areaName, AreaUpdateSubscriber areaSubscriber){
        subscriber.get(areaName).remove(areaSubscriber);
    }

    public void notifySubscriber(AreaName areaName, AbstractArea area){
        for(AreaUpdateSubscriber s : subscriber.get(areaName))
            s.onUpdate(area);
    }

    public void notifySubscriberOnError(AreaName areaName, String errorMessage){
        for(AreaUpdateSubscriber s : subscriber.get(areaName))
            s.onError(errorMessage);
    }

    public static AreaUpdateSubscriberService getInstance(){
        if(instance == null)
            instance = new AreaUpdateSubscriberService();

        return instance;
    }
}

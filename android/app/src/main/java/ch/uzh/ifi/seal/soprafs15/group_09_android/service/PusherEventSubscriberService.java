package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import java.util.ArrayList;
import java.util.HashMap;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;

/**
 * @author Marco
 * Patterns: Singleton, Observer
 *
 * Responsible for managing subscribers to events, notifies them upon new events
 */
public class PusherEventSubscriberService {

    private static PusherEventSubscriberService instance;

    private HashMap<PushEventNameEnum, ArrayList<PusherEventSubscriber>> subscriber;


    private PusherEventSubscriberService(){
        init();
    }

    private void init(){
        subscriber = new HashMap<>();

        for(PushEventNameEnum e : PushEventNameEnum.values())
            subscriber.put(e, new ArrayList<PusherEventSubscriber>());
    }


    public void addSubscriber(PushEventNameEnum event, PusherEventSubscriber eventSubscriber){
        if(!subscriber.get(event).contains(eventSubscriber))
            subscriber.get(event).add(eventSubscriber);
    }

    public void removeSubscriber(PushEventNameEnum event, PusherEventSubscriber eventSubscriber){
        subscriber.get(event).remove(eventSubscriber);
    }

    public void removeAllSubscriber(){
        subscriber.clear();
        init();
    }

    public void notifySubscriber(PushEventNameEnum eventNameEnum, AbstractPusherEvent event){
        for(PusherEventSubscriber s : subscriber.get(eventNameEnum))
            s.onNewEvent(event);
    }

    public static PusherEventSubscriberService getInstance(){
        if(instance == null)
            instance = new PusherEventSubscriberService();

        return instance;
    }
}

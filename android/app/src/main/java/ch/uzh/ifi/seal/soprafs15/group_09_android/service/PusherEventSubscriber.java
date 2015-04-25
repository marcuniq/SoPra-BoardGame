package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.AbstractPusherEvent;

/**
 * @author Marco
 */
public abstract class PusherEventSubscriber {
    public abstract void onNewEvent(AbstractPusherEvent event);
}

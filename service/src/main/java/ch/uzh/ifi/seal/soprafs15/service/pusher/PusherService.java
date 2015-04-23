package ch.uzh.ifi.seal.soprafs15.service.pusher;

import ch.uzh.ifi.seal.soprafs15.controller.GenericService;
import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.AbstractPushEvent;

/**
 * @author Marco
 */
public abstract class PusherService extends GenericService {

    public abstract void pushToSubscribers(AbstractPushEvent event, Game game);

}

package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public class GameStartEvent extends AbstractPushEvent {

    public GameStartEvent() {
        super(PushEventNameEnum.GAME_START_EVENT);
    }
}

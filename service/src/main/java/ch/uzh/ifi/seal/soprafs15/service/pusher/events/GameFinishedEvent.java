package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public class GameFinishedEvent extends AbstractPushEvent{


    public GameFinishedEvent() {
        super(PushEventNameEnum.GAME_FINISHED_EVENT);
    }
}

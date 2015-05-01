package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public class FastModeAlmostFinishedEvent extends AbstractPushEvent {

    public FastModeAlmostFinishedEvent() {
        super(PushEventNameEnum.FAST_MODE_ALMOST_FINISHED_EVENT);
    }
}

package ch.uzh.ifi.seal.soprafs15.service.pusher;

import ch.uzh.ifi.seal.soprafs15.model.game.Game;
import ch.uzh.ifi.seal.soprafs15.service.pusher.events.AbstractPushEvent;
import com.pusher.rest.Pusher;
import com.pusher.rest.data.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Marco
 */
@Service("pusherService")
public class PusherServiceImpl extends PusherService {

    Logger logger = LoggerFactory.getLogger(PusherServiceImpl.class);

    private final String APP_ID = "116207";
    private final String KEY = "0f12b4dc1fd1743e1c87";
    private final String SECRET = "34bac97761edbdabdbe3";

    protected Pusher pusher;


    public PusherServiceImpl(){
        init();
    }

    /**
     * Initialize pusher with credentials
     */
    private void init(){
        pusher = new Pusher(APP_ID, KEY, SECRET);
        pusher.setRequestTimeout(8000);
    }

    /**
     *
     * @param event
     * @param game
     */
    @Override
    public void pushToSubscribers(AbstractPushEvent event, Game game) {

        Result triggerResult = pusher.trigger(game.getPusherChannelName(), event.getPushEventNameEnum().toString(), event);

        if (triggerResult.getStatus() != Result.Status.SUCCESS) {
            if (triggerResult.getStatus().shouldRetry()) {
                // Temporary, let's schedule a retry

            }
            else {
                // Something is wrong with our request
                logger.debug("trigger result message: " + triggerResult.getMessage());
                logger.debug("trigger result http status: " + triggerResult.getHttpStatus());

            }
        }
    }
}

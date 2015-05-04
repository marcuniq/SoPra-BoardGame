package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.GameFinishedEventBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.LegOverEventBean;

/**
 * @author Josua
 */
public class GameFinishedEvent extends AbstractPusherEvent {

    public GameFinishedEvent(GameFinishedEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
    }
}

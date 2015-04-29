package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.GameStartEventBean;

/**
 * @author Marco
 */
public class GameStartEvent extends AbstractPusherEvent {

    public GameStartEvent(GameStartEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
    }
}

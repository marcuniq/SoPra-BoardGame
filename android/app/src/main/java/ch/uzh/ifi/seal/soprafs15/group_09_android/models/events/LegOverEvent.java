package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.LegOverEventBean;

/**
 * @author Marco
 */
public class LegOverEvent extends AbstractPusherEvent {

    public LegOverEvent(LegOverEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
    }
}

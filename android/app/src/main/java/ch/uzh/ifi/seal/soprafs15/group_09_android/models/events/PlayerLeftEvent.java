package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.PlayerLeftEventBean;

/**
 * @author Marco
 */
public class PlayerLeftEvent extends AbstractPusherEvent {

    private Long userId;

    public PlayerLeftEvent(PlayerLeftEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
        this.userId = bean.userId();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

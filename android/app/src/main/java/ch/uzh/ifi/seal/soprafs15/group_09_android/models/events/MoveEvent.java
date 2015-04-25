package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.MoveEventBean;

/**
 * @author Marco
 */
public class MoveEvent extends AbstractPusherEvent {

    private Long moveId;

    public MoveEvent(MoveEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
        this.moveId = bean.moveId();
    }

    public Long getMoveId() {
        return moveId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }
}

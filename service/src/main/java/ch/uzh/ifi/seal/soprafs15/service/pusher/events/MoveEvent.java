package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.MoveEnum;

/**
 * @author Marco
 */
public class MoveEvent extends AbstractPushEvent {

    private Long moveId;

    public MoveEvent(Long moveId) {
        super(PushEventNameEnum.MOVE_EVENT);
        this.moveId = moveId;
    }

    public Long getMoveId() {
        return moveId;
    }

    public void setMoveId(Long moveId) {
        this.moveId = moveId;
    }
}

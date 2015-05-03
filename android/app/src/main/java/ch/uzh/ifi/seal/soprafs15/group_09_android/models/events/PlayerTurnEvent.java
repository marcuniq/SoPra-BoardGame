package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.PlayerTurnEventBean;

/**
 * @author Marco
 */
public class PlayerTurnEvent extends AbstractPusherEvent {

    private Integer playerId;

    public PlayerTurnEvent(PlayerTurnEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
        this.playerId = bean.playerId();
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
}

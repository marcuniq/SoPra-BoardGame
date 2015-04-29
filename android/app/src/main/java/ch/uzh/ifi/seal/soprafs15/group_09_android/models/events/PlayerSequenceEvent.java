package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import java.util.Map;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans.PlayerSequenceEventBean;

/**
 * @author Marco
 */
public class PlayerSequenceEvent extends AbstractPusherEvent {

    private Map<Long, Integer> userIdToPlayerIdMap;

    public PlayerSequenceEvent(PlayerSequenceEventBean bean){
        this.pushEventNameEnum = bean.pushEventNameEnum();
        this.userIdToPlayerIdMap = bean.userIdToPlayerIdMap();
    }

    public Map<Long, Integer> getUserIdToPlayerIdMap() {
        return userIdToPlayerIdMap;
    }

    public void setUserIdToPlayerIdMap(Map<Long, Integer> userIdToPlayerIdMap) {
        this.userIdToPlayerIdMap = userIdToPlayerIdMap;
    }
}

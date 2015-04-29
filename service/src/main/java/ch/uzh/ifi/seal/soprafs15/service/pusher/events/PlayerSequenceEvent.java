package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

import java.util.Map;

/**
 * @author Marco
 */
public class PlayerSequenceEvent extends AbstractPushEvent {

    private Map<Long, Integer> userIdToPlayerIdMap;

    public PlayerSequenceEvent(Map<Long, Integer> userIdToPlayerIdMap) {
        super(PushEventNameEnum.PLAYER_SEQUENCE_EVENT);
        this.userIdToPlayerIdMap = userIdToPlayerIdMap;
    }

    public Map<Long, Integer> getUserIdToPlayerIdMap() {
        return userIdToPlayerIdMap;
    }

    public void setUserIdToPlayerIdMap(Map<Long, Integer> userIdToPlayerIdMap) {
        this.userIdToPlayerIdMap = userIdToPlayerIdMap;
    }
}

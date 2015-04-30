package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

import java.util.Map;

/**
 * @author Marco
 */
public class GameStartEvent extends AbstractPushEvent {

    private Map<Long, Integer> userIdToPlayerIdMap;

    public GameStartEvent(Map<Long, Integer> userIdToPlayerIdMap) {
        super(PushEventNameEnum.GAME_START_EVENT);
        this.userIdToPlayerIdMap = userIdToPlayerIdMap;
    }

    public Map<Long, Integer> getUserIdToPlayerIdMap() {
        return userIdToPlayerIdMap;
    }

    public void setUserIdToPlayerIdMap(Map<Long, Integer> userIdToPlayerIdMap) {
        this.userIdToPlayerIdMap = userIdToPlayerIdMap;
    }
}

package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public class PlayerJoinedEvent extends AbstractPushEvent {

    private Long userId;

    public PlayerJoinedEvent(Long userId) {
        super(PushEventNameEnum.PLAYER_JOINED_EVENT);
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public class PlayerLeftEvent extends AbstractPushEvent {

    private Long userId; // id of user who left

    public PlayerLeftEvent() {
        super(PushEventNameEnum.PLAYER_LEFT_EVENT);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

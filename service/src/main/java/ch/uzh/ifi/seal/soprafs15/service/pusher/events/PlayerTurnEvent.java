package ch.uzh.ifi.seal.soprafs15.service.pusher.events;

/**
 * @author Marco
 */
public class PlayerTurnEvent extends AbstractPushEvent {

    private Integer playerId;

    public PlayerTurnEvent(Integer playerId) {
        super(PushEventNameEnum.PLAYER_TURN_EVENT);
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
}

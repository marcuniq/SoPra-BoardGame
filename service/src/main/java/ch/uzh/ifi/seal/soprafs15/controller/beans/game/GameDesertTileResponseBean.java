package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

/**
 * @author Marco
 */
public class GameDesertTileResponseBean extends GameRaceTrackObjectResponseBean {

    private Long playerId;
    private Boolean isOasis;

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Boolean getIsOasis() {
        return isOasis;
    }

    public void setIsOasis(Boolean isOasis) {
        this.isOasis = isOasis;
    }
}

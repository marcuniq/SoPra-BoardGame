package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class LegBettingTile extends Card {

    @Column
    private Integer leadingPositionGain;

    @Column
    private Integer secondPositionGain;

    @Column
    private Integer otherPositionLoss;

    public Integer getLeadingPositionGain() {
        return leadingPositionGain;
    }

    public void setLeadingPositionGain(Integer leadingPositionGain) {
        this.leadingPositionGain = leadingPositionGain;
    }

    public Integer getSecondPositionGain() {
        return secondPositionGain;
    }

    public void setSecondPositionGain(Integer secondPositionGain) {
        this.secondPositionGain = secondPositionGain;
    }

    public Integer getOtherPositionLoss() {
        return otherPositionLoss;
    }

    public void setOtherPositionLoss(Integer otherPositionLoss) {
        this.otherPositionLoss = otherPositionLoss;
    }
}

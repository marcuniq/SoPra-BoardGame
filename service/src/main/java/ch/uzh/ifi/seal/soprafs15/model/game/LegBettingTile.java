package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBettingTile extends Card implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column(nullable = false)
    private Integer leadingPositionGain;

    @Column(nullable = false)
    private Integer secondPositionGain;

    @Column(nullable = false)
    private Integer otherPositionLoss;

    @ManyToOne
    private User user;


    public LegBettingTile(){

    }

    public LegBettingTile(Integer leadingPositionGain, Integer secondPositionGain, Integer otherPositionLoss){
        this.leadingPositionGain = leadingPositionGain;
        this.secondPositionGain = secondPositionGain;
        this.otherPositionLoss = otherPositionLoss;
    }


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

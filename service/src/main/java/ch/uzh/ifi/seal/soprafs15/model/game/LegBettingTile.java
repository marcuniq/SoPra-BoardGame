package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;
import ch.uzh.ifi.seal.soprafs15.model.move.LegBetting;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBettingTile extends Card implements Serializable {

    private static final long serialVersionUID = 1L;


    @Column
    private Integer leadingPositionGain;

    @Column
    private Integer secondPositionGain;

    @Column
    private Integer otherPositionLoss;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="LEGBETTINGTILESTACK_ID")
    @JsonIgnore
    private LegBettingTileStack stack;

    @ManyToOne
    @JsonIgnore
    private User user;

    public LegBettingTile(){}

    public LegBettingTile(Color c, Integer leadingPositionGain, Integer secondPositionGain, Integer otherPositionLoss){
        this.color = c;
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

    public LegBettingTileStack getStack() {
        return stack;
    }

    public void setStack(LegBettingTileStack stack) {
        this.stack = stack;
    }
}

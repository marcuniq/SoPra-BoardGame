package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBettingTile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Color color;


    @Column
    private Integer leadingPositionGain;

    @Column
    private Integer secondPositionGain;

    @Column
    private Integer otherPositionLoss;




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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

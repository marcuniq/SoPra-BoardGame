package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackObjectResponseBean;

import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
//@Entity
public abstract class RaceTrackObject implements Serializable {

    protected Integer position;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }


    public abstract GameRaceTrackObjectResponseBean toBean();
}
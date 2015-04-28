package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.controller.beans.game.GameRaceTrackObjectResponseBean;

import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
//@Entity
public abstract class RaceTrackObject implements Serializable {


    public abstract GameRaceTrackObjectResponseBean toBean();
}
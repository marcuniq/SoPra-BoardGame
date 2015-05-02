package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.Camel;

import java.util.List;

/**
 * @author Marco
 */
public class GameCamelStackResponseBean extends GameRaceTrackObjectResponseBean {

    private List<Camel> stack;

    public List<Camel> getStack() {
        return stack;
    }

    public void setStack(List<Camel> stack) {
        this.stack = stack;
    }
}

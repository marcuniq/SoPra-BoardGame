package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.Color;

/**
 * @author Marco
 */
public class GameCamelResponseBean extends GameRaceTrackObjectResponseBean {

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

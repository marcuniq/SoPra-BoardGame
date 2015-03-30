package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class Camel extends RaceTrackObject {

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

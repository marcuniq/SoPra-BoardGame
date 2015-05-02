package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 *
 * Simple wrapper class for Color
 */
@Embeddable
public class Camel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color color;

    public Camel(){}
    public Camel(Color color){
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

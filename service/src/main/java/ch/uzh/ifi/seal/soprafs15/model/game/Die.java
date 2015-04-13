package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Embeddable
public class Die implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color color;

    private Integer faceValue;

    public Die(){}

    public Die(Color c, Integer v){
        this.color = c;
        this.faceValue = v;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Integer getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;
    }
}

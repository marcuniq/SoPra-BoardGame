package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Embeddable
public class Dice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Color color;

    private Integer faceValue;

    public Dice(){}

    public Dice(Color c, Integer v){
        this.color = c;
        this.faceValue = v;
    }

    // returns number between 1 and 3
    public Integer rollDice() {
        return (int) Math.random() % 3 + 1;
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

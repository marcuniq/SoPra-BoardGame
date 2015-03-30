package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class Dice implements Serializable {

    @Column
    private Color color;

    @Column
    private Integer faceValue;

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

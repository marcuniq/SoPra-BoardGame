package ch.uzh.ifi.seal.soprafs15.model.move;

import ch.uzh.ifi.seal.soprafs15.model.Move;
import ch.uzh.ifi.seal.soprafs15.model.game.Dice;

import javax.persistence.Column;

/**
 * Created by Hakuna on 30.03.2015.
 */
public class DiceRolling extends Move {

    @Column
    private Dice dice;

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}

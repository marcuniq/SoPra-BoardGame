package ch.uzh.ifi.seal.soprafs15.model.game;

import org.aspectj.lang.annotation.control.CodeGenerationHint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * Created by Hakuna on 30.03.2015.

 */
@Entity
public class DiceArea implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Stack<Dice> diceInPyramid;

    @Column
    private List<Dice> rolledDice;

    private void init() {

    }

    public Stack<Dice> getDiceInPyramid() {
        return diceInPyramid;
    }

    public void setDiceInPyramid(Stack<Dice> diceInPyramid) {
        this.diceInPyramid = diceInPyramid;
    }

    public List<Dice> getRolledDice() {
        return rolledDice;
    }

    public void setRolledDice(List<Dice> rolledDice) {
        this.rolledDice = rolledDice;
    }
}

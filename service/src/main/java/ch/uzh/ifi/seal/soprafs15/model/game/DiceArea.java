package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @ElementCollection(fetch=FetchType.EAGER)
    @Column
    @Embedded
    private List<Dice> diceInPyramid;

    @ElementCollection(fetch=FetchType.EAGER)
    @Column
    @Embedded
    private List<Dice> rolledDice;

    public DiceArea(){}


    private void init() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Dice> getDiceInPyramid() {
        return diceInPyramid;
    }

    public void setDiceInPyramid(List<Dice> diceInPyramid) {
        this.diceInPyramid = diceInPyramid;
    }

    public List<Dice> getRolledDice() {
        return rolledDice;
    }

    public void setRolledDice(List<Dice> rolledDice) {
        this.rolledDice = rolledDice;
    }
}

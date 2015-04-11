package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


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

    @ElementCollection
    @Column
    @Embedded
    @OrderColumn
    private List<Die> diceInPyramid;

    @ElementCollection
    @Column
    @Embedded
    @OrderColumn
    private List<Die> rolledDice;

    @OneToOne(cascade = CascadeType.ALL)//(fetch = FetchType.EAGER)
    @JoinColumn(name="GAME_ID")
    private Game game;

    @Transient
    private Random r = new Random();

    public DiceArea(){
        init();
    }


    public void init() {
        diceInPyramid = new ArrayList<Die>();
        rolledDice = new ArrayList<Die>();

        for(Color c : Color.values()){
            Die d = new Die(c, r.nextInt(3) + 1);
            diceInPyramid.add(d);
        }
        
        Collections.shuffle(diceInPyramid);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Die> getDiceInPyramid() {
        return diceInPyramid;
    }

    public void setDiceInPyramid(List<Die> diceInPyramid) {
        this.diceInPyramid = diceInPyramid;
    }

    public List<Die> getRolledDice() {
        return rolledDice;
    }

    public void setRolledDice(List<Die> rolledDice) {
        this.rolledDice = rolledDice;
    }

    public Die rollDice() {
        int size = diceInPyramid.size();

        if(size > 0) {
            Die die = diceInPyramid.remove(size - 1);
            rolledDice.add(die);

            return die;
        }
        return null;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

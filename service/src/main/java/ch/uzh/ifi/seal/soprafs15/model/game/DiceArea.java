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

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    @Embedded
    @OrderColumn
    private List<Die> diceInPyramid;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column
    @Embedded
    @OrderColumn
    private List<Die> rolledDice;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "GAMESTATE_ID")
    private GameState gameState;

    @Transient
    private Random r = new Random(System.currentTimeMillis());

    public DiceArea(){
        init();
    }


    /**
     * Initialize a list of dice with random face values
     * and shuffle dice s.t. we have a random order of colors
     */
    public void init() {
        diceInPyramid = new ArrayList<Die>();
        rolledDice = new ArrayList<Die>();

        for(Color c : Color.values()){
            Die d = new Die(c, r.nextInt(3) + 1);
            diceInPyramid.add(d);
        }
        
        Collections.shuffle(diceInPyramid);
    }

    /**
     * Get a die from the already shuffled pyramid
     * @return Die
     */
    public Die rollDice() {
        int size = diceInPyramid.size();

        if(size > 0) {
            Die die = diceInPyramid.remove(size - 1);
            rolledDice.add(die);

            return die;
        }
        return null;
    }

    /**
     * Undo action for fast mode
     */
    public void undoRollDice(){
        if(rolledDice.size() > 0){
            Die die = rolledDice.remove(rolledDice.size()-1);
            diceInPyramid.add(die);
        }
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

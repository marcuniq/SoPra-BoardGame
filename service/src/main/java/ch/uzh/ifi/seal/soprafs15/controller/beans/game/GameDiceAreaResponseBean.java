package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.Die;

import java.util.List;

/**
 * @author Marco
 */
public class GameDiceAreaResponseBean {

    private Long id;
    private List<Die> rolledDice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Die> getRolledDice() {
        return rolledDice;
    }

    public void setRolledDice(List<Die> rolledDice) {
        this.rolledDice = rolledDice;
    }
}

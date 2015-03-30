package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBettingArea implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private HashMap<Color, Stack<LegBettingTile>> legBettingTiles;

    private void init() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HashMap<Color, Stack<LegBettingTile>> getLegBettingTiles() {
        return legBettingTiles;
    }

    public void setLegBettingTiles(HashMap<Color, Stack<LegBettingTile>> legBettingTiles) {
        this.legBettingTiles = legBettingTiles;
    }

    public List<LegBettingTile> topLegBettingTiles() {
        return null;
    }



}

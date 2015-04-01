package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
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

    @ElementCollection
    @Column
    private Map<Color, Stack<LegBettingTile>> legBettingTiles;


    public LegBettingArea(){

    }

    private void init() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Color, Stack<LegBettingTile>> getLegBettingTiles() {
        return legBettingTiles;
    }

    public void setLegBettingTiles(Map<Color, Stack<LegBettingTile>> legBettingTiles) {
        this.legBettingTiles = legBettingTiles;
    }

    public List<LegBettingTile> topLegBettingTiles() {
        return null;
    }



}

package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.Stack;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */
@Entity
public class LegBettingTileStack implements Serializable, Stack<LegBettingTile> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private Color color;

    @ManyToOne(cascade = CascadeType.ALL)
    private LegBettingArea legBettingArea;

    @OneToMany(mappedBy="stack", cascade = CascadeType.ALL)
    private List<LegBettingTile> tiles;

    public LegBettingTileStack(){}

    public LegBettingTileStack(LegBettingArea legBettingArea, Color color, List<LegBettingTile> tiles){
        this.legBettingArea = legBettingArea;
        this.color = color;
        this.tiles = tiles;
        this.tiles.stream().forEach(t -> t.setStack(this));
    }

    /**
     * Remove top tile from stack
     * @return top tile from stack
     */
    public LegBettingTile pop(){
        LegBettingTile result = null;
        if(tiles.size() > 0)
            result = tiles.remove(tiles.size()-1);

        return result;
    }

    /**
     * See top tile
     * @return top tile from stack
     */
    public LegBettingTile peek(){
        LegBettingTile result = null;
        if(tiles.size() > 0)
            result = tiles.get(tiles.size() - 1);

        return result;
    }

    /**
     * Push onto stack
     * @param tile
     */
    public void push(LegBettingTile tile){
        if(tiles == null)
            tiles = new ArrayList<>();

        tiles.add(tile);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LegBettingTile> getTiles() {
        return tiles;
    }

    public void setTiles(List<LegBettingTile> tiles) {
        this.tiles = tiles;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public LegBettingArea getLegBettingArea() {
        return legBettingArea;
    }

    public void setLegBettingArea(LegBettingArea legBettingArea) {
        this.legBettingArea = legBettingArea;
    }
}

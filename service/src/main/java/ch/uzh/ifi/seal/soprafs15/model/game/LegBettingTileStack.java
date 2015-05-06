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


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "LEGBETTINGTILESTACK_ID")
    private List<LegBettingTile> tiles;

    public LegBettingTileStack(){}

    public LegBettingTileStack(Color color, List<LegBettingTile> tiles){
        this.color = color;
        this.tiles = tiles;
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
}

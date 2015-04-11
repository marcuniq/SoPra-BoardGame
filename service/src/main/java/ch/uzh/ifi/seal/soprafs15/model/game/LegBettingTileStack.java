package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marco
 */
@Entity
public class LegBettingTileStack implements Serializable {

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
    private List<LegBettingTile> tiles = new ArrayList<>();

    public LegBettingTileStack(){}

    public LegBettingTileStack(Color c, List<LegBettingTile> tiles){
        this.color = color;
        this.tiles = tiles;
    }

    public LegBettingTile pop(){
        LegBettingTile result = null;
        if(tiles.size() > 0)
            result = tiles.remove(tiles.size()-1);

        return result;
    }

    public LegBettingTile peek(){
        LegBettingTile result = null;
        if(tiles.size() > 0)
            result = tiles.get(tiles.size() - 1);

        return result;
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

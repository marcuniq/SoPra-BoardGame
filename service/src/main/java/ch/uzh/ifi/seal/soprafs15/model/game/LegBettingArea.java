package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.move.LegBetting;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class LegBettingArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "legBettingArea", cascade = CascadeType.ALL)
    @Column(columnDefinition = "BLOB")
    @MapKeyColumn(name = "color", length = 50, nullable = false)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Color, LegBettingTileStack> legBettingTiles;


    public LegBettingArea(){
        init();
    }

    /**
     * Initialize leg betting area with the needed leg betting tiles
     */
    public void init() {
        legBettingTiles = new HashMap<>();

        for(Color c : Color.values()){
            LegBettingTile tile_5 = new LegBettingTile(c, 5, 1, -1);
            LegBettingTile tile_3 = new LegBettingTile(c, 3, 1, -1);
            LegBettingTile tile_2 = new LegBettingTile(c, 2, 1, -1);

            List<LegBettingTile> list = new ArrayList<LegBettingTile>();
            list.add(tile_2);
            list.add(tile_3);
            list.add(tile_5);

            legBettingTiles.put(c, new LegBettingTileStack(this, c, list));
        }
    }

    /**
     * Player needs to see the top tiles s.t. she can make a move decision
     * @return a list of LegBettingTile which are the top tile of each stack
     */
    public List<LegBettingTile> topLegBettingTiles() {
        List result = new ArrayList<LegBettingTile>();

        for(Color color : legBettingTiles.keySet()) {
            LegBettingTileStack stack = legBettingTiles.get(color);

            result.add(stack.peek());
        }

        return result;
    }

    /**
     * Remove LegBettingTile from stack
     * @param c Color
     * @return top LegBettingTile of that particular color
     */
    public LegBettingTile popLegBettingTile(Color c) {
        LegBettingTile tile = legBettingTiles.get(c).pop();
        return tile;
    }

    public LegBettingTile peekLegBettingTile(Color c){
        return legBettingTiles.get(c).peek();
    }

    public void pushLegBettingTile(LegBettingTile legBettingTile) {
        LegBettingTileStack stack = legBettingTiles.get(legBettingTile.getColor());
        stack.push(legBettingTile);
    }

    public void pushAndSort(List<LegBettingTile> tilesToPush){
        for(Color c : Color.values()){
            LegBettingTileStack stack = legBettingTiles.get(c);

            tilesToPush.stream()
                    .filter(t -> t.getColor() == c)
                    .sorted((t1, t2) -> Integer.compare(t2.getLeadingPositionGain(), t1.getLeadingPositionGain()))
                    .forEachOrdered(t -> stack.push(t));
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Color, LegBettingTileStack> getLegBettingTiles() {
        return legBettingTiles;
    }

    public void setLegBettingTiles(Map<Color, LegBettingTileStack> legBettingTiles) {
        this.legBettingTiles = legBettingTiles;
    }
}

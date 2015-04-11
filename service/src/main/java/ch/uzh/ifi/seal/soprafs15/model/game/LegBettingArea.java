package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.move.LegBetting;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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

    @OneToMany(mappedBy = "legBettingArea", cascade=CascadeType.ALL)
    @Column(columnDefinition = "BLOB")
    @MapKeyColumn(name = "color", length = 50, nullable = false)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Color, LegBettingTileStack> legBettingTiles;

    @OneToOne(cascade=CascadeType.ALL)//(fetch = FetchType.EAGER)
    @JoinColumn(name="GAME_ID")
    private Game game;

    public LegBettingArea(){
        init();
    }

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

            legBettingTiles.put(c, new LegBettingTileStack(c, list));
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

    public List<LegBettingTile> topLegBettingTiles() {
        List result = new ArrayList<LegBettingTile>();

        for(Color color : legBettingTiles.keySet()) {
            LegBettingTileStack stack = legBettingTiles.get(color);

            result.add(stack.peek());
        }

        return result;
    }

    public LegBettingTile getLegBettingTile(Color c) {
        return legBettingTiles.get(c).pop();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

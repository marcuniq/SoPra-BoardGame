package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

import ch.uzh.ifi.seal.soprafs15.model.game.LegBettingTile;

import java.util.List;

/**
 * @author Marco
 */
public class GameLegBettingAreaResponseBean {

    private Long id;
    private List<LegBettingTile> topLegBettingTiles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LegBettingTile> getTopLegBettingTiles() {
        return topLegBettingTiles;
    }

    public void setTopLegBettingTiles(List<LegBettingTile> topLegBettingTiles) {
        this.topLegBettingTiles = topLegBettingTiles;
    }
}

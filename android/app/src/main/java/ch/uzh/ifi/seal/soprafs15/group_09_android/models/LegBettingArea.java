package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.LegBettingAreaBean;

/**
 * @author Marco
 */
public class LegBettingArea extends AbstractArea {

    private Long id;
    private List<LegBettingTile> topLegBettingTiles;

    public LegBettingArea(LegBettingAreaBean bean){
        this.id = bean.id();
        this.topLegBettingTiles = bean.topLegBettingTiles();
    }

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

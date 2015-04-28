package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Looper;

import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceBettingAreaBean;

/**
 * @author Marco
 */
public class RaceBettingArea extends AbstractArea {

    private Long id;
    private Integer nrOfWinnerBetting;
    private Integer nrOfLoserBetting;

    public RaceBettingArea(RaceBettingAreaBean bean){
        this.id = bean.id();
        this.nrOfWinnerBetting = bean.nrOfWinnerBetting();
        this.nrOfLoserBetting = bean.nrOfLoserBetting();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNrOfWinnerBetting() {
        return nrOfWinnerBetting;
    }

    public void setNrOfWinnerBetting(Integer nrOfWinnerBetting) {
        this.nrOfWinnerBetting = nrOfWinnerBetting;
    }

    public Integer getNrOfLoserBetting() {
        return nrOfLoserBetting;
    }

    public void setNrOfLoserBetting(Integer nrOfLoserBetting) {
        this.nrOfLoserBetting = nrOfLoserBetting;
    }
}

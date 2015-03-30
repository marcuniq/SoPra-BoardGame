package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceBettingArea implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private List<RaceBettingCard> winnerBetting;

    @Column
    private List<RaceBettingCard> loserBetting;

    private void init() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RaceBettingCard> getWinnerBetting() {
        return winnerBetting;
    }

    public void setWinnerBetting(List<RaceBettingCard> winnerBetting) {
        this.winnerBetting = winnerBetting;
    }

    public List<RaceBettingCard> getLoserBetting() {
        return loserBetting;
    }

    public void setLoserBetting(List<RaceBettingCard> loserBetting) {
        this.loserBetting = loserBetting;
    }
}

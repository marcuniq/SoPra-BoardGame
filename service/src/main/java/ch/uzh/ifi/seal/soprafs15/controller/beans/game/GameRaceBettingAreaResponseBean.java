package ch.uzh.ifi.seal.soprafs15.controller.beans.game;

/**
 * @author Marco
 */
public class GameRaceBettingAreaResponseBean {

    private Long id;
    private Integer nrOfWinnerBetting;
    private Integer nrOfLoserBetting;

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

package ch.uzh.ifi.seal.soprafs15.model.game;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
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

    @ElementCollection
    @Column
    private List<RaceBettingCard> winnerBetting = new ArrayList<>();

    @ElementCollection
    @Column
    private List<RaceBettingCard> loserBetting = new ArrayList<>();

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="GAME_ID")
    private Game game;


    public RaceBettingArea(){

    }

    /**
     *
     */
    private void init() {

    }

    /**
     * Call to place race betting card on winner stack
     * @param raceBettingCard
     */
    public void betOnWinner(RaceBettingCard raceBettingCard){
        winnerBetting.add(raceBettingCard);
    }

    /**
     * Call to place race betting card on loser stack
     * @param raceBettingCard
     */
    public void betOnLoser(RaceBettingCard raceBettingCard){
        loserBetting.add(raceBettingCard);
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

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

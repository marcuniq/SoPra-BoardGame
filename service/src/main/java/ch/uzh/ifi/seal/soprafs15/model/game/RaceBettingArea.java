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

    @OneToOne(cascade = CascadeType.ALL)//(fetch = FetchType.EAGER)
    private GameState gameState;


    public RaceBettingArea(){

    }

    /**
     *
     */
    private void init() {

    }

    public Integer getNrOfWinnerBetting(){
        return winnerBetting.size();
    }
    public Integer getNrOfLoserBetting() {
        return loserBetting.size();
    }


    /**
     * Call to place race betting card on winner stack
     * @param raceBettingCard
     */
    public void bet(RaceBettingCard raceBettingCard, Boolean betOnWinner){

        if(betOnWinner)
            winnerBetting.add(raceBettingCard);
        else
            loserBetting.add(raceBettingCard);
    }

    /**
     * Undo action for fast mode
     */
    public RaceBettingCard undoBet(Boolean betOnWinner){
        if(betOnWinner)
            return winnerBetting.remove(winnerBetting.size() - 1);
        else
            return loserBetting.remove(loserBetting.size()-1);
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

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

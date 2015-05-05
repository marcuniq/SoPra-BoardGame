package ch.uzh.ifi.seal.soprafs15.model.game;

import ch.uzh.ifi.seal.soprafs15.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Hakuna on 30.03.2015.
 */
@Entity
public class RaceBettingArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "raceBettingArea", cascade=CascadeType.ALL)
    @Column(columnDefinition = "BLOB")
    @MapKeyColumn(name = "betOnWinner",nullable = false)
    private Map<Boolean, RaceBettingCardStack> raceBettings;

    @OneToOne(cascade = CascadeType.ALL)//(fetch = FetchType.EAGER)
    private GameState gameState;


    public RaceBettingArea(){
        init();
    }

    /**
     * Initialization of RaceBettingArea
     */
    private void init() {
        raceBettings = new HashMap<>();

        RaceBettingCardStack winnerBetting = new RaceBettingCardStack(this, true);
        raceBettings.put(true, winnerBetting);

        RaceBettingCardStack loserBetting = new RaceBettingCardStack(this, false);
        raceBettings.put(false, loserBetting);
    }

    public Integer getNrOfWinnerBetting(){
        return raceBettings.get(true).size();
    }
    public Integer getNrOfLoserBetting() {
        return raceBettings.get(false).size();
    }


    /**
     * Call to place race betting card on winner or loser stack
     * @param raceBettingCard
     */
    public void bet(RaceBettingCard raceBettingCard, Boolean betOnWinner){
        raceBettings.get(betOnWinner).push(raceBettingCard);
    }

    /**
     * Undo action for fast mode
     */
    public RaceBettingCard undoBet(Boolean betOnWinner){
        return raceBettings.get(betOnWinner).pop();
    }

    /**
     * Helper method to return race betting stack on winner
     * @return
     */
    public RaceBettingCardStack getWinnerBetting() {
        return raceBettings.get(true);
    }
    public void setWinnerBetting(RaceBettingCardStack stack){
        raceBettings.put(true, stack);
    }

    /**
     * Helper method to return race betting stack on loser
     * @return
     */
    public RaceBettingCardStack getLoserBetting() {
        return raceBettings.get(false);
    }
    public void setLoserBetting(RaceBettingCardStack stack){
        raceBettings.put(false, stack);
    }


    public void removePlayersBet(Long userId){
        for(Boolean b : raceBettings.keySet()){
            raceBettings.get(b).removePlayersBet(userId);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Map<Boolean, RaceBettingCardStack> getRaceBettings() {
        return raceBettings;
    }

    public void setRaceBettings(Map<Boolean, RaceBettingCardStack> raceBettings) {
        this.raceBettings = raceBettings;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class RaceBet {
    private ArrayList<Integer> raceBets = new ArrayList<>();
    private Integer characterCard;
    private Integer characterCardButton;

    public RaceBet(Integer characterCard, Integer characterCardButton) {
        this.characterCard = characterCard;
        this.characterCardButton = characterCardButton;
    }

    public void add(Integer raceBet) {
        raceBets.add(raceBet);
    }

    public void clear(){
        raceBets.clear();
    }

    public ArrayList<Integer> getAllRaceBetCards() {
        if (raceBets.isEmpty()) return null;
        else return raceBets;
    }

    public boolean isEmpty(){
        return raceBets.isEmpty();
    }

    public Integer getCharacterCard() {
        return characterCard;
    }

    public Integer getCharacterCardButton() {
        return characterCardButton;
    }

    public void setCharacterCard(Integer characterCard) {
        this.characterCard = characterCard;
    }

    public void setCharacterCardButton(Integer characterCardButton) {
        this.characterCardButton = characterCardButton;
    }
}

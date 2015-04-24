package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class LegBet {
    private ArrayList<Integer> legBets = new ArrayList<>();
    private ArrayList<Integer> legBetButtons = new ArrayList<>();
    private int legBetPointer = 0;

    public LegBet() { }

    public void add(ArrayList<Integer> someLegBets) {
        for (Integer legBet: someLegBets){
            legBets.add(legBet);
        }
    }

    public void add(Integer legBet, Integer legBetButton) {
        legBets.add(legBet);
        legBetButtons.add(legBetButton);
    }

    public void clear(){
        legBets.clear();
        legBetButtons.clear();
    }

    public Integer getCurrentLegBet() {
        if (legBets.isEmpty()) return null;
        else return legBets.get(legBetPointer);
    }

    public Integer getCurrentLegBetButton() {
        if (legBetButtons.isEmpty()) return null;
        else return legBetButtons.get(legBetPointer);
    }

    public boolean isEmpty(){
        return legBets.isEmpty() && legBetButtons.isEmpty();
    }

    public void setLegBetPointer(int pos){
        if (legBets.isEmpty()) legBetPointer = 0;
        else if ( (pos > (legBets.size() - 1)) || (pos < 0) ) legBetPointer = (legBets.size() - 1);
        else legBetPointer = pos;
    }

    public int getLegBetPointer(){
        return legBetPointer;
    }
}

package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class LegBet {
    private ArrayList<Integer> legBets = new ArrayList<>();
    private int legBetPointer = 0;

    public LegBet() { }

    public void add(ArrayList<Integer> someLegBets) {
        for (Integer legBet: someLegBets){
            legBets.add(legBet);
        }
    }

    public void add(Integer dice) {
        legBets.add(dice);
    }

    public void clear(){
        legBets.clear();
    }

    public Integer getCurrentLegBet() {
        if (legBets.isEmpty()) return null;
        else return legBets.get(legBetPointer);
    }

    public boolean isEmpty(){
        return legBets.isEmpty();
    }

    public void setLegBetPointer(int pos){
        if ( (pos > (legBets.size() - 1)) || (pos < 0) ) legBetPointer = 0;
        else legBetPointer = pos;
    }
}

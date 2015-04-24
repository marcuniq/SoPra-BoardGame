package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class RaceBet {
    private ArrayList<Integer> raceBets = new ArrayList<>();
    private ArrayList<Integer> raceBetButtons = new ArrayList<>();
    private int raceBetPointer = 0;

    public RaceBet() { }

    public void add(ArrayList<Integer> someRaceBets) {
        for (Integer raceBet : someRaceBets){
            raceBets.add(raceBet);
        }
    }

    public void add(Integer raceBet, Integer raceBetButton) {
        raceBets.add(raceBet);
        raceBetButtons.add(raceBetButton);
    }

    public void clear(){
        raceBets.clear();
    }

    public Integer getCurrentRaceBet() {
        if (raceBets.isEmpty()) return null;
        else return raceBets.get(raceBetPointer);
    }

    public Integer getCurrentRaceBetButton() {
        if (raceBetButtons.isEmpty()) return null;
        else return raceBetButtons.get(raceBetPointer);
    }

    public boolean isEmpty(){
        return raceBets.isEmpty();
    }

    public void setRaceBetPointer(int pos){
        if (raceBets.isEmpty()) raceBetPointer = 0;
        else if ( (pos > (raceBets.size() - 1)) || (pos < 0) ) raceBetPointer = (raceBets.size() - 1);
        else raceBetPointer = pos;
    }
}

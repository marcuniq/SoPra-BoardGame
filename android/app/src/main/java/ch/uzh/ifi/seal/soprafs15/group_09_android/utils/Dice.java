package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class Dice {
    private ArrayList<Integer> dices = new ArrayList<>();
    private int dicePointer = 0;

    public Dice() { }

    public void add(ArrayList<Integer> someDices) {
        for (Integer dice: someDices){
            dices.add(dice);
        }
    }

    public void add(Integer dice) {
        dices.add(dice);
    }

    public void clear(){
        dices.clear();
    }

    public Integer getCurrentDice() {
        if (dices.isEmpty()) return null;
        else return dices.get(dicePointer);
    }

    public boolean isEmpty(){
        return dices.isEmpty();
    }

    public void setDicePointer(int pos){
        if ( (pos > (dices.size() - 1)) || (pos < 0) ) dicePointer = 0;
        else dicePointer = pos;
    }
}

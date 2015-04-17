package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class GameField {

    private int position;
    private boolean hasOase = false;
    private boolean hasDesert = false;
    private ArrayList<Integer> camels = new ArrayList<>();

    public GameField(int position) {
        setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<Integer> getCamels() {
        return camels;
    }

    public void setCamels(ArrayList<Integer> camelStack) {
        this.camels = camelStack;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOase(boolean hasOase) {
        this.hasOase = hasOase;
    }

    public boolean hasOase(){
        return hasOase;
    }

    public void setDesert(boolean hasDesert) {
        this.hasDesert = hasDesert;
    }

    public boolean hasDesert(){
        return hasOase;
    }

}

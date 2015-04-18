package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class RaceTrackField {

    private int position;
    private Integer oasis;
    private Integer desert;
    private ArrayList<Integer> camels = new ArrayList<>();

    public RaceTrackField(int position) {
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

    public void setOasis(Integer oasis) {
        this.oasis = oasis;
    }

    public Integer getOasis() {
        return oasis;
    }

    public boolean hasOasis(){
        return !(oasis == null);
    }

    public void setDesert(Integer desert) {
        this.desert = desert;
    }

    public boolean hasDesert(){
        return !(desert == null);
    }

    public Integer getDesert() {
        return desert;
    }
}

package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class InteractionTile {
    private Integer oasis;
    private Integer desert;

    public InteractionTile() { }

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

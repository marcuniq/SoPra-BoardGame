package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import java.util.ArrayList;

public class InteractionTile {
    private ArrayList<Integer> tiles = new ArrayList<>();
    private int tilePointer = 0;

    public InteractionTile() { }

    public void add(ArrayList<Integer> someTiles) {
        for (Integer tile: someTiles){
            tiles.add(tile);
        }
    }

    public void add(Integer tile) {
        tiles.add(tile);
    }

    public void clear(){
        tiles.clear();
    }

    public Integer getCurrentTile() {
        if (tiles.isEmpty()) return null;
        else return tiles.get(tilePointer);
    }

    public boolean isEmpty(){
        return tiles.isEmpty();
    }

    public void setTilePointer(int pos){
        if ( (pos > (tiles.size() - 1)) || (pos < 0) ) tilePointer = 0;
        else tilePointer = pos;
    }
}

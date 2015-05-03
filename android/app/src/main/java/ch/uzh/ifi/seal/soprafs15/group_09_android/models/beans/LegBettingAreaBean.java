package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import java.util.List;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.LegBettingTile;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class LegBettingAreaBean implements Parcelable {
    public abstract Long id();
    public abstract List<LegBettingTile> topLegBettingTiles();

    public static LegBettingAreaBean create(Long id, List<LegBettingTile> topLegBettingTiles){
        return new AutoParcel_LegBettingAreaBean(id, topLegBettingTiles);
    }
}


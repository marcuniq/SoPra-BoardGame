package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class LegBettingArea implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract LegBettingTile topLegBettingTile();

    public static LegBettingArea create(Long id, LegBettingTile topLegBettingTile) {
        return new AutoParcel_LegBettingArea(id, topLegBettingTile);
    }
}


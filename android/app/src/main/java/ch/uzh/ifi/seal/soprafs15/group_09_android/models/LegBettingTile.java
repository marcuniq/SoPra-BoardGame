package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class LegBettingTile implements Parcelable {
    public abstract String name();

    public static LegBettingTile create(String name) {
        return new AutoParcel_LegBettingTile(name);
    }
}


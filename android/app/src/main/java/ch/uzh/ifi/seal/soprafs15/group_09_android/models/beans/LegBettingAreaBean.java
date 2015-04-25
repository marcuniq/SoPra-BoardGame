package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class LegBettingAreaBean implements Parcelable {
    public abstract String name();

    public static LegBettingAreaBean create(String name) {
        return new AutoParcel_LegBettingAreaBean(name);
    }
}


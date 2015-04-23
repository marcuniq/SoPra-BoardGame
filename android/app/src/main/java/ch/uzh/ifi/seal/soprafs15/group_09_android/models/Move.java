package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class Move implements Parcelable {
    public abstract String name();

    public static Move create(String name) {
        return new AutoParcel_Move(name);
    }
}


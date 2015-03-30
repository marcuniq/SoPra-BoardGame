package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class RaceTrack implements Parcelable {
    public abstract String name();

    public static RaceTrack create(String name) {
        return new AutoParcel_RaceTrack(name);
    }
}


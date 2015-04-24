package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

import java.util.ArrayList;

@AutoParcel @AutoGson
public abstract class RaceTrack implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract Long gameId();
    @Nullable
    public abstract ArrayList<String> fields(); // TODO: add correct data type; fields?

    public static RaceTrack create(Long id, Long gameId, ArrayList<String> fields) {
        return new AutoParcel_RaceTrack(id, gameId, fields);
    }
}


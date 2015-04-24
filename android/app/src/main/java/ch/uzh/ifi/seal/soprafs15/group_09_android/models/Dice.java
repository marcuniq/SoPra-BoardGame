package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class Dice implements Parcelable {
    @Nullable
    public abstract String color();
    @Nullable
    public abstract Integer faceValue();

    public static Dice create(String color, Integer faceValue) {
        return new AutoParcel_Dice(color, faceValue);
    }
}


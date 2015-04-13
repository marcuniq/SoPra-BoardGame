package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class Token implements Parcelable {
    public abstract String token();

    public static Token create(String token) {
        return new AutoParcel_Token(token);
    }
}


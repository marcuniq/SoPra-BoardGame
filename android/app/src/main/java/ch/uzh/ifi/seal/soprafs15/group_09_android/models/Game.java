package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class Game implements Parcelable {

    // AutoParcel / AutoGson demand abstract dummy methods, getter/setter get autogenerated upon build.
    public abstract Integer id();
    public abstract String game();
    public abstract String owner();
    public abstract String status();

/*    public static Game create(String name) {
        return new AutoParcel_Game(name);
    }*/

}


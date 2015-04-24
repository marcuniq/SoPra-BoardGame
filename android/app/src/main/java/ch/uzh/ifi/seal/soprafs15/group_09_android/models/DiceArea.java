package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class DiceArea implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract RolledDice rolledDice();

    public static DiceArea create(Long id, RolledDice rolledDice) {
        return new AutoParcel_DiceArea(id, rolledDice);
    }
}


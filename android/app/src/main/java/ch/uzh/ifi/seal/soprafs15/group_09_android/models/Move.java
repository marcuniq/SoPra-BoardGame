package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class Move implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract Long userId();
    @Nullable
    public abstract String move();
    @Nullable
    public abstract LegBettingTile legBettingTile();

    public static Move create(Long id, Long userId, String move, LegBettingTile legBettingTile) {
        return new AutoParcel_Move(id, userId, move, legBettingTile);
    }
}


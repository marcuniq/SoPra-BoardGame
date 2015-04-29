package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;

@AutoParcel @AutoGson
public abstract class LegBettingTile implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract GameColors color();
    @Nullable
    public abstract Integer leadingPositionGain();
    @Nullable
    public abstract Integer secondPositionGain();
    @Nullable
    public abstract Integer otherPositionLoss();

    public static LegBettingTile create(Long id, GameColors color, Integer leadingPositionGain, Integer secondPositionGain, Integer otherPositionLoss) {
        return new AutoParcel_LegBettingTile(id, color, leadingPositionGain, secondPositionGain, otherPositionLoss);
    }
}


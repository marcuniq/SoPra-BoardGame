package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.graphics.Color;
import android.os.Parcelable;

import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

import java.util.ArrayList;

@AutoParcel @AutoGson
public abstract class LegBettingTile implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract ArrayList<String> camel();
    @Nullable
    public abstract Integer leadingPositionGain();
    @Nullable
    public abstract Integer secondPositionGain();
    @Nullable
    public abstract Integer otherPositionLoss();
    public static LegBettingTile create(Long id, ArrayList<String> camel, Integer leadingPositionGain, Integer secondPositionGain, Integer otherPositionLoss) {
        return new AutoParcel_LegBettingTile(id, camel, leadingPositionGain, secondPositionGain, otherPositionLoss);
    }
}


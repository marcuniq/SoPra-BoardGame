package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;

import android.support.annotation.Nullable;
import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class RaceBettingArea implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract Integer nrOfWinnerBetting();
    @Nullable
    public abstract Integer nrOfLoserBetting();

    public static RaceBettingArea create(Long id, Integer nrOfWinnerBetting, Integer nrOfLoserBetting) {
        return new AutoParcel_RaceBettingArea(id, nrOfWinnerBetting, nrOfLoserBetting);
    }
}


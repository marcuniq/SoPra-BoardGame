package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.enums.GameColors;

@AutoParcel @AutoGson
public abstract class RaceBettingCard implements Parcelable {
    @Nullable
    public abstract Long id();
    @Nullable
    public abstract Integer nrOfWinnerBetting();
    @Nullable
    public abstract Integer nrOfLoserBetting();
    @Nullable
    public abstract GameColors color();

    public static RaceBettingCard create(Long id, Integer nrOfWinnerBetting, Integer nrOfLoserBetting, GameColors color) {
        return new AutoParcel_RaceBettingCard(id, nrOfWinnerBetting, nrOfLoserBetting, color);
    }
}


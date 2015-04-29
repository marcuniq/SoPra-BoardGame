package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class RaceBettingAreaBean implements Parcelable {
    public abstract Long id();
    public abstract Integer nrOfWinnerBetting();
    public abstract Integer nrOfLoserBetting();

    public static RaceBettingAreaBean create(Long id, Integer nrOfWinnerBetting, Integer nrOfLoserBetting){
        return new AutoParcel_RaceBettingAreaBean(id, nrOfWinnerBetting, nrOfLoserBetting);
    }
}


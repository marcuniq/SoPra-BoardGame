package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import java.util.List;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

@AutoParcel @AutoGson
public abstract class RaceTrackBean implements Parcelable {
    public abstract Long id();
    public abstract Long gameId();
    public abstract List<RaceTrackObjectBean> fields();

    public static RaceTrackBean create(Long id, Long gameId, List<RaceTrackObjectBean> fields){
        return new AutoParcel_RaceTrackBean(id, gameId, fields);
    }
}


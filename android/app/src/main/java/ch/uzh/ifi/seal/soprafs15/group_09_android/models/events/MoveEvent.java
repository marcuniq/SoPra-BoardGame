package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * Created by Marco on 20.04.2015.
 */
@AutoGson @AutoParcel
public abstract class MoveEvent implements Parcelable {

    @Nullable
    public abstract Long moveId();
    @Nullable
    public abstract PushEventNameEnum pushEventNameEnum();

    public static MoveEvent create(Long moveId, PushEventNameEnum pushEventNameEnum){
        return new AutoParcel_MoveEvent(moveId, pushEventNameEnum);
    }
}

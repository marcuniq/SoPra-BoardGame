package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoGson @AutoParcel
public abstract class MoveEvent implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();

    @Nullable
    public abstract Long moveId();

    public static MoveEvent create(PushEventNameEnum pushEventNameEnum, Long moveId){
        return new AutoParcel_MoveEvent(pushEventNameEnum, moveId);
    }
}

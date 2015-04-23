package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco 
 */
@AutoGson @AutoParcel
public abstract class PlayerLeftEvent implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();

    @Nullable
    public abstract Long userId();

    public static PlayerLeftEvent create(PushEventNameEnum pushEventNameEnum, Long userId){
        return new AutoParcel_PlayerLeftEvent(pushEventNameEnum,userId);
    }
}

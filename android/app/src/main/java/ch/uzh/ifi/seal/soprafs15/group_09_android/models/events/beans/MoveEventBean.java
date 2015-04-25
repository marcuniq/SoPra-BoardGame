package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoGson @AutoParcel
public abstract class MoveEventBean implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();

    @Nullable
    public abstract Long moveId();

    public static MoveEventBean create(PushEventNameEnum pushEventNameEnum, Long moveId){
        return new AutoParcel_MoveEventBean(pushEventNameEnum, moveId);
    }
}

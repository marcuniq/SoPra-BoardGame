package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoGson
@AutoParcel
public abstract class PlayerJoinedEventBean implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();
    public abstract Long userId();

    public static PlayerJoinedEventBean create(PushEventNameEnum pushEventNameEnum, Long userId){
        return new AutoParcel_PlayerJoinedEventBean(pushEventNameEnum, userId);
    }
}

package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */

public abstract class AbstractPusherEvent {

    protected PushEventNameEnum pushEventNameEnum;

    public PushEventNameEnum getPushEventNameEnum() {
        return pushEventNameEnum;
    }

    public void setPushEventNameEnum(PushEventNameEnum pushEventNameEnum) {
        this.pushEventNameEnum = pushEventNameEnum;
    }
}

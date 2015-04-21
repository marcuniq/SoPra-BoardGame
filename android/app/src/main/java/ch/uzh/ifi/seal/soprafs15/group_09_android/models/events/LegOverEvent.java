package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */
@AutoGson @AutoParcel
public abstract class LegOverEvent implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();


    public static LegOverEvent create(PushEventNameEnum pushEventNameEnum){
        return new AutoParcel_LegOverEvent(pushEventNameEnum);
    }
}

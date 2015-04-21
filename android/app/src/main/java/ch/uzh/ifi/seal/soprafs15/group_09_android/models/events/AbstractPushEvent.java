package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events;

import android.os.Parcelable;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * Created by Marco on 20.04.2015.
 */
@AutoParcel @AutoGson
public abstract class AbstractPushEvent implements Parcelable {

    public abstract String name();
}

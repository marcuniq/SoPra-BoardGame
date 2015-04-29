package ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.beans;

import android.os.Parcelable;

import java.util.Map;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.events.PushEventNameEnum;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 */

@AutoGson
@AutoParcel
public abstract class PlayerSequenceEventBean implements Parcelable {

    public abstract PushEventNameEnum pushEventNameEnum();

    public abstract Map<Long, Integer> userIdToPlayerIdMap();
}

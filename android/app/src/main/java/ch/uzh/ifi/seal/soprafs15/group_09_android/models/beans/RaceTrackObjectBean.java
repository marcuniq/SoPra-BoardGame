package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;

/**
 * @author Marco
 *
 * Equivalent to GameRaceTrackObjectResponseBean on server, but
 * CamelStack and DesertTile are flattened here
 */
@AutoGson @AutoParcel
public abstract class RaceTrackObjectBean implements Parcelable{

    @Nullable
    public abstract Integer position();

    // CamelStack
    @Nullable
    public abstract List<CamelBean> stack();

    // DeserTile
    @Nullable
    public abstract Boolean isOasis();
    @Nullable
    public abstract Integer playerId();

    public static RaceTrackObjectBean create(Integer position, List<CamelBean> stack){
        return new AutoParcel_RaceTrackObjectBean(position, stack, null, null);
    }
}

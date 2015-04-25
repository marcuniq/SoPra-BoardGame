package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

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

    public abstract Long id();

    // CamelStack
    public abstract List<Color> stack();

    // DeserTile
    public abstract Boolean isOasis();
    public abstract Long playerId();

}

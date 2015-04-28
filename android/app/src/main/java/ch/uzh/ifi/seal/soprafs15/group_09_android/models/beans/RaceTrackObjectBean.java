package ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans;

import android.os.Parcelable;

import java.util.List;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.GameColors;

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
    public abstract List<GameColors> stack();

    // DeserTile
    public abstract Boolean isOasis();
    public abstract Long playerId();

}

package ch.uzh.ifi.seal.soprafs15.group_09_android.models;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

import auto.parcel.AutoParcel;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.gson.AutoGson;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.UserStatus;

@AutoParcel @AutoGson
public abstract class User implements Parcelable {

    // AutoParcel / AutoGson demand abstract dummy methods, getter/setter get autogenerated upon build.
    @Nullable
    public abstract Long id();
    public abstract String username();
    public abstract Integer age();
    @Nullable
    public abstract String token();
    @Nullable
    public abstract UserStatus status();
    @Nullable
    public abstract Game game();
    @Nullable
    public abstract List<Move> moves();
    @Nullable
    public abstract Integer money();
    @Nullable
    public abstract List<RaceBettingCard> raceBettingCards();
    @Nullable
    public abstract List<LegBettingTile> legBettingTiles();


    public static User create( String username,
                               Integer age) {
        return new AutoParcel_User( null, username, age, null,
                                    null, null, null, null,
                                    null, null);
    }

    public static User update( Long id,
                               String username,
                               Integer age,
                               String token,
                               UserStatus status,
                               Game game,
                               List<Move> moves,
                               Integer money,
                               List<RaceBettingCard> raceBettingCards,
                               List<LegBettingTile> legBettingTiles ) {
        return new AutoParcel_User( id, username, age, token,
                status, game, moves, money,
                raceBettingCards, legBettingTiles);
    }

}


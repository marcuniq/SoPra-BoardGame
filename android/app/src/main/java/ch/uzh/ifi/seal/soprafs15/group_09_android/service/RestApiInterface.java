package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.MoveBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.DiceAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.LegBettingAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceBettingAreaBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.RaceTrackBean;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface RestApiInterface {

    /**
     * Returns all users
     *
     * @param callback
     */
    @GET("/users")
    void getUsers(Callback<List<UserBean>> callback);

    /**
     * Register a new user on the server
     * @User user: UserBean to register
     * @param callback
     */
    @POST("/users")
    void createUser(@Body UserBean user, Callback<UserBean> callback);

    /**
     * Returns all games
     * @param callback
     */
    @GET("/games")
    void getGames(Callback<List<GameBean>> callback);

    /**
     * Returns all players
     * @param gameId
     * @param callback
     * @help  http://stackoverflow.com/questions/20382253/dynamically-add-optional-parameters-to-api-requests
     */
    @GET("/games/{gameId}/players")
    void getPlayers(@Path("gameId") Long gameId, Callback<List<UserBean>> callback);

    /**
     * Returns all moves
     * @param gameId
     * @param callback
     */
    @GET("/games/{gameId}/moves")
    void getGameMoves(@Path("gameId") Long gameId, Callback<List<MoveBean>> callback);

    /**
     * Returns a specific move
     * @param gameId
     * @param moveId
     * @param callback
     */
    @GET("/games/{gameId}/moves/{moveId}")
    void getGameMove(@Path("gameId") Long gameId, @Path("moveId") Long moveId, Callback<List<MoveBean>> callback);


    @POST("/games/{gameId}/moves")
    void initiateGameMove(@Path("gameId") Long gameId, @Body MoveBean move, Callback<MoveBean> callback);


    /**
     * Creates a new game
     * @Game game: The new game
     * @param callback
     */
    @POST("/games")
    void createGame(@Body GameBean game, Callback<GameBean> callback);

    /**
     * Logins a user
     * @param userId the id (Long) of the user
     * @param callback
     */
    @POST("/users/{userId}/login")
    void loginUser(@Path("userId") Long userId, Callback<UserBean> callback);

    /**
     *
     * @param gameId
     * @param token
     * @param callback
     */
    @POST("/games/{gameId}/players")
    void joinGame(@Path("gameId") Long gameId, @Body UserBean token, Callback<UserBean> callback);

    /**
     *
     * @param gameId
     * @param callback
     */
    @GET("/games/{gameId}/racetrack")
    void getRacetrack(@Path("gameId") Long gameId, Callback<RaceTrackBean> callback);

    /**
     *
     * @param gameId
     * @param callback
     */
    @GET("/games/{gameId}/legbettingarea")
    void getLegBettingArea(@Path("gameId") Long gameId, Callback<LegBettingAreaBean> callback);

    /**
     *
     * @param gameId
     * @param callback
     */
    @GET("/games/{gameId}/racebettingarea")
    void getRaceBettingArea(@Path("gameId") Long gameId, Callback<RaceBettingAreaBean> callback);

    /**
     *
     * @param gameId
     * @param callback
     */
    @GET("/games/{gameId}/dicearea")
    void getDiceArea(@Path("gameId") Long gameId, Callback<DiceAreaBean> callback);

    /**
     *
     * @param gameId
     * @param playerId
     * @param callback
     */
    @GET("/games/{gameId}/players/{playerId}")
    void getGamePlayer(@Path("gameId") Long gameId, @Path("playerId") Integer playerId, Callback<UserBean> callback);

    /**
     *
     * @param gameId
     */
    @POST("/games/{gameId}/start-fast-mode")
    void startFastMode(@Path("gameId") Long gameId, @Body UserBean token, Callback<GameBean> callback);

    /**
     *
     * @param gameId
     */
    @POST("/games/{gameId}/start")
    void start(@Path("gameId") Long gameId, @Body UserBean token, Callback<GameBean> callback);

    /* TODO: java.lang.IllegalArgumentException: RestApiInterface.removeGamePlayer: Non-body HTTP method cannot contain @Body or @TypedOutput.
     * man kann keinen Body mitschicken wenn man was löschen will? */
    @DELETE("/games/{gameId}/players/{playerId}")
    void removeGamePlayer(@Path("gameId") Long gameId, @Path("playerId") Integer playerId, @Body UserBean token, Callback<UserBean> callback);

    @DELETE("/games/{gameId}")
    void removeGame(@Path("gameId") Long gameId, @Body UserBean token, Callback<GameBean> callback);

}

package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

import java.util.List;

public interface RestApiInterface {

    /**
     * Returns all users
     *
     * @param callback
     */
    @GET("/users")
    void getUsers(Callback<List<User>> callback);

    /**
     * Register a new user on the server
     * @User user: User to register
     * @param callback
     */
    @POST("/users")
    void createUser(@Body User user, Callback<User> callback);

    /**
     * Returns all games
     * @param callback
     */
    @GET("/games")
    void getGames(Callback<List<Game>> callback);

    /**
     * Creates a new game
     * @Game game: The new game
     * @param callback
     */
    @POST("/games")
    void createGame(@Body Game game, Callback<Game> callback);

}

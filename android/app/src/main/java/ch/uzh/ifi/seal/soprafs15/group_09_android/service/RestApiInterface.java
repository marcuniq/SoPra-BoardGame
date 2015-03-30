package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestApiInterface {

    /**
     * Returns all users
     *
     * @param cb
     */
    @GET("/users")
    void getUsers(Callback<List<User>> cb);

    /**
     * Register a new user on the server
     * @User user: User to register
     * @param cb
     */
    @POST("/users")
    void createUser(@Body User user, Callback<User> cb);

    /**
     * Returns all games
     * @param cb
     */
    @GET("/games")
    void getGames(Callback<List<Game>> cb);

    /**
     * Creates a new game
     * @Game game: The new game
     * @param cb
     */
    @POST("/games")
    void createGame(@Body Game game, Callback<Game> cb);

}

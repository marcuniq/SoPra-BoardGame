package ch.uzh.ifi.seal.soprafs15.group_09_android.service;

import java.util.List;

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
    void createUser(@Body User user, Callback<RestUri> cb);


}

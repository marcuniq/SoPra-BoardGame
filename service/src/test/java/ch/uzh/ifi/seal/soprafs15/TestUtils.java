package ch.uzh.ifi.seal.soprafs15;

import ch.uzh.ifi.seal.soprafs15.controller.beans.user.UserRequestBean;

/**
 * Created by Cyrus on 17.04.2015.
 */
public class TestUtils {

    public static UserRequestBean createUserRequestBean(int age, String username) {

        // Set up UserRequestBean Object
        UserRequestBean userRequest = new UserRequestBean();
        userRequest.setAge(43);
        userRequest.setUsername("mm");

        return userRequest;
    }




}

package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;

/**
 *  Uses the GenericArrayAdapter to be able to add any Object to our custom ArrayAdapter. Now we
 *  can use user.add(User object) to add a item to the ArrayList and thus to the ListView which is
 *  somehow practical because we can define in the adapter itself what will be displayed in the view
 *  and the Fragments using the adapter is only responsible for getting the correct data.
 */
public class UserArrayAdapter extends GenericArrayAdapter<User> {

    public UserArrayAdapter(Context context, int resource, int textViewResourceId, ArrayList<User> user) {
        super(context, resource, textViewResourceId, user);
    }

    /**
     * Will display the username of User object in the ListView
     *
     * @param textView
     * @param user
     */
    @Override public void drawText(TextView textView, User user) {
        textView.setText(user.username());
    }

}
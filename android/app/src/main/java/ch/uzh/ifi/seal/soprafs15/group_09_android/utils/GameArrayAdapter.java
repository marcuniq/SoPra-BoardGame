package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;

/**
 *  Uses the GenericArrayAdapter to be able to add any Object to our custom ArrayAdapter. Now we
 *  can use game.add(Game object) to add a item to the ArrayList and thus to the ListView which is
 *  somehow practical because we can define in the adapter itself what will be displayed in the view
 *  and the Fragments using the adapter is only responsible for getting the correct data.
 */
public class GameArrayAdapter extends GenericArrayAdapter<Game> {

    public GameArrayAdapter(Context context, ArrayList<Game> game) {
        super(context, game);
    }

    /**
     * Will display the game name of Game object in the ListView
     *
     * @param textView
     * @param game
     */
    @Override public void drawText(TextView textView, Game game) {
        textView.setText(game.name());
    }

}
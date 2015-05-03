package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.GameBean;

/**
 *  Uses the GenericArrayAdapter to be able to add any Object to our custom ArrayAdapter. Now we
 *  can use game.add(GameBean object) to add a item to the ArrayList and thus to the ListView which is
 *  somehow practical because we can define in the adapter itself what will be displayed in the view
 *  and the Fragments using the adapter is only responsible for getting the correct data.
 */
public class GameArrayAdapter extends GenericArrayAdapter<GameBean> {

    public GameArrayAdapter(Context context,  int resource, int textResourceId, int textDescriptionResourceId, int imageResourceId, ArrayList<GameBean> game) {
        super(context, resource, textResourceId, textDescriptionResourceId, imageResourceId, game);
    }

    /**
     * Will display the game name of GameBean object in the ListView
     *
     * @param textView
     * @param game
     */
    @Override
    public void setText(TextView textView, GameBean game) {
        textView.setText(game.name());
    }

    @Override
    public void setTextDescription(TextView textView, GameBean game) {
        textView.setText("Owner: " + game.owner());
    }

    public void setIcon(ImageView imageView, GameBean game, int index){
        imageView.setBackgroundResource(R.drawable.ic_launcher);
    }
}
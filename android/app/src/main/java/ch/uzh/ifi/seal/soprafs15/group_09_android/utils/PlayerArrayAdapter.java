package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;

/**
 *  Uses the GenericArrayAdapter to be able to add any Object to our custom ArrayAdapter. Now we
 *  can use user.add(User object) to add a item to the ArrayList and thus to the ListView which is
 *  somehow practical because we can define in the adapter itself what will be displayed in the view
 *  and the Fragments using the adapter is only responsible for getting the correct data.
 */
public class PlayerArrayAdapter extends GenericArrayAdapter<User> {

    private ArrayList<Integer> icons = new ArrayList<>();
    private int position = 0;

    public PlayerArrayAdapter(Context context, int resource, int textResourceId, int imageResourceId, ArrayList<User> player) {
        super(context, resource, textResourceId, imageResourceId,player);

        icons.add(R.drawable.c1_head);
        icons.add(R.drawable.c2_head);
        icons.add(R.drawable.c3_head);
        icons.add(R.drawable.c4_head);
        icons.add(R.drawable.c5_head);
        icons.add(R.drawable.c6_head);
        icons.add(R.drawable.c7_head);
        icons.add(R.drawable.c8_head);
    }

    /**
     * Will display the username of User object in the ListView
     *
     * @param textView
     * @param player
     */
    @Override
    public void drawText(TextView textView, User player) {
        textView.setText(player.username());
    }

    public void setIcon(ImageView imageView, User player){
        if (position > 7) position = 0;
        imageView.setBackgroundResource(icons.get(position));
        position++;
    }

}
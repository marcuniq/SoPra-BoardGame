package ch.uzh.ifi.seal.soprafs15.group_09_android.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;

/**
 *  Uses the GenericArrayAdapter to be able to add any Object to our custom ArrayAdapter. Now we
 *  can use user.add(UserBean object) to add a item to the ArrayList and thus to the ListView which is
 *  somehow practical because we can define in the adapter itself what will be displayed in the view
 *  and the Fragments using the adapter is only responsible for getting the correct data.
 */
public class PlayerArrayAdapter extends GenericArrayAdapter<UserBean> {

    private boolean playerIdIsUserId = false;

    public PlayerArrayAdapter(Context context, int resource, int textResourceId, int textDescriptionResourceId, int imageResourceId, ArrayList<UserBean> player, boolean playerIdIsUserId) {
        super(context, resource, textResourceId, textDescriptionResourceId, imageResourceId, player);
        this.playerIdIsUserId = playerIdIsUserId;
    }

    /**
     * Will display the username of UserBean object in the ListView
     *
     * @param textView
     * @param player
     */
    @Override
    public void setText(TextView textView, UserBean player) {
        textView.setText(player.username());
    }

    @Override
    public void setTextDescription(TextView textView, UserBean player) {
        if (player.money() == null) textView.setText("");
        else if (player.money() == 1) textView.setText(player.money() + " coin");
        else textView.setText(player.money() + " coins");
    }

    @Override
    public void setIcon(ImageView imageView, UserBean player, int index){
        if (playerIdIsUserId) index = player.id().intValue();
        imageView.setBackgroundResource(getContext().getResources().getIdentifier("c" + index + "_head", "drawable", getContext().getPackageName()));
    }
}
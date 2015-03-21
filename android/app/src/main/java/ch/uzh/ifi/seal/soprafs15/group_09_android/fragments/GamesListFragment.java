package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/* NEED TO CHANGE:
* extends: ListFragments to Fragments
* ListFragments does not include any view and is set only for testing purposes*/
public class GamesListFragment extends ListFragment {

    /* empty constructor */
    public GamesListFragment() {}

    /* Creates a list of devices as sample data
    * needs to be changed:
    * get all the available games and list them */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] values = new String[] { "Android",    "iPhone",   "WindowsMobile",
                                         "Blackberry", "WebOS",    "Ubuntu",
                                         "Windows7",   "Max OS X", "Linux" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    /* Implement some behaviour when clicking on an item
    * in future: should open some detailed view of a game
    * including: the users that have already joined that game and a "join game" button
    * where the user can join that specific game if he wants to */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
}



package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

//public class GameLobbyFragment extends ListFragment {
public class GameLobbyFragment extends Fragment {

    private TextView tvLogBox;
    private ArrayAdapter<String> arrayAdapter; // adapts the ArrayList of Games to the ListView
    private Long gameId;
    private Button startGameButton;

    /* empty constructor */
    public GameLobbyFragment() {}

    /**
     * Called after User has successfully logged in.
     * @return A new instance of fragment GamesListFragment.
     */
    public static GameLobbyFragment newInstance() {
        return new GameLobbyFragment();
    }

    /**
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameId = this.getArguments().getLong("gameId");
    }

    /**
     * Creates a new view, instantiates a new ArrayAdapter that 'links' the ArrayList<String>
     * to the ListView which is then displayed.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lobby, container, false);

        /*arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.player_item,
                  new ArrayList<String>());
        setListAdapter(arrayAdapter);*/

        startGameButton = (Button) v.findViewById(R.id.startButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStarteGameBtn(v);
            }
        });


        return v;
    }

    /**
     * Creates a Callback to get the list of all current available games. Adds all games to the
     * adapter, respectively to the list which is later automatically displayed in the view.
     */
    @Override
    public void onResume(){
        super.onResume();

       /* RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<User>>() {
            @Override
            public void success(List<User> players, Response response) {
                for (User player : players) {
                    arrayAdapter.add(player.username());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });*/
    }

    /**
     * Implements some behaviour when clicking on an item.
     *
     * @param l         The list view.
     * @param v         The current view.
     * @param position  Current position of the item in the view.
     * @param id        Id of the item from the list.
     */
    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        // For now just display what item has been selected
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(v.getContext(), "You selected player \"" + item + "\"", Toast.LENGTH_LONG).show();
    }*/

    private void onClickStarteGameBtn(View v) {
        // TODO: start game
        Toast.makeText(v.getContext(), "You started game \"" + gameId + "\"", Toast.LENGTH_LONG).show();
    }
}



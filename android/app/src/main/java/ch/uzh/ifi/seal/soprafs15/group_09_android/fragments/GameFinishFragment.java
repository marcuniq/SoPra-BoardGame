package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.support.v4.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import ch.uzh.ifi.seal.soprafs15.group_09_android.utils.PlayerArrayAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GameFinishFragment extends ListFragment {

    private Long gameId;
    private boolean isOwner = false;
    private Long userId;
    private PlayerArrayAdapter playerArrayAdapter; // adapts the ArrayList of Games to the ListView

    public GameFinishFragment() {}

    public static GameFinishFragment newInstance() {
        return new GameFinishFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getActivity().getIntent().getExtras();
        gameId = b.getLong("gameId");
        userId = b.getLong("userId");
        isOwner = b.getBoolean("isOwner");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_finish, container, false);

        playerArrayAdapter = new PlayerArrayAdapter(
                getActivity(),
                R.layout.player_item,
                R.id.player_item_text,
                R.id.player_item_description,
                R.id.player_item_icon,
                new ArrayList<User>());
        setListAdapter(playerArrayAdapter);

        Button closeButton = (Button) v.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GameActivity)getActivity()).removePlayerFromGame();
                if (isOwner) ((GameActivity)getActivity()).removeGame();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String token = "";
                token = sharedPref.getString("token", token);

                Intent intent = new Intent();
                intent.setClass(getActivity(), MenuActivity.class);
                Bundle b = new Bundle();
                b.putLong("userId", userId);
                b.putString("token", token);
                intent.putExtras(b);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        getPlayers();
    }

    private void getPlayers(){
        RestService.getInstance(getActivity()).getPlayers(gameId, new Callback<List<User>>() {
            @Override
            public void success(List<User> newPlayers, Response response) {
                playerArrayAdapter.clear();
                Collections.sort(newPlayers, new Comparator<User>() {
                    @Override
                    public int compare(User user1, User user2) {
                        return user1.money().compareTo(user2.money());
                    }
                });
                setListAdapter(playerArrayAdapter);
                for (User player : newPlayers) {
                    playerArrayAdapter.add(player);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MainActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.Game;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.google.android.gms.plus.PlusOneButton;

/**
 * A fragment with a Google +1 button.
 */
public class MainMenuFragment extends Fragment {

    private Button createGameMenuButton;
    private Button listGamesMenuButton;


    public MainMenuFragment() {
        // Required empty public constructor
    }

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        createGameMenuButton = (Button) v.findViewById(R.id.createGameMenuButton);
        createGameMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateGameMenuButton(v);
            }
        });
        listGamesMenuButton = (Button) v.findViewById(R.id.listGamesMenuButton);
        listGamesMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListGamesMenuButton(v);
            }
        });


        return v;
    }

    private void onClickCreateGameMenuButton(View v) {
        ((MainActivity)getActivity()).pushFragment(CreateGameFragment.newInstance());
    }

    private void onClickListGamesMenuButton(View v) {
        ((MainActivity)getActivity()).pushFragment(GamesListFragment.newInstance());
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
        // createGameButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }
}

package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;

/**
 * This Fragment displays the Main Menu
 * Including Buttons for the navigation
 */
public class MainMenuFragment extends Fragment {

    private Button createGameMenuButton;
    private Button listGameMenuButton;
    private String token;

    public MainMenuFragment() {}

    public static MainMenuFragment newInstance() {
        return new MainMenuFragment();
    }

    /**
     * Displays two buttons for navigation:
     * - createGameMenuButton: switches to the CreateGameFrame
     * - listGameMenuButton:  switches to the GamesListFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        token = sharedPref.getString("token", token);

        Log.v("MainMenuFragment/Token", " = " + token);

        /* TODO we need !urgent! some consistency in naming the id's !!! */
        createGameMenuButton = (Button) v.findViewById(R.id.createGameMenuButton);
        createGameMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateGameMenuButton(v);
            }
        });
        listGameMenuButton = (Button) v.findViewById(R.id.listGameMenuButton);
        listGameMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListGameMenuButton(v);
            }
        });

        return v;
    }

    /**
     * switches View to the CreateGameFrame
     *
     * @param v the current View
     */
    private void onClickCreateGameMenuButton(View v) {
        ((MenuActivity)getActivity()).pushFragment(GameCreatorFragment.newInstance());
    }

    /**
     * switches View to the GameListFragment
     *
     * @param v the current View
     */
    private void onClickListGameMenuButton(View v) {
        ((MenuActivity)getActivity()).pushFragment(GameListFragment.newInstance());
    }
}

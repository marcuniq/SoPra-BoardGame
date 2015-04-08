package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;

/**
 * This Fragment displays the Main Menu
 * Including Buttons for the navigation
 */
public class MainMenuFragment extends Fragment {

    private Button createGameMenuButton;
    private Button listGamesMenuButton;
    private User user;

    public MainMenuFragment() {}

    public static MainMenuFragment newInstance(User user) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("user");
    }

    /**
     * Displays two buttons for navigation:
     * - createGameMenuButton: switches to the CreateGameFrame
     * - listGamesMenuButton:  switches to the GamesListFragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);

        /* TODO we need !urgent! some consistency in naming the id's !!! */
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

    /**
     * switches View to the CreateGameFrame
     *
     * @param v the current View
     */
    private void onClickCreateGameMenuButton(View v) {
        ((MenuActivity)getActivity()).pushFragment(GameCreatorFragment.newInstance());
    }

    /**
     * switches View to the GamesListFragment
     *
     * @param v the current View
     */
    private void onClickListGamesMenuButton(View v) {
        ((MenuActivity)getActivity()).pushFragment(GameListFragment.newInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}

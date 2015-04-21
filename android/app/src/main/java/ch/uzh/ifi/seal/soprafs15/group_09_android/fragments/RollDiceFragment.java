package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.GameActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;

public class RollDiceFragment extends Fragment {

    public RollDiceFragment() {}

    public static RollDiceFragment newInstance() {
        return new RollDiceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_roll_dice, container, false);


        Button acceptButton = (Button) v.findViewById(R.id.accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateGameMenuButton(v);
            }
        });
        Button rejectButton = (Button) v.findViewById(R.id.reject);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListGameMenuButton(v);
            }
        });

        return v;
    }

    private void onClickCreateGameMenuButton(View v) {
        ((GameActivity)getActivity()).pushFragment(GameFragment.newInstance());
    }

    private void onClickListGameMenuButton(View v) {
        ((GameActivity)getActivity()).pushFragment(GameFragment.newInstance());
    }
}

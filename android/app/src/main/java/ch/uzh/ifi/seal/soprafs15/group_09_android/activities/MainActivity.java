package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.fragments.GamesListFragment;
import ch.uzh.ifi.seal.soprafs15.group_09_android.fragments.LoginFragment;

public class MainActivity extends FragmentActivity implements LoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(LoginFragment.newInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void setFragment(Fragment fragment) {

        /* FOR TESTING PURPOSES
        * start the GamesListFragment instead of the login fragment
        *
        * QUESTION:
        * How can I close the LoginFragment when the user has sucessfully logged in
        * and created an account?
        * Maybe this isn't the right place? */
        GamesListFragment gamesListFragment = new GamesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, gamesListFragment).commit();

// ORIGINAL:
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    protected void pushFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Empty Implementation
    }
}

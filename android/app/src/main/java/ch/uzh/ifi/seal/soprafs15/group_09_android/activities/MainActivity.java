package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
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

    public void setFragment(Fragment fragment) {
        /* setFragment() replaces the currently active Fragment (and thus, "closes" it in a way.
         * As soon as it isn't attached to the hierarchy anymore it will get garbage-collected) */
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    protected void pushFragment(Fragment fragment) {
        /* pushFragment() puts a new Fragment on top of the existing one (the back-button can be
         * used to navigate back through the fragments that are underneath) */
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Empty Implementation
    }
}

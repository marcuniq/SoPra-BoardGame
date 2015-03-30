package ch.uzh.ifi.seal.soprafs15.group_09_android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.fragments.LoginFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(LoginFragment.newInstance());
    }

    /**
     * getMenuInflater() Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * replaces the currently active Fragment (and thus, "closes" it in a way.
     * As soon as it isn't attached to the hierarchy anymore it will get garbage-collected)
     *
     * @param fragment the fragment to be set
     */
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    /**
     * puts a new Fragment on top of the existing one (the back-button can be
     * used to navigate back through the fragments that are underneath)
     *
     * @param fragment the fragment to be pushed
     */
    public void pushFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment);
        transaction.addToBackStack(null).commit();
    }
}

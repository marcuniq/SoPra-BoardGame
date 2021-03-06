package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.beans.UserBean;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginFragment extends Fragment {

    private EditText etAge;
    private EditText etUsername;
    private TextView tvLogBox;
    private Button loginButton;
    private Long userId;
    private String token = "you fool";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    public LoginFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void onClickCreateUserBtn(final View v) {
        final String username = etUsername.getText().toString();
        Integer age = Integer.parseInt(etAge.getText().toString());

        UserBean user = UserBean.create(username,              // username
                age);                  // age

        RestService.getInstance(getActivity()).createUser(user, new Callback<UserBean>() {
            @Override
            public void success(UserBean user, Response response) {
                try {
                    loginUser(user, v); // get the token
                }
                catch (NullPointerException e) {
                    Log.e("UserCreate", "Null pointer exception in LoginFragment");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Create User Failed :" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginUser(UserBean user, final View v){
        userId = user.id();

        RestService.getInstance(getActivity()).loginUser(userId, new Callback<UserBean>() {
            @Override
            public void success(UserBean user, Response response) {
                if (user == null) {
                    throw new NullPointerException("UserBean is null in LoginFragment.");
                }

                token = user.token();

                /* Show the token
                Toast.makeText(v.getContext(), "Token = \"" + token + "\"", Toast.LENGTH_LONG).show();
                */

                /* Start new Activity LobbyActivity and close current Fragment */
                Intent intent = new Intent();
                intent.setClass(getActivity(), MenuActivity.class);
                Bundle b = new Bundle();
                b.putLong("userId", userId);
                b.putString("token", token);
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Login User Failed :" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = (EditText) v.findViewById(R.id.username);
        etAge = (EditText) v.findViewById(R.id.age);
        tvLogBox = (TextView) v.findViewById(R.id.logBox);

        loginButton = (Button) v.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateUserBtn(v);
            }
        });

        return v;
    }

}

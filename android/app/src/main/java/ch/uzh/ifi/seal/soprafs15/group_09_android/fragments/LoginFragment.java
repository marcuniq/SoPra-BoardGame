package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.AlertDialog;
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

import com.google.gson.Gson;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginFragment extends Fragment {

    private EditText etAge;
    private EditText etUsername;
    private TextView tvLogBox;
    private Button btnLogin;

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

    private void onClickCreateUserBtn(View v) {
        final String username = etUsername.getText().toString();
        Integer age = Integer.parseInt(etAge.getText().toString());

        User user = User.create( username,              // username
                                 age);                  // age

        RestService.getInstance(getActivity()).createUser(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
//                AlertDialog dialog = userCreatedSuccessfullyAlert();
//                dialog.show();

                if (user == null){
                    Log.v("UserCreate", "Creation Failed. NULL Object returned.");
                    Log.v("UserCreate", new Gson().toJson(user));
                } else {

                /* Start new Activity LobbyActivity and close current Fragment */
                Intent intent = new Intent();
                intent.setClass(getActivity(), MenuActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
    }

    /**
     * Create small pop up dialog box
     * @return AlertDialog builder.create()
     */
    private AlertDialog userCreatedSuccessfullyAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("User created successfully!").setTitle("User created");
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        etUsername = (EditText) v.findViewById(R.id.username);
        etAge = (EditText) v.findViewById(R.id.age);
        tvLogBox = (TextView) v.findViewById(R.id.logBox);

        btnLogin = (Button) v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateUserBtn(v);
            }
        });

        return v;
    }

}

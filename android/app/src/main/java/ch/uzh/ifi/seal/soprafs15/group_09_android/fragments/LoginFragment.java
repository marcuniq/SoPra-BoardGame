package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MenuActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
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
        LoginFragment fragment = new LoginFragment();

        /*
        * To pass objects and parameters to fragments, use the Bundle-Class
        *
        * Bundle args = new Bundle();
        * args.putString(ARG_PARAM1, param1);
        * args.putString(ARG_PARAM2, param2);
        * fragment.setArguments(args);
        */

        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        *   To retrieve passed arguments to a fragment:
        *
        *   if (getArguments() != null) {
        *       mParam1 = getArguments().getString(ARG_PARAM1);
        *       mParam2 = getArguments().getString(ARG_PARAM2);
        *   }
        */

    }

    private void onClickCreateUserBtn(View v) {
        final String username = etUsername.getText().toString();
        Integer age = Integer.parseInt(etAge.getText().toString());

        User user = User.create(username, age);

        RestService.getInstance(getActivity()).createUser(user, new Callback<RestUri>() {
            @Override
            public void success(RestUri restUri, Response response) {
                //tvLogBox.setText("SUCCESS: User generated at: " + restUri.uri());

                /* Start new Activity LobbyActivity and close current Fragment */
                Intent intent = new Intent();
                intent.setClass(getActivity(), MenuActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                tvLogBox.setText("ERROR: " + error.getMessage());
            }
        });
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

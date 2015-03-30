package ch.uzh.ifi.seal.soprafs15.group_09_android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.uzh.ifi.seal.soprafs15.group_09_android.R;
import ch.uzh.ifi.seal.soprafs15.group_09_android.activities.MainActivity;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.RestUri;
import ch.uzh.ifi.seal.soprafs15.group_09_android.models.User;
import ch.uzh.ifi.seal.soprafs15.group_09_android.service.RestService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private EditText etAge;
    private EditText etUsername;
    private TextView tvLogBox;
    private Button btnLogin;

    private OnFragmentInteractionListener mListener;

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
                AlertDialog dialog = userCreatedSuccessfullyAlert();
                dialog.show();

                /* As you don't want the user to be able to login again if he did successfully,
                 * setFragment() might be the right choice here */
                ((MainActivity)getActivity()).setFragment(MainMenuFragment.newInstance());
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
        builder.setMessage("User created successfully!")
            .setTitle("User created");
        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}

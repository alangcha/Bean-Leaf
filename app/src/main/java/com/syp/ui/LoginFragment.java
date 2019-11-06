package com.syp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.syp.model.Database;
import com.syp.model.User;

import com.syp.MainActivity;
import com.syp.R;

import java.util.Optional;

public class LoginFragment extends Fragment {

    private MainActivity mainActivity;
    private EditText username;
    private EditText password;
    private TextView error;
    private Button signInButton;
    private Button registerButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Get Data
        this.mainActivity = (MainActivity) getActivity();
        this.username = v.findViewById(R.id.emailSignIn);
        this.password = v.findViewById(R.id.passwordSignIn);
        this.signInButton = v.findViewById(R.id.signInButton);
        this.registerButton = v.findViewById(R.id.registerButton);
        this.error = v.findViewById(R.id.loginInvalid);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get strings from input
                String usernameString = username.getText().toString();
                String passwordString = password.getText().toString();

                // Invalid pools
                boolean username_invalid = false;
                boolean password_invalid = false;
                boolean query_invalid = false;

                // Check empty for username strings
                if(usernameString.trim().length()==0)
                    username_invalid = true;
                if(passwordString.trim().length()==0)
                    password_invalid = true;

                // Query database for username
                Optional<User> u = Database.checkExistingUser(usernameString);

                // If exists :
                // Set user in main activity
                // Else :
                // Set query invalid bool
                if(!u.isPresent())
                    query_invalid = true;
//                else
//                    mainActivity.setUser(u.get());

                // If anything is invalid :
                // Make error visible
                // Repopulate valid data
                if(username_invalid || password_invalid || query_invalid){
                    if(!username_invalid)
                        username.setText(usernameString);
                    if(!password_invalid)
                        password.setText(passwordString);
                    error.setVisibility(View.VISIBLE);
                    return;
                }

                // If all pass navigate to Map Page
//                NavDirections action = LoginFragmentDirections.actionLoginFragmentToMapFragment();
//                Navigation.findNavController(view).navigate(action);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                // Navigate to register page
//                NavDirections action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment();
//                Navigation.findNavController(view).navigate(action);
            }
        });

        return v;
    }
}

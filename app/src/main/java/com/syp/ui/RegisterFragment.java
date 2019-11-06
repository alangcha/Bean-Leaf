package com.syp.ui;

import android.os.Bundle;

import com.syp.model.Database;
import com.syp.model.User;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.regex.Pattern;

import com.syp.MainActivity;
import com.syp.R;

public class RegisterFragment extends Fragment {

    private MainActivity mainActivity;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private TextView error;
    private Button createAccount;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        // Connect views to variables
        firstName = v.findViewById(R.id.registerFirstName);
        lastName = v.findViewById(R.id.registerLastName);
        email = v.findViewById(R.id.registerEmail);
        password = v.findViewById(R.id.registerPassword);
        createAccount = v.findViewById(R.id.registerCreateAccountButton);
        error = v.findViewById(R.id.registerError);
        mainActivity = (MainActivity) getActivity();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get data from user inputs
                String firstNameString = firstName.getText().toString();
                String lastNameString = lastName.getText().toString();
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                String errorStr = "";

                // Create invalid booleans against empty, format, and exists criteria
                boolean firstNameInvalid = firstNameString.trim().length()==0;
                boolean firstNameContainsInvalid = !onlyLetters(firstNameString);
                boolean lastNameInvalid = lastNameString.trim().length()==0;
                boolean lastNameContainsInvalid = !onlyLetters(lastNameString);
                boolean emailInvalid = !isEmailFormat(emailString);
                boolean passwordInvalid = passwordString.trim().length()==0;
                boolean email_exists = Database.checkExistingUser(emailString).isPresent();

                // Create invalid bool representing if any are invalid
                boolean invalid = firstNameInvalid | lastNameInvalid | emailInvalid | passwordInvalid | email_exists;

                // if invalid bool is true :
                // Add error to error string
                // if invalid bool is false:
                // Repopulate data in page with valid data

                if(!firstNameInvalid && !firstNameContainsInvalid) firstName.setText(firstNameString);
                else errorStr += "Invalid First Name \n\n";

                if(!lastNameInvalid && !lastNameContainsInvalid) lastName.setText(lastNameString);
                else errorStr += "Invalid Last Name \n\n";

                if(!emailInvalid) firstName.setText(emailString);
                else errorStr += "Invalid Email\n\n";

                if(!email_exists || !emailInvalid) firstName.setText(emailString);
                else errorStr += "Email already exists\n\n";

                if(!passwordInvalid) firstName.setText(passwordString);
                else errorStr += "Password Invalid \n\n";

                // If invalid set error text & make error visible
                if(invalid){
                    error.setText(errorStr);
                    error.setVisibility(View.VISIBLE);
                    return;
                }

                // If all pass create new user
                User u = new User();
//                u.setFirstName(firstName.getText().toString());
//                u.setLastName(lastName.getText().toString());
//                u.set_email(email.getText().toString());
//                u.set_password(email.getText().toString());

                // Add user to main activity
                //mainActivity.addUser(u);

                // Redirect to maps page is all pass
//                NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToMapFragment();
//                Navigation.findNavController(view).navigate(action);
            }
        });

        return v;
    }

    private boolean onlyLetters(String s){
        char[] chars = s.toCharArray();
        for(char c: chars){
            if(!Character.isLetter(c) && c != '-' && c != ' ')
                return false;
        }
        return true;
    }

    private boolean isEmailFormat(String s){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (s == null)
            return false;
        return pat.matcher(s).matches();
    }
}

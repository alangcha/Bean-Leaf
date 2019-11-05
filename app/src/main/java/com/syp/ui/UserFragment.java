package com.syp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.syp.MainActivity;
import com.syp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.regex.Pattern;

// hardcoded data:

public class UserFragment extends Fragment {

    private MainActivity mainActivity;
    private TextView greetingsLabel;
    private TextView email;
    private TextView password;
    private TextView errorEmail;
    private TextView errorPassword;
    private EditText email_edit;
    private EditText password_edit;
    private Button addShop;
    private FloatingActionButton edit;
    private FloatingActionButton done;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        greetingsLabel = v.findViewById(R.id.profile_greetings);
        email = v.findViewById(R.id.profile_email);
        email_edit = v.findViewById(R.id.profile_email_edit);
        errorEmail = v.findViewById(R.id.profile_invalid_email);
        password = v.findViewById(R.id.profile_password);
        password_edit = v.findViewById(R.id.profile_password_edit);
        errorPassword = v.findViewById(R.id.profile_invalid_password);
        addShop = v.findViewById(R.id.add_shop);
        edit = v.findViewById(R.id.btnEditProfile);
        done = v.findViewById(R.id.btnDoneProfile);

        done.hide();

        final String originalEmail = email.getText().toString();
        final String originalPassword = password.getText().toString();

        addShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = UserFragmentDirections.actionUserFragmentToAddShopFragment();
                Navigation.findNavController(v).navigate(action);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setVisibility(View.GONE);
                email_edit.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                password_edit.setVisibility(View.VISIBLE);
                errorEmail.setVisibility(View.INVISIBLE);
                errorPassword.setVisibility(View.INVISIBLE);
                edit.hide();
                done.show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = email_edit.getText().toString();
                if(!isEmailFormat(newEmail)) {
                    newEmail = originalEmail;
                    errorEmail.setVisibility(View.VISIBLE);
                }
                String newPassword = password_edit.getText().toString();
                if(newPassword.trim().length() == 0){
                    newPassword = originalPassword;
                    errorPassword.setVisibility(View.VISIBLE);
                }

                email.setVisibility(View.VISIBLE);
                email.setText(newEmail);
                email_edit.setVisibility(View.GONE);
                email_edit.setText(newEmail);

                password.setVisibility(View.VISIBLE);
                password_edit.setVisibility(View.GONE);
                password.setText(newPassword);
                password_edit.setText(newPassword);

                edit.show();
                done.hide();
                InputMethodManager imm = (InputMethodManager) done.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });




        return v;
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
package com.beanleaf;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private DatabaseReference root;

    public Database() {
        root = FirebaseDatabase.getInstance().getReference().child("User");
    }

    public void register_user(String username, String password, String email, String name) {
        root = FirebaseDatabase.getInstance().getReference().child("User");

        User user = new User();
        user.set_username(username);
        user.set_password(password);
        user.set_name(name);
        user.set_email(email);

        root.child().setValue(user);
    }
}

package com.syp.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserOrder extends Fragment {
    private void fetchOrderInfo(){

        // Get User Database Reference from firebase
        DatabaseReference userRef = Singleton.get(mainActivity).getDatabase()
                .child(Singleton.firebaseUserTag)
                .child(Singleton.get(mainActivity).getUserId())
                .child(Singleton.get);

        // Add Event Listener for spot in database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Convert to user
                User user =  dataSnapshot.getValue(User.class);

                if(user != null)
                    setUserInfo(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}

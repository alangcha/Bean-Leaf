package com.syp.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class Singleton {

    private static Singleton singleton;
    private Context context;
    private DatabaseReference database;
    private FirebaseStorage storage;
    private FirebaseUser firebaseUser;

    private User currentUser;
    private HashMap<String, Cafe> cafes;
    private Order currentOrder;
    private String currentCafeId;
    private int currentItemIndex;
    private int currentMerchantCafeIndex;
    private int currentMerchantItemIndex;
    private int currentMerchantItemImageIndex;

    // --------------------
    // Singleton functions
    // --------------------
    private Singleton(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        cafes = new HashMap<>();
    }

    public static Singleton get(Context context) {
        if (singleton == null) {
            singleton = new Singleton(context);
        }
        return singleton;
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void insertCafes() {
        String key = database.child("cafes").push().getKey();
        Cafe cafe = new Cafe();
        cafe.set_id(key);
        cafe.set_name("Dong Cha");
        cafe.set_address("935 W 30th St");
        cafe.set_latitude(37.400403);
        cafe.set_longitude(-122.113402);

        Toast.makeText(context, "Cafe 1 added", Toast.LENGTH_LONG).show();
        database.child("cafes").child(key).setValue(cafe.toMap());

        key = database.child("cafes").push().getKey();
        cafe = new Cafe();
        cafe.set_id(key);
        cafe.set_name("Pot of Chang");
        cafe.set_address("935 W 30th St");
        cafe.set_latitude(37.400503);
        cafe.set_longitude(-122.113522);

        Toast.makeText(context, "Cafe 2 added", Toast.LENGTH_LONG).show();
        database.child("cafes").child(key).setValue(cafe.toMap());
    }

    public void insertItems() {
        String key = database.child("cafes").child(currentCafeId).child("items").push().getKey();
        Item item = new Item();
        item.set_id(key);
        item.set_name("The TEA");
        item.set_price(11.5);
        item.set_caffeine(30);

        Toast.makeText(context, "Item 1 added", Toast.LENGTH_LONG).show();
        database.child("cafes").child(currentCafeId).child("items").child(key).setValue(item.toMap());

        key = database.child("cafes").child(currentCafeId).child("items").push().getKey();
        item = new Item();
        item.set_id(key);
        item.set_name("The Juice");
        item.set_price(10);

        Toast.makeText(context, "Item 2 added", Toast.LENGTH_LONG).show();
        database.child("cafes").child(currentCafeId).child("items").child(key).setValue(item.toMap());
    }

    public void setFirebaseUser(FirebaseUser u) {
        this.firebaseUser = u;
    }

    // public void uploadFile(Uri uri, String extension) {
    // StorageReference fileReference =
    // storage.getReference().child(System.currentTimeMillis() + "." + extension);
    // fileReference.putFile(uri).addOnSuccessListener(new
    // OnSuccessListener<UploadTask.TaskSnapshot>() {
    // @Override
    // public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
    // String uploadId = database.getReference("Images").push().getKey();
    // database.getReference("Images").child(uploadId).setValue(fileReference.getDownloadUrl());
    // }
    // }).addOnFailureListener(new OnFailureListener() {
    // @Override
    // public void onFailure(@NonNull Exception e) {
    //
    // }
    // }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
    // @Override
    // public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
    //
    // }
    // });
    // }

    // --------------------
    // Data base push pull
    // --------------------
    // public User getUserFromDatabase(String email) {
    // User u = new User();
    //
    // DatabaseReference userDocument = database.getReference("Users");
    // Query q = userDocument.orderByChild("email").equalTo(email);
    //
    // q.addValueEventListener(new ValueEventListener() {
    // @Override
    // public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    // if (dataSnapshot.exists()) {
    // dataSnapshot.getValue(User.class);
    // }
    // }
    //
    // @Override
    // public void onCancelled(@NonNull DatabaseError databaseError) {
    //
    // }
    // });
    //
    // return u;
    // }

    public void pushUserToDatabase() {
        User u = new User();
        u.setEmail(firebaseUser.getEmail());
        u.setDisplayName(firebaseUser.getDisplayName());
        u.setMerchant(false);

        // database.getReference().child("Users").push();
    }

    private void pullCafesFromDatabase() {
    }

    private void pushCafeIntoDatabase() {
    }

    private void removeCafeFromDatabase() {
    }

    private void pushItemIntoCafeInDatabase() {
    }

    private void removeItemFromCafeInDatabase() {
    }

    private void editItemInDatabase() {
    }

    private void pushOrderIntoDatabase() {
    }

    // --------------------
    // Current items / shop indexes
    // --------------------

    public HashMap<String, Cafe> getCafes() {
        if (cafes.size() == 0)
            pullCafesFromDatabase();
        return cafes;
    }

    public ArrayList<Cafe> getUserCafes() {
        return currentUser.cafes;
    }

    public Cafe getCurrentCafe() {
        return cafes.get(currentCafeId);
    }

    public Cafe getCurrentMerchantCafe() {
        return currentUser.cafes.get(currentMerchantCafeIndex);
    }

    public Item getCurrentItem() {
        return getCurrentCafe().menu.get(currentItemIndex);
    }

    public Item getCurrentMerchantItem() {
        return getCurrentMerchantCafe().menu.get(currentMerchantItemIndex);
    }

    public void setCurrentCafeId(String cafeId) {
        this.currentCafeId = cafeId;
    }

    public void setCurrentMerchantCafeIndex(int currentMerchantCafeIndex) {
        this.currentMerchantCafeIndex = currentMerchantCafeIndex;
    }

    public void setCurrentItemIndex(int currentItemIndex) {
        this.currentItemIndex = currentItemIndex;
    }

    public void setCurrentMerchantItemIndex(int currentMerchantItemIndex) {
        this.currentMerchantItemIndex = currentMerchantItemIndex;
    }

    public String getCurrentCafeId() {
        return currentCafeId;
    }

    public int getCurrentItemIndex() {
        return currentItemIndex;
    }

    public int getCurrentMerchantCafeIndex() {
        return currentMerchantCafeIndex;
    }

    public int getCurrentMerchantItemIndex() {
        return currentMerchantItemIndex;
    }

    // --------------------
    // Adding / Removing / Editing objects
    // --------------------

    public void addCafeIfNotExist(String id, Cafe cafe) {
        if (!cafes.containsKey(id)) {
            Log.d("ADDING", id);
            cafes.put(id, cafe);
        }
    }

    public void addItem(Item item) {
        cafes.get(currentMerchantCafeIndex).menu.add(item);
        pushItemIntoCafeInDatabase();
    }

    public void removeCafe() {
        cafes.remove(currentMerchantCafeIndex);
        removeCafeFromDatabase();
    }

    public void removeItem() {
        cafes.get(currentMerchantCafeIndex).menu.remove(currentMerchantItemIndex);
        removeItem();
    }

    public void editItem(Item i) {
        removeItem();
        addItem(i);
    }

    public void completeOrder() {
        currentOrder.setTimestamp(System.currentTimeMillis());
        pushOrderIntoDatabase();
        currentOrder = new Order();
        currentOrder.setUser(currentUser);
    }

    public void addItemToOrder(ArrayList<Item> i) {
        for (Item item : i) {
            currentOrder.add_item(item);
        }
    }

    public void removeItemFromOrdeR(Item i) {
        currentOrder.removeItem(i);
    }

}
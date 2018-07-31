package com.eliassilva.sportagenda;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Elias on 31/07/2018.
 */
public class CustomApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
    }
}

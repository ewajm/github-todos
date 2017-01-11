package com.epicodus.githubtodos.utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ewa on 1/9/2017.
 */

public class DatabaseUtil {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        return mDatabase;
    }
}

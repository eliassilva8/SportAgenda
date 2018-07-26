package com.eliassilva.sportagenda;/*package com.eliassilva.sportagenda;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

*/

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Elias on 12/07/2018.
 */
public class FirebaseDatabaseSingleton {
    private static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    protected FirebaseDatabaseSingleton() {
        firebaseDatabase.setPersistenceEnabled(true);
    }

    public static FirebaseDatabase getInstance() {
        return firebaseDatabase;
    }
}

package com.example.mobiledevproject;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class FireBaseManager {

    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();


    public static void signUp(String email, String password) {

        // receive valid email and password and register user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //TODO: handle success
                return;
                // Sign in success, update UI with the signed-in user's information
                //updateUI(user);
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                //updateUI(null);
            }
        });
    }


    public static Task<AuthResult> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

}

package com.example.mobiledevproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    EditText email;
    EditText password;
    Button btn;

    TextView emailError;
    TextView passError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        setListeners();
    }

    void findViews() {
        email = findViewById(R.id.Login_ET_email);
        password = findViewById(R.id.Login_ET_password);
        btn = findViewById(R.id.Login_BTN_login);
        emailError = findViewById(R.id.Login_TV_error);
        passError = findViewById(R.id.Login_TV_error2);
    }

    void setListeners() {

        email.setOnKeyListener((v, keyCode, event) -> {
            emailError.setVisibility(View.INVISIBLE);
            passError.setVisibility(View.INVISIBLE);
            return false;
        });

        View.OnClickListener registerListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().equals("")) {
                    auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            finish();
                        } else {
                            String errorCode = ((FirebaseAuthException) task1.getException()).getErrorCode();
                            if (errorCode.equals("ERROR_WEAK_PASSWORD")) {
                                password.setError(task1.getException().getMessage());
                            }
                            else {
                                Log.d("Error: ", ((FirebaseAuthException) task1.getException()).getErrorCode() + task1.getException().getMessage());
                            }
                        }
                    });
                }

            }
        };

        View.OnClickListener loginListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().equals("")) {
                    auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            finish();
                        } else {

                            String errorCode = ((FirebaseAuthException) task1.getException()).getErrorCode();
                            Log.d(errorCode, task1.getException().getMessage());

                            if(errorCode.equals("ERROR_WRONG_PASSWORD")) {
                                password.setError(task1.getException().getMessage());
                            }
                        }
                    });
                }
            }
        };

        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (email.getText() != null && !email.getText().toString().equals("")) {
                    auth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            email.setEnabled(false);
                            email.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.medium));
                            boolean isNewUser = Objects.requireNonNull(task.getResult().getSignInMethods()).isEmpty();
                            password.setEnabled(true);
                            password.setVisibility(android.view.View.VISIBLE);
                            if (isNewUser) {
                                btn.setText("Register");
                                btn.setOnClickListener(registerListener);
                                password.setOnEditorActionListener((v1, actionId, event) -> {
                                    registerListener.onClick(v1);
                                    return false;
                                });
                                email.setOnEditorActionListener((v1, actionId, event) -> {
                                    registerListener.onClick(v1);
                                    return false;
                                });
                            } else {
                                btn.setText("Login");
                                btn.setOnClickListener(loginListener);
                                password.setOnEditorActionListener((v1, actionId, event) -> {
                                    loginListener.onClick(v1);
                                    return false;
                                });
                                email.setOnEditorActionListener((v1, actionId, event) -> {
                                    loginListener.onClick(v1);
                                    return false;
                                });
                            }
                        }
                        else
                        {
                            emailError.setVisibility(View.VISIBLE);
                            emailError.setText(task.getException().getMessage());
                        }
                    });
                }

            }
        };

        btn.setOnClickListener(listener);

        email.setOnEditorActionListener((v, actionId, event) -> {
            listener.onClick(v);
            return true;
        });
        password.setOnEditorActionListener((v, actionId, event) -> {
            listener.onClick(v);
            return true;
        });

    }
}
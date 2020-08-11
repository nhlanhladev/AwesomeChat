package com.example.awesomechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private EditText nameEditText;
    private Button loginSignUpButton;
    private TextView toggleLoginSignUpTextView;
    private boolean loginModeActive;

    private final String TAG = "";

    private FirebaseAuth mAuth;


    FirebaseDatabase database;
    DatabaseReference usersDatabaseReferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.emailEditText);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        nameEditText = findViewById(R.id.emailEditText);
        loginSignUpButton = findViewById(R.id.loginSignUpButton);
        toggleLoginSignUpTextView = findViewById(R.id.toggleLoginSignUpTextView);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        usersDatabaseReferences = database.getReference().child("users");


        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginSignUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, UserListActivity.class));
        }


    }

    // for login
    private void loginSignUpUser(String email, String password) {
        // checks that login
        if (loginModeActive) {
            if (passwordEditText.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "password must be at least 7 characters",
                        Toast.LENGTH_SHORT).show();

            }else if(emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please Input Your email",
                        Toast.LENGTH_SHORT).show();
            }else{
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity .class);
                                    intent.putExtra("userName",nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //  updateUI(null);
                                    // ...
                                }


                            }
                        });
            }
            
        }else{
            if (!passwordEditText.getText().toString().trim().equals(
                    repeatPasswordEditText.getText().toString().trim()
            )){
                Toast.makeText(this,"Password don' t match",Toast.LENGTH_SHORT).show();
            }else if (passwordEditText.getText().toString().trim().length() < 7){
                  Toast.makeText(this,"password must be at least 7 characters",Toast.LENGTH_SHORT).show();
            } if (emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this,"Please Input your email",Toast.LENGTH_SHORT).show();
            } else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //   Log.d(TAG, "createUserWithEmail:success");

                                    Toast.makeText(SignInActivity.this, "user registered", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUser(user);

                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName",nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //  Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //   updateUI(null);
                                }

                                // ...
                            }

                        });
            }


        }

        }

    private void createUser(FirebaseUser firebaseUser) {

        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());



        usersDatabaseReferences.push().setValue(user);

    }

    public void toggleLoginMode(View view) {
        if (loginModeActive){
            loginModeActive = false;
            loginSignUpButton.setText("Sign Up");
            toggleLoginSignUpTextView.setText("Or, Log in");
            repeatPasswordEditText.setVisibility(View.VISIBLE);
        }else{
            loginModeActive = true;
            loginSignUpButton.setText("Log In");
            toggleLoginSignUpTextView.setText("Or, Sign Up");
            repeatPasswordEditText.setVisibility(View.GONE);
        }
    }
}




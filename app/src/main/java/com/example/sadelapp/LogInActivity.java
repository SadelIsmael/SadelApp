package com.example.sadelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LogInActivity extends AppCompatActivity {

    private Utilities utils;
    private EditText etEmail,etPassword;
    private FirebaseServices fbs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().hide();
        connectComponents();
    }



    private void connectComponents(){
        etEmail=findViewById(R.id.etUsernameLOGIN);
        etPassword=findViewById(R.id.etPasswordLOGIN);
        utils = Utilities.getInstance();
        fbs = FirebaseServices.getInstance();
    }


    public void logIN(View view){
        String username = etEmail.getText().toString();
        String password = etPassword.getText().toString();


        //check if the email or the password is empty
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, "Username or password is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!utils.validateEmail(username) || !utils.validatePassword(password))
        {
            Toast.makeText(this, R.string.err_incorrect_user_password, Toast.LENGTH_SHORT).show();
            return;
        }

        fbs.getAuth().signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(LogInActivity.this, AllProductsActivity.class);
                            startActivity(i);

                        } else {
                            Toast.makeText(LogInActivity.this, R.string.err_incorrect_user_password, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }








    public void goToMainPage(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
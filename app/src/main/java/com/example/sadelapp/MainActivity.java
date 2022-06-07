package com.example.sadelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }

    public void goToLogIn(View view) {
        Intent i = new Intent(this, LogInActivity.class);
        startActivity(i);
    }



    public void goToSignUp(View view) {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void gotoAddProduct(View view) {
        Intent i = new Intent(this, AddProductActivity.class);
        startActivity(i);
    }

    public void gotoAllProducts(View view) {
        Intent i = new Intent(this, AllProductsActivity.class);
        startActivity(i);
    }

}
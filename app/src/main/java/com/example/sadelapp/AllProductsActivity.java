package com.example.sadelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {

    private RecyclerView rvAllRest;
    AdapterProduct adapter;
    FirebaseServices fbs;
    ArrayList<Product> products;
    MyCallBack myCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        fbs = FirebaseServices.getInstance();
        products = new ArrayList<Product>();
        readData();
        myCallback = new MyCallBack() {
            @Override
            public void onCallback(List<Product> productsList) {
                RecyclerView recyclerView = findViewById(R.id.rvAllProducts);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new AdapterProduct(getApplicationContext(), products);
                recyclerView.setAdapter(adapter);
            }
        };

    }

    private void readData() {
        try {

            fbs.getFire().collection("products")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    products.add(document.toObject(Product.class));
                                }

                                myCallback.onCallback(products);
                            } else {
                                Log.e("AllProductsActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "error reading!" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
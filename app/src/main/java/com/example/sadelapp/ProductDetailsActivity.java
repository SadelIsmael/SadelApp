package com.example.sadelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView tvProName, tvInfo, tvProPrice,tvProCompany;
    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        connectComponents();
        Intent i = this.getIntent();
        Product product = (Product) i.getSerializableExtra("product");

        tvProName.setText(product.getProductName());
        tvInfo.setText(product.getProInfo());
        tvProCompany.setText(product.getProCompany());
        tvProPrice.setText(product.getProPrice());
        Picasso.get().load(product.getProPhoto()).into(ivPhoto);
    }

    private void connectComponents() {
        tvProName = findViewById(R.id.etProductNameAddProduct);
        tvInfo = findViewById(R.id.etdInfoAddProduct);
        tvProCompany = findViewById(R.id.etCompanyProductDetails);
        tvProPrice = findViewById(R.id.etProPriceProductDetails);
        ivPhoto = findViewById(R.id.ivPhotoProductDetails);
    }
}
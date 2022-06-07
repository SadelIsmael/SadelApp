package com.example.sadelapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddProductActivity extends AppCompatActivity {

    private static final String TAG = "AddProductActivity";
    private EditText etName, etInfo;
    private Spinner spCat;
    private ImageView ivPhoto;
    private EditText etPrice;
    private EditText etCompany;
    private FirebaseServices fbs;
    private Uri filePath;
    private StorageReference storageReference;
    private String refAfterSuccessfullUpload = null;
    private String downloadableURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getSupportActionBar().hide();
        connectComponents();

    }

    private void connectComponents() {
        etName = findViewById(R.id.etProductNameAddProduct);
        spCat = findViewById(R.id.spProductCatADDPRODUCT);
        etInfo = findViewById(R.id.etdInfoAddProduct);
        ivPhoto = findViewById(R.id.ivPhotoAddProduct);
        etPrice=findViewById(R.id.etProPriceAddProduct);
        etCompany=findViewById(R.id.etProductCompanyAddPro);
        fbs = FirebaseServices.getInstance();
        spCat.setAdapter(new ArrayAdapter<ProductCat>(this, android.R.layout.simple_list_item_1, ProductCat.values()));
        storageReference = fbs.getStorage().getReference();
    }


    public void add(View view) {
        // check if any field is empty
        String productName,  proInfo,  proCompany,  proPhoto,  proPrice;
        productName = etName.getText().toString();
        proInfo = etInfo.getText().toString();
        proPrice=etPrice.getText().toString();
        proCompany=etCompany.getText().toString();
        // TODO: get price
        // TODO: get company
        if (ivPhoto.getDrawable() == null)
            proPhoto = "no_image";
        else proPhoto = downloadableURL;

        if (productName.trim().isEmpty() || proInfo.trim().isEmpty()
                || proPhoto.trim().isEmpty())
        {
            Toast.makeText(this, "error fields empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(  productName, proInfo, proCompany, proPhoto, proPrice);
        //public Product(String productName, String proInfo, String proCompany, String proPhoto, String proPrice) {
        fbs.getFire().collection("restaurants")
                .add(product)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void selectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),40);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 40) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    filePath = data.getData();
                    ivPhoto.setBackground(null);
                    Picasso.get().load(filePath).into(ivPhoto);
                    uploadImage();
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            String fileNameStr = filePath.toString().substring(filePath.toString().lastIndexOf("/")+1);
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + fileNameStr);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(AddProductActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    refAfterSuccessfullUpload = ref.toString();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddProductActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
}

package com.example.sadelapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {
    private List<Product> mData;
    private LayoutInflater mInflater;
    private Context context;
    private final AdapterProduct.ItemClickListener mClickListener=new ItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            Product product = mData.get(position);
            Intent i = new Intent(context, ProductDetailsActivity.class);
            i.putExtra("product", String.valueOf(product));

            context.startActivity(i);
        }
    };

    // data is passed into the constructor
    AdapterProduct(Context context, List<Product> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }


    // inflates the row layout from xml when needed
    @Override
    public AdapterProduct.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row, parent, false);
        return new AdapterProduct.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(AdapterProduct.ViewHolder holder, int position) {
        Product product = mData.get(position);
        holder.tvName.setText(product.getProductName());
        Picasso.get().load(product.getProPhoto()).into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName;
        ImageView ivPhoto;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameProductRow);
            ivPhoto = itemView.findViewById(R.id.ivPhotoProductRow);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    // convenience method for getting data at click position
    Product getItem(int id) {
        return mData.get(id);
    }



    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}






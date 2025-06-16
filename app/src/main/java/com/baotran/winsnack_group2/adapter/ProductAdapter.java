package com.baotran.winsnack_group2.adapter;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.AboutUsActivity;
import com.baotran.winsnack_group2.R;
import com.baotran.winsnack_group2.models.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void updateList(List<Product> newList) {
        products = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
        Log.d(TAG, "Loading image: " + product.getImage());
        Glide.with(context)
                .load(product.getImage())
                .thumbnail(0.25f)
                .timeout(30000)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .listener(new com.bumptech.glide.request.RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e(TAG, "Image load failed: " + product.getImage(), e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d(TAG, "Image loaded: " + product.getImage());
                        return false;
                    }
                })
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AboutUsActivity.class);
            intent.putExtra("productId", product.getProductID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice, productName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productPrice = itemView.findViewById(R.id.product_price);
            productName = itemView.findViewById(R.id.product_name);
        }
    }
}
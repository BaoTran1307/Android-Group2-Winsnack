package com.baotran.winsnack_group2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.R;
import com.baotran.winsnack_group2.models.CartItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> items;
    private Context context;
    private OnItemInteractionListener listener;

    public interface OnItemInteractionListener {
        void onItemChecked(int position, boolean isChecked);
        void onQuantityChanged(int position, int newQuantity);
    }

    public CartAdapter(Context context, List<CartItem> items, OnItemInteractionListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvName.setText(item.getProductName() != null ? item.getProductName() : "Unknown Product");
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice() != 0 ? item.getPrice() : 0.0));
        // Xóa dòng holder.tvDateTime.setText("N/A");
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context).load(item.getImage()).into(holder.ivProductImage);

        holder.cbSelect.setChecked(item.isChecked());
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            if (listener != null) listener.onItemChecked(position, isChecked);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                if (listener != null) listener.onQuantityChanged(position, currentQuantity - 1);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (listener != null) listener.onQuantityChanged(position, currentQuantity + 1);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, txtQuantity; // Xóa tvDateTime
        ImageView ivProductImage;
        CheckBox cbSelect;
        ImageButton btnDecrease, btnIncrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }
}
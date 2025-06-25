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
    private OnItemActionListener listener;
    private Context context;

    public interface OnItemActionListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemChecked(int position, boolean isChecked);
    }

    public CartAdapter(List<CartItem> items, OnItemActionListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        // Chuyển Long thành String
        holder.tvName.setText(item.getProductID() != null ? item.getProductID().toString() : "Unknown");
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice() != 0 ? item.getPrice() : 0.0));
        holder.tvDateTime.setText("N/A");
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context).load(item.getImage()).into(holder.ivProductImage);

        holder.cbSelect.setChecked(false);
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
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
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvDateTime, txtQuantity;
        ImageButton btnDecrease, btnIncrease;
        CheckBox cbSelect;
        ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
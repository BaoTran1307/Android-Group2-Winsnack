package com.baotran.winsnack_group2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.R;
import com.baotran.winsnack_group2.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> items;
    private OnItemActionListener listener; // Interface để xử lý hành động (tăng/giảm, chọn)

    // Interface để xử lý hành động
    public interface OnItemActionListener {
        void onQuantityChanged(int position, int newQuantity);
        void onItemChecked(int position, boolean isChecked);
    }

    public CartAdapter(List<CartItem> items, OnItemActionListener listener) {
        this.items = items;
        this.listener = listener;
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
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());
        holder.tvDateTime.setText(item.getDateTime());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // Setup checkbox listener
        holder.cbSelect.setChecked(false); // Mặc định không chọn
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onItemChecked(position, isChecked);
            }
        });

        // Setup decrease button
        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.txtQuantity.getText().toString());
            if (currentQuantity > 1) {
                holder.txtQuantity.setText(String.valueOf(currentQuantity - 1));
                if (listener != null) {
                    listener.onQuantityChanged(position, currentQuantity - 1);
                }
            }
        });

        // Setup increase button
        holder.btnIncrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.txtQuantity.getText().toString());
            holder.txtQuantity.setText(String.valueOf(currentQuantity + 1));
            if (listener != null) {
                listener.onQuantityChanged(position, currentQuantity + 1);
            }
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            cbSelect = itemView.findViewById(R.id.cbSelect);
        }
    }
}
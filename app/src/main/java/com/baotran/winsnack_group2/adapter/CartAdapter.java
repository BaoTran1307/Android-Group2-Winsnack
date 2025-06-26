package com.baotran.winsnack_group2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.R;
import com.baotran.winsnack_group2.models.CartItem;
import com.baotran.winsnack_group2.PaymentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> items;
    private Context context;
    private OnItemInteractionListener listener;

    public interface OnItemInteractionListener {
        void onItemChecked(int position, boolean isChecked);
        void onQuantityChanged(int position, int newQuantity);
        void onCancelOrder(int position);
    }

    public CartAdapter(Context context, List<CartItem> items, OnItemInteractionListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = context instanceof PaymentActivity ? R.layout.item_payment : R.layout.item_cart;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvName.setText(item.getProductName() != null ? item.getProductName() : "Unknown Product");
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice() != 0 ? item.getPrice() : 0.0));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // Tải hình ảnh với Glide
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.placeholder) // Thêm placeholder
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache hình ảnh
                .into(holder.ivProductImage);

        // Chỉ xử lý checkbox nếu nó tồn tại
        if (holder.cbSelect != null) {
            holder.cbSelect.setChecked(item.isChecked());
            holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
                if (listener != null) listener.onItemChecked(position, isChecked);
            });
        }

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

        holder.cancelOrderButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelOrder(position); // Sử dụng position thay vì getAdapterPosition
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, txtQuantity;
        ImageView ivProductImage;
        CheckBox cbSelect;
        ImageButton btnDecrease, btnIncrease;
        Button cancelOrderButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            cbSelect = itemView.findViewById(R.id.cbSelect);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            cancelOrderButton = itemView.findViewById(R.id.cancel_order_button);
        }
    }
}

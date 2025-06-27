package com.baotran.winsnack_group2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.OrderDetailsActivity;
import com.baotran.winsnack_group2.R;
import com.baotran.winsnack_group2.models.Order;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvDate.setText("Date: " + order.getDate());
        holder.tvTotal.setText(new DecimalFormat("$#.##").format(order.getTotal()));
        holder.tvStatus.setText("Status: " + order.getStatus());
        holder.tvAddress.setText("Address: " + order.getAddress());
        holder.tvPaymentMethod.setText("Payment: " + order.getPaymentMethod());
        Glide.with(context).load(order.getImage()).into(holder.ivImage);

        // Thêm sự kiện nhấn vào Order ID để chuyển sang OrderDetailsActivity
        holder.tvOrderId.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailsActivity.class);
            intent.putExtra("orderId", order.getOrderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvDate, tvTotal, tvStatus, tvAddress, tvPaymentMethod;
        ImageView ivImage;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
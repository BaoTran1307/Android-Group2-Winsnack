package com.baotran.winsnack_group2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends DialogFragment {

    private TextView tvItemCount, tvTotal;
    private com.google.android.material.button.MaterialButton btnCheckout;
    private LinearLayout llCartItems;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_cart, container, false);

        // Initialize views
        tvItemCount = view.findViewById(R.id.tvItemCount);
        tvTotal = view.findViewById(R.id.tvTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);
        llCartItems = view.findViewById(R.id.llCartItems);

        // Get current date and time
        String currentDateTime = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(new Date());

        // Add hardcoded items
        View item1 = createCartItem(inflater, "Strawberry Shake", "$20.00", currentDateTime, 2);
        llCartItems.addView(item1);

        View item2 = createCartItem(inflater, "Strawberry Shake", "$20.00", currentDateTime, 2);
        llCartItems.addView(item2);

        // Set initial text
        tvItemCount.setText("You have 2 items in the cart");
        tvTotal.setText("$40.00");

        // Set click listener for Checkout button
        btnCheckout.setOnClickListener(v -> {
            dismiss(); // Close dialog
        });

        return view;
    }

    private View createCartItem(LayoutInflater inflater, String name, String price, String dateTime, int quantity) {
        View itemView = inflater.inflate(R.layout.item_cart, llCartItems, false);
        TextView tvName = itemView.findViewById(R.id.tvName);
        TextView tvPrice = itemView.findViewById(R.id.tvPrice);
        TextView tvDateTime = itemView.findViewById(R.id.tvDateTime);
        TextView txtQuantity = itemView.findViewById(R.id.txtQuantity);
        ImageButton btnDecrease = itemView.findViewById(R.id.btnDecrease);
        ImageButton btnIncrease = itemView.findViewById(R.id.btnIncrease);
        CheckBox cbSelect = itemView.findViewById(R.id.cbSelect);

        // Set item data
        tvName.setText(name);
        tvPrice.setText(price);
        tvDateTime.setText(dateTime);
        txtQuantity.setText(String.valueOf(quantity));

        // Setup quantity controls
        btnDecrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(txtQuantity.getText().toString());
            if (currentQuantity > 1) {
                txtQuantity.setText(String.valueOf(currentQuantity - 1));
                updateTotal();
            }
        });

        btnIncrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(txtQuantity.getText().toString());
            txtQuantity.setText(String.valueOf(currentQuantity + 1));
            updateTotal();
        });

        // Optional: Handle checkbox selection
        cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Add logic for selected items if needed (e.g., update total for selected items)
            updateTotal(); // Cập nhật tổng khi checkbox thay đổi (tùy chọn)
        });

        return itemView;
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < llCartItems.getChildCount(); i++) {
            View item = llCartItems.getChildAt(i);
            TextView txtQuantity = item.findViewById(R.id.txtQuantity);
            CheckBox cbSelect = item.findViewById(R.id.cbSelect);
            int quantity = Integer.parseInt(txtQuantity.getText().toString());
            // Chỉ tính tổng cho item được chọn (nếu checkbox được tích)
            if (cbSelect.isChecked()) {
                total += 20.0 * quantity; // Giả định giá $20.00
            }
        }
        tvTotal.setText(String.format("$%.2f", total));
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set dialog width to 85% of screen width
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.MATCH_PARENT);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    // Database methods (commented out)
    /*
    private void loadDataFromDatabase() {
        // Assume we have a DatabaseHelper class
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        // Logic to load items from database and add to llCartItems
        // Example: List<CartItem> cartItems = dbHelper.getCartItems();
        // for (CartItem item : cartItems) {
        //     llCartItems.addView(createCartItem(inflater, item.getName(), item.getPrice(), item.getDateTime(), item.getQuantity()));
        // }
        // tvItemCount.setText("You have " + cartItems.size() + " items in the cart");
        // updateTotal();
    }
    */
}

package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private SeekBar priceSeekBar;
    private TextView minPriceText, maxPriceText;
    private String selectedCategory = null;
    private double maxPrice = Double.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo UI
        searchEditText = findViewById(R.id.search_edit_text);
        priceSeekBar = findViewById(R.id.price_seekbar);
        minPriceText = findViewById(R.id.min_price_text);
        maxPriceText = findViewById(R.id.max_price_text);
        ImageView backButton = findViewById(R.id.back_button);
        Button applyButton = findViewById(R.id.apply_button);

        // Thiết lập SeekBar
        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double price = 1 + (progress * 9.0 / 100); // Từ $1 đến $10
                maxPrice = price;
                maxPriceText.setText("$" + String.format("%.1f", price) + " >");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Thiết lập nút quay lại
        backButton.setOnClickListener(v -> finish());

        // Thiết lập nút Apply
        applyButton.setOnClickListener(v -> applyFilters());
        setupCategoryListeners();
    }

    private void setupCategoryListeners() {
        int[] categoryIds = {R.id.mixed_category, R.id.grilled_category, R.id.sweet_category, R.id.combo_category, R.id.ingredients_category};
        String[] categoryCodes = {"BT01", "BT02", "BT03", "BT04", "BT05"};

        for (int i = 0; i < categoryIds.length; i++) {
            LinearLayout categoryLayout = findViewById(categoryIds[i]);
            if (categoryLayout != null) {
                final String code = categoryCodes[i];
                categoryLayout.setOnClickListener(v -> {
                    selectedCategory = code;
                });
            }
        }
    }

    private void applyFilters() {
        String query = searchEditText.getText().toString().trim();
        Intent intent = new Intent(SearchActivity.this, CategoryActivity.class);
        intent.putExtra("search_query", query.isEmpty() ? null : query);
        intent.putExtra("category_code", selectedCategory);
        intent.putExtra("max_price", maxPrice);
        startActivity(intent);
        finish();
    }
}
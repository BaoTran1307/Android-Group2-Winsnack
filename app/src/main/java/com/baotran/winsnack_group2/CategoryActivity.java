package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.ProductAdapter;
import com.baotran.winsnack_group2.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends FooterActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private FirebaseFirestore db;
    private String categoryCode;
    private String categoryName;
    private TextView titleView;
    private ImageView filterButton;
    private EditText searchEditText;
    private String searchQuery = null;
    private List<Product> fullList = new ArrayList<>();
    private List<Product> filteredList = new ArrayList<>();
    private double maxPrice = Double.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setupFooter();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewCategory);
        titleView = findViewById(R.id.categoryTitle);
        filterButton = findViewById(R.id.filterButton);
        searchEditText = findViewById(R.id.search_edit_text);

        // Lấy dữ liệu từ Intent
        searchQuery = getIntent().getStringExtra("search_query");
        categoryCode = getIntent().getStringExtra("category_code");
        categoryName = getIntent().getStringExtra("category_name");
        Double maxPriceExtra = getIntent().getDoubleExtra("max_price", Double.MAX_VALUE);
        if (maxPriceExtra != null) maxPrice = maxPriceExtra;

        // Hiển thị tiêu đề
        if (searchQuery != null && !searchQuery.isEmpty()) {
            titleView.setText("Search Results for: " + searchQuery);
            loadAllProductsAndFilter(searchQuery);
        } else if (categoryCode != null) {
            titleView.setText(categoryName);
            loadProductsByCategory(categoryCode);
        } else {
            titleView.setText("All Products");
            loadAllProducts();
        }

        // Setup RecyclerView
        adapter = new ProductAdapter(this, filteredList, R.layout.item_product_category);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột
        recyclerView.setAdapter(adapter);

        // Tìm kiếm trực tiếp trên trang
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearchKey = actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN);

            if (isSearchKey) {
                String keyword = searchEditText.getText().toString().trim();
                if (!keyword.isEmpty()) {
                    filterProductsByQuery(keyword);
                }
                return true;
            }
            return false;
        });

        // Mở SearchActivity khi nhấn filter
        filterButton.setOnClickListener(v -> startActivity(new Intent(CategoryActivity.this, SearchActivity.class)));
    }

    private void loadAllProductsAndFilter(String keyword) {
        db.collection("PRODUCT")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fullList.clear();
                        filteredList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            fullList.add(product);
                            if (product.getPrice() <= maxPrice && // Lọc theo giá
                                    (product.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                                            product.getDescription().toLowerCase().contains(keyword.toLowerCase()))) {
                                filteredList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("CategoryActivity", "Error loading products: " + task.getException());
                    }
                });
    }

    private void filterProductsByQuery(String query) {
        filteredList.clear();
        for (Product p : fullList) {
            if (p.getPrice() <= maxPrice && // Lọc theo giá
                    (p.getProductName() != null && p.getProductName().toLowerCase().contains(query.toLowerCase()) ||
                            p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadAllProducts() {
        db.collection("PRODUCT")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fullList.clear();
                        filteredList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            fullList.add(product);
                            if (product.getPrice() <= maxPrice) {
                                filteredList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadProductsByCategory(String categoryCode) {
        db.collection("PRODUCT")
                .whereEqualTo("CategoryID", categoryCode)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fullList.clear();
                        filteredList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product product = doc.toObject(Product.class);
                            fullList.add(product);
                            if (product.getPrice() <= maxPrice) {
                                filteredList.add(product);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
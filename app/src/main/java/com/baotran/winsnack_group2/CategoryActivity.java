package com.baotran.winsnack_group2;

import android.os.Bundle;
import android.util.Log;
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

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> filteredList;
    private FirebaseFirestore db;
    private String categoryCode;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // layout XML bạn đã tạo

        recyclerView = findViewById(R.id.recyclerViewCategory);
//        ImageView btnBack = findViewById(R.id.btnBackCategory);
//        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu truyền từ HomeActivity
        categoryCode = getIntent().getStringExtra("category_code");
        categoryName = getIntent().getStringExtra("category_name");

        // Hiển thị tên danh mục
        TextView titleView = findViewById(R.id.categoryTitle);
        if (categoryName != null) {
            titleView.setText(categoryName);
        }

        // Setup RecyclerView
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(this, filteredList, R.layout.item_product_category);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu từ Firestore
        db = FirebaseFirestore.getInstance();
        loadProductsByCategory(categoryCode);
    }

    private void loadProductsByCategory(String categoryCode) {
        Log.d("CategoryActivity", "Loading products with category: " + categoryCode);
        db.collection("PRODUCT")
                .whereEqualTo("CategoryID", categoryCode) // Viết hoa 'CategoryID'
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        filteredList.clear();
                        int count = 0;
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Product p = doc.toObject(Product.class);
                            filteredList.add(p);
                            Log.d("CategoryActivity", "Loaded product: " + p.getProductName());
                            count++;
                        }
                        Log.d("CategoryActivity", "Total products loaded: " + count);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("CategoryActivity", "Load failed: " + task.getException().getMessage());
                    }
                });
    }

}

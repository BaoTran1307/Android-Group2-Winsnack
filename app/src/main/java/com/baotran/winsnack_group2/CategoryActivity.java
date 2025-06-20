package com.baotran.winsnack_group2;

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

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private FirebaseFirestore db;
    private String categoryCode;
    private String categoryName;
    TextView titleView;
    ImageView filterButton;
    private EditText searchEditText;
    private String searchQuery = null;
    private List<Product> fullList = new ArrayList<>(); // tất cả sản phẩm
    private List<Product> filteredList = new ArrayList<>(); // để adapter hiển thị




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu); // layout XML bạn đã tạo
        db = FirebaseFirestore.getInstance();


        recyclerView = findViewById(R.id.recyclerViewCategory);
//        ImageView btnBack = findViewById(R.id.btnBackCategory);
//        btnBack.setOnClickListener(v -> finish());

        // Lấy dữ liệu truyền từ HomeActivity
        categoryCode = getIntent().getStringExtra("category_code");
        categoryName = getIntent().getStringExtra("category_name");
        titleView = findViewById(R.id.categoryTitle);
        filterButton = findViewById(R.id.filterButton);


        filterButton.setOnClickListener(v -> showFilterMenu());
        searchQuery = getIntent().getStringExtra("search_query");
        categoryCode = getIntent().getStringExtra("category_code");
        categoryName = getIntent().getStringExtra("category_name");

        if (searchQuery != null) {
            titleView.setText("All Products");
            searchEditText = findViewById(R.id.search_edit_text); // ✅ thêm dòng này trước khi dùng
            searchEditText.setText(searchQuery); // hiển thị từ khoá
            loadAllProducts(); // ✅ load toàn bộ trước
        } else if (categoryCode != null) {
            titleView.setText(categoryName);
            loadProductsByCategory(categoryCode);
        } else {
            loadAllProducts(); // fallback
        }



        // Hiển thị tên danh mục
        TextView titleView = findViewById(R.id.categoryTitle);
        if (categoryName != null) {
            titleView.setText(categoryName);
        }
//        if (categoryCode != null) {
//            loadProductsByCategory(categoryCode);
//        } else {
//            loadAllProducts(); // ✅ THÊM DÒNG NÀY
//        }
        searchEditText = findViewById(R.id.search_edit_text);

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            boolean isSearchKey =
                    actionId == EditorInfo.IME_ACTION_SEARCH ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                    event.getAction() == KeyEvent.ACTION_DOWN);

            if (isSearchKey) {
                String keyword = searchEditText.getText().toString().trim();
                filterProductsByQuery(keyword);
                return true;
            }
            return false;
        });


        // Setup RecyclerView
        filteredList = new ArrayList<>();
        adapter = new ProductAdapter(this, filteredList, R.layout.item_product_category);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu từ Firestore
        db = FirebaseFirestore.getInstance();
        loadProductsByCategory(categoryCode);
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
                            // 👉 Lọc trực tiếp khi load
                            if (product.getProductName().toLowerCase().contains(keyword.toLowerCase()) ||
                                    product.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
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
        List<Product> result = new ArrayList<>();
        for (Product p : fullList) {
            if ((p.getProductName() != null && p.getProductName().toLowerCase().contains(query.toLowerCase())) ||
                    (p.getDescription() != null && p.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                result.add(p);
            }
        }
        filteredList.clear();
        filteredList.addAll(result);
        adapter.notifyDataSetChanged();
    }


    private void showFilterMenu() {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, filterButton);
        popup.getMenu().add("All Products");
        popup.getMenu().add("Mixed");
        popup.getMenu().add("Grilled");
        popup.getMenu().add("Sweet");
        popup.getMenu().add("Combo");
        popup.getMenu().add("Ingredients");

        popup.setOnMenuItemClickListener(item -> {
            String selected = item.getTitle().toString();
            titleView.setText(selected);

            switch (selected) {
                case "All Products":
                    loadAllProducts();
                    break;
                case "Mixed":
                    loadProductsByCategory("BT01");
                    break;
                case "Grilled":
                    loadProductsByCategory("BT02");
                    break;
                case "Sweet":
                    loadProductsByCategory("BT03");
                    break;
                case "Combo":
                    loadProductsByCategory("BT04");
                    break;
                case "Ingredients":
                    loadProductsByCategory("BT05");
                    break;
            }
            return true;
        });

        popup.show();
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
                        }

                        // Nếu có searchQuery truyền từ Home thì lọc
                        if (searchQuery != null && !searchQuery.isEmpty()) {
                            filterProductsByQuery(searchQuery);
                        } else {
                            filteredList.addAll(fullList);
                            adapter.notifyDataSetChanged();
                        }
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
                            filteredList.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }


}

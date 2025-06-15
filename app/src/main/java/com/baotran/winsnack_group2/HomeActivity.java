package com.baotran.winsnack_group2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView bestSellerRecyclerView, recommendRecyclerView;
    private ProductAdapter bestSellerAdapter, recommendAdapter;
    private List<Product> productList;
    private DatabaseReference databaseReference;
    private EditText searchEditText;
    private LinearLayout searchContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("PRODUCT");

        // Initialize UI components
        searchEditText = findViewById(R.id.search_container).findViewById(android.R.id.edit);
        searchContainer = findViewById(R.id.search_container);
        bestSellerRecyclerView = new RecyclerView(this);
        recommendRecyclerView = new RecyclerView(this);

        // Setup RecyclerViews
        productList = new ArrayList<>();
        bestSellerAdapter = new ProductAdapter(productList);
        recommendAdapter = new ProductAdapter(productList);

        // Replace HorizontalScrollView with RecyclerViews
        LinearLayout bestSellerLayout = findViewById(R.id.best_seller_container);
        LinearLayout recommendLayout = findViewById(R.id.recommend_container);

        if (bestSellerLayout != null) {
            bestSellerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            bestSellerRecyclerView.setAdapter(bestSellerAdapter);
            bestSellerLayout.addView(bestSellerRecyclerView);
        }

        if (recommendLayout != null) {
            recommendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recommendRecyclerView.setAdapter(recommendAdapter);
            recommendLayout.addView(recommendRecyclerView);
        }

        // Load data from Firebase
        loadProducts();

        // Setup search functionality
        setupSearch();

        // Setup category buttons
        setupCategoryButtons();

        // Setup navigation buttons
        setupNavigationButtons();
    }

    private void loadProducts() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                bestSellerAdapter.notifyDataSetChanged();
                recommendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Failed to load products: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        ImageButton searchButton = searchContainer.findViewById(android.R.id.button1);
        searchButton.setOnClickListener(v -> {
            String query = searchEditText.getText().toString().trim();
            filterProducts(query);
        });
    }

    private void filterProducts(String query) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        bestSellerAdapter.updateList(filteredList);
        recommendAdapter.updateList(filteredList);
    }

    private void setupCategoryButtons() {
        int[] categoryIds = {
                R.id.mixed_category,
                R.id.grilled_category,
                R.id.sweet_category,
                R.id.combo_category,
                R.id.ingredients_category
        };

        String[] categories = {"Mixed", "Grilled", "Sweet", "Combo", "Ingredients"};

        for (int i = 0; i < categoryIds.length; i++) {
            LinearLayout categoryLayout = findViewById(categoryIds[i]);
            if (categoryLayout != null) {
                final String category = categories[i];
                categoryLayout.setOnClickListener(v -> filterByCategory(category));
            }
        }
    }

    private void filterByCategory(String category) {
        List<Product> filteredList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getCategoryID().equalsIgnoreCase(category)) {
                filteredList.add(product);
            }
        }
        bestSellerAdapter.updateList(filteredList);
        recommendAdapter.updateList(filteredList);
    }

    private void setupNavigationButtons() {
        ImageButton cartButton = findViewById(R.id.cart_button);
        ImageButton notificationButton = findViewById(R.id.notification_button);
        ImageButton profileButton = findViewById(R.id.profile_button);

        if (cartButton != null) {
            cartButton.setOnClickListener(v -> {
                // Navigate to Cart Activity
                Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show();
            });
        }

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                // Navigate to Notification Activity
                Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show();
            });
        }

        if (profileButton != null) {
            profileButton.setOnClickListener(v -> {
                // Navigate to Profile Activity
                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Product class to map Firebase data
    public static class Product {
        private String CategoryID;
        private String Description;
        private String Image;
        private double OriginalPrice;
        private double Price;
        private int ProductID;
        private String ProductName;
        private int Quantity;

        public Product() {
        }

        public String getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(String categoryID) {
            CategoryID = categoryID;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getImage() {
            return Image;
        }

        public void setImage(String image) {
            Image = image;
        }

        public double getOriginalPrice() {
            return OriginalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            OriginalPrice = originalPrice;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double price) {
            Price = price;
        }

        public int getProductID() {
            return ProductID;
        }

        public void setProductID(int productID) {
            ProductID = productID;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int quantity) {
            Quantity = quantity;
        }
    }

    // Adapter for RecyclerView
    private class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
        private List<Product> products;

        public ProductAdapter(List<Product> products) {
            this.products = products;
        }

        public void updateList(List<Product> newList) {
            products = new ArrayList<>(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_item, parent, false);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
            Product product = products.get(position);
            holder.productName.setText(product.getProductName());
            holder.productPrice.setText(String.format("$%.2f", product.getPrice()));
            Glide.with(HomeActivity.this)
                    .load(product.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.productImage);

            holder.itemView.setOnClickListener(v -> {
                // Navigate to Product Detail Activity
                Toast.makeText(HomeActivity.this, "Selected: " + product.getProductName(), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {
            ImageView productImage;
            TextView productPrice, productName;

            public ProductViewHolder(@NonNull View itemView) {
                super(itemView);
                productImage = itemView.findViewById(R.id.product_image);
                productPrice = itemView.findViewById(R.id.product_price);
                productName = itemView.findViewById(R.id.product_name);
            }
        }
    }
}
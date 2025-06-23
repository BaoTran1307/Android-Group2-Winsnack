package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import com.baotran.winsnack_group2.models.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView txtBrand, txtPrice, txtOriginalPrice, txtQuantity, txtDescription, txtSeeMore,
            txtCommentsTitle, txtCommentUser1, txtCommentDate1, txtCommentText1, txtCommentUser2,
            txtCommentDate2, txtCommentText2, txtAddComment, txtDiscount;
    private ImageView btnBack, imgProduct;
    private ImageButton btnDecrease, btnIncrease;
    private RatingBar ratingBar1, ratingBar2;
    private Button btnAddToCart;
    private int quantity = 1;
    private boolean isDescriptionExpanded = false;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        btnBack = findViewById(R.id.btn_back);
        txtBrand = findViewById(R.id.txtBrand);
        txtPrice = findViewById(R.id.txtPrice);
        txtOriginalPrice = findViewById(R.id.txtOriginalPrice);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtDescription = findViewById(R.id.txtDescription);
        txtSeeMore = findViewById(R.id.txtSeeMore);
        txtCommentsTitle = findViewById(R.id.txtCommentsTitle);
        txtCommentUser1 = findViewById(R.id.txtCommentUser1);
        txtCommentDate1 = findViewById(R.id.txtCommentDate1);
        txtCommentText1 = findViewById(R.id.txtCommentText1);
        txtCommentUser2 = findViewById(R.id.txtCommentUser2);
        txtCommentDate2 = findViewById(R.id.txtCommentDate2);
        txtCommentText2 = findViewById(R.id.txtCommentText2);
//        txtAddComment = findViewById(R.id.txtAddComment);
        imgProduct = findViewById(R.id.imgProduct);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        ratingBar1 = findViewById(R.id.ratingBar1);
        ratingBar2 = findViewById(R.id.ratingBar2);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        txtDiscount = findViewById(R.id.txtDiscount);

        // Get product data from Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            Object extra = intent.getSerializableExtra("product");
            if (extra instanceof Product) {
                product = (Product) extra;
                loadProductDetails();
            } else {
                Toast.makeText(this, "Invalid product data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Static comments for all products
        setupStaticComments();

        // Button actions
        btnBack.setOnClickListener(v -> onBackPressed());

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });

        btnIncrease.setOnClickListener(v -> {
            if (product != null && quantity < product.getQuantity()) {
                quantity++;
                txtQuantity.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Maximum quantity reached", Toast.LENGTH_SHORT).show();
            }
        });

        txtSeeMore.setOnClickListener(v -> {
            if (isDescriptionExpanded) {
                txtDescription.setText(product != null ? product.getDescription() : "");
                txtSeeMore.setText("SEE MORE");
                isDescriptionExpanded = false;
            } else {
                txtDescription.setText(product != null ? product.getDescription() + " This traditional Vietnamese snack combines crispy rice paper with savory shredded pork, seasoned with a perfect blend of spices for an unforgettable taste." : "");
                txtSeeMore.setText("SEE LESS");
                isDescriptionExpanded = true;
            }
        });
//
//        txtAddComment.setOnClickListener(v ->
//                Toast.makeText(this, "Add comment feature not implemented", Toast.LENGTH_SHORT).show()
//        );

        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                String productName = txtBrand.getText().toString();
                double price = Double.parseDouble(txtPrice.getText().toString().replace("$", ""));
                addToCart(product.getProductID(), productName, price, quantity);
            } else {
                Toast.makeText(this, "Cannot add to cart: Product not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProductDetails() {
        if (product == null) return;
        txtBrand.setText(product.getProductName());
        txtPrice.setText(String.format("$%.2f", product.getPrice()));
        txtOriginalPrice.setText(String.format("$%.2f", product.getOriginalPrice()));
        txtDescription.setText(product.getDescription());
        txtQuantity.setText(String.valueOf(quantity));
        double discount = ((product.getOriginalPrice() - product.getPrice()) / product.getOriginalPrice()) * 100;
        txtDiscount.setText(String.format("-%.0f%%", discount));
        if (ratingBar1 != null) ratingBar1.setRating((float) product.getRate());
        if (ratingBar2 != null) ratingBar2.setRating((float) product.getRate());

        // Load image using Glide
        Glide.with(this)
                .load(product.getImage())
                .override(300, 300)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(imgProduct);
    }

    private void setupStaticComments() {
        if (txtCommentUser1 != null && txtCommentDate1 != null && txtCommentText1 != null &&
                txtCommentUser2 != null && txtCommentDate2 != null && txtCommentText2 != null &&
                ratingBar1 != null && ratingBar2 != null) {
            txtCommentUser1.setText("John Doe");
            txtCommentDate1.setText("3 days ago");
            txtCommentText1.setText("Amazing snack! The spicy pork flavor is perfect and the rice paper is super crispy!");
            ratingBar1.setRating(5.0f);

            txtCommentUser2.setText("Jane Smith");
            txtCommentDate2.setText("5 days ago");
            txtCommentText2.setText("Really enjoyed this product. Great for a quick snack and the price is reasonable!");
            ratingBar2.setRating(4.8f);
        } else {
            Toast.makeText(this, "Error initializing comments section", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToCart(int productId, String productName, double price, int quantity) {
        FirebaseUser user = auth.getCurrentUser();
        String userId = user != null ? user.getUid() : "user_1";

        DocumentReference cartItemRef = db.collection("carts")
                .document(userId)
                .collection("items")
                .document(String.valueOf(productId));

        cartItemRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Update quantity
                long existingQuantity = documentSnapshot.getLong("quantity");
                cartItemRef.update("quantity", existingQuantity + quantity)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ProductDetailsActivity.this, "Updated quantity in cart", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProductDetailsActivity.this, "Error updating cart: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            } else {
                // Add new item
                Map<String, Object> cartItem = new HashMap<>();
                cartItem.put("productId", productId);
                cartItem.put("productName", productName);
                cartItem.put("price", price);
                cartItem.put("quantity", quantity);

                cartItemRef.set(cartItem)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ProductDetailsActivity.this, "Added " + quantity + " " + productName + " to cart", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProductDetailsActivity.this, "Error adding to cart: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ProductDetailsActivity.this, "Error checking cart: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
}
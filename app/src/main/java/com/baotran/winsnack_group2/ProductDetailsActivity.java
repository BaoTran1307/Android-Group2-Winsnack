//package com.baotran.winsnack_group2;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.RatingBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentTransaction;
//
//public class ProductDetailsActivity extends com.baotran.winsnack_group2.BaseActivity { // Thay AppCompatActivity bằng BaseActivity
//
//    private TextView txtBrand, txtPrice, txtOriginalPrice, txtQuantity, txtDescription, txtSeeMore,
//            txtCommentsTitle, txtCommentUser1, txtCommentDate1, txtCommentText1, txtCommentUser2, txtCommentDate2,
//            txtCommentText2, txtAddComment;
//    private ImageView btnBack, imgProduct;
//    private ImageButton btnDecrease, btnIncrease, btnHome, btnMenu, btnEvent, btnChatbot, btnOthers;
//    private RatingBar ratingBar1, ratingBar2;
//    private Button btnAddToCart;
//    private int quantity = 3;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Inflate layout của ProductDetailsActivity vào FrameLayout của BaseActivity
//        LayoutInflater inflater = getLayoutInflater();
//        View contentView = inflater.inflate(R.layout.activity_product_details, findViewById(R.id.content_frame), true);
//
//        // Initialize views
//        btnBack = findViewById(R.id.btn_back);
//        txtBrand = findViewById(R.id.txtBrand);
//        txtPrice = findViewById(R.id.txtPrice);
//        txtOriginalPrice = findViewById(R.id.txtOriginalPrice);
//        txtQuantity = findViewById(R.id.txtQuantity);
//        txtDescription = findViewById(R.id.txtDescription);
//        txtSeeMore = findViewById(R.id.txtSeeMore);
//        txtCommentsTitle = findViewById(R.id.txtCommentsTitle);
//        txtCommentUser1 = findViewById(R.id.txtCommentUser1);
//        txtCommentDate1 = findViewById(R.id.txtCommentDate1);
//        txtCommentText1 = findViewById(R.id.txtCommentText1);
//        txtCommentUser2 = findViewById(R.id.txtCommentUser2);
//        txtCommentDate2 = findViewById(R.id.txtCommentDate2);
//        txtCommentText2 = findViewById(R.id.txtCommentText2);
//        txtAddComment = findViewById(R.id.txtAddComment);
//        imgProduct = findViewById(R.id.imgProduct);
//        btnDecrease = findViewById(R.id.btnDecrease);
//        btnIncrease = findViewById(R.id.btnIncrease);
//        btnHome = findViewById(R.id.btnHome);
//        btnMenu = findViewById(R.id.btnMenu);
//        btnEvent = findViewById(R.id.btnEvent);
//        btnChatbot = findViewById(R.id.btnChatbot);
//        btnOthers = findViewById(R.id.btnOthers);
//        ratingBar1 = findViewById(R.id.ratingBar1);
//        ratingBar2 = findViewById(R.id.ratingBar2);
//        btnAddToCart = findViewById(R.id.btnAddToCart);
//
//        // Hardcode data
//        txtBrand.setText("Bánh tráng Chà bông");
//        txtPrice.setText("$14.00");
//        txtOriginalPrice.setText("$20.00");
//        txtQuantity.setText(String.valueOf(quantity));
//        txtDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore.");
//        txtCommentUser1.setText("Thank Tee");
//        ratingBar1.setRating(4.8f);
//        txtCommentDate1.setText("3 Ngày trước");
//        txtCommentText1.setText("Mình đã thử và thấy bao la lòng bạn nhé! Win Snack, bánh tráng rong biển, bánh tráng bơ.");
//        txtCommentUser2.setText("Thank Tee");
//        ratingBar2.setRating(4.8f);
//        txtCommentDate2.setText("3 Ngày trước");
//        txtCommentText2.setText("Mình đã thử và thấy bao la lòng bạn nhé! Win Snack, bánh tráng rong biển, bánh tráng bơ.");
//
//        // Button actions
//        btnBack.setOnClickListener(v -> onBackPressed());
//        btnDecrease.setOnClickListener(v -> {
//            if (quantity > 1) {
//                quantity--;
//                txtQuantity.setText(String.valueOf(quantity));
//            }
//        });
//        btnIncrease.setOnClickListener(v -> {
//            quantity++;
//            txtQuantity.setText(String.valueOf(quantity));
//        });
//        txtSeeMore.setOnClickListener(v -> Toast.makeText(this, "See more clicked", Toast.LENGTH_SHORT).show());
//        txtAddComment.setOnClickListener(v -> Toast.makeText(this, "Add comment clicked", Toast.LENGTH_SHORT).show());
//        btnAddToCart.setOnClickListener(v -> Toast.makeText(this, "Added to cart: " + quantity + " items", Toast.LENGTH_SHORT).show());
//        btnHome.setOnClickListener(v -> Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show());
//        btnMenu.setOnClickListener(v -> Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show());
//        btnEvent.setOnClickListener(v -> Toast.makeText(this, "Event clicked", Toast.LENGTH_SHORT).show());
//        btnOthers.setOnClickListener(v -> Toast.makeText(this, "Others clicked", Toast.LENGTH_SHORT).show());
//        btnChatbot.setOnClickListener(v -> Toast.makeText(this, "Chatbot clicked", Toast.LENGTH_SHORT).show());
//    }
//
//    /*
//    // Example to load data from database
//    private void loadProductFromDatabase() {
//        SQLiteConnector connector = new SQLiteConnector(this);
//        SQLiteDatabase database = connector.openDatabase();
//        Cursor cursor = database.rawQuery("SELECT * FROM Products WHERE Id = ?", new String[]{String.valueOf(productId)});
//        if (cursor != null && cursor.moveToFirst()) {
//            txtBrand.setText(cursor.getString(cursor.getColumnIndexOrThrow("Brand")));
//            txtPrice.setText("$" + cursor.getDouble(cursor.getColumnIndexOrThrow("Price")));
//            txtOriginalPrice.setText("$" + cursor.getDouble(cursor.getColumnIndexOrThrow("OriginalPrice")));
//            txtDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
//            cursor.close();
//        }
//        connector.closeDatabase();
//
//        // Load comments
//        Cursor commentCursor = database.rawQuery("SELECT * FROM Comments WHERE ProductId = ?", new String[]{String.valueOf(productId)});
//        if (commentCursor != null && commentCursor.moveToFirst()) {
//            txtCommentUser1.setText(commentCursor.getString(commentCursor.getColumnIndexOrThrow("UserName")));
//            ratingBar1.setRating(commentCursor.getFloat(commentCursor.getColumnIndexOrThrow("Rating")));
//            txtCommentDate1.setText(commentCursor.getString(commentCursor.getColumnIndexOrThrow("Date")));
//            txtCommentText1.setText(commentCursor.getString(commentCursor.getColumnIndexOrThrow("CommentText")));
//            if (commentCursor.moveToNext()) {
//                txtCommentUser2.setText(commentCursor.getString(commentCursor.getColumnIndexOrThrow("UserName")));
//                ratingBar2.setRating(commentCursor.getFloat(commentCursor.getColumnIndexOrThrow("Rating")));
//                txtCommentDate2.setText(commentCursor.getString(commentCursor.getColumnIndexOrThrow("Date")));
//                txtCommentText2.setText(commentCursor.getString(commentCursor.getColumnIndexOrThrow("CommentText")));
//            }
//            commentCursor.close();
//        }
//        connector.closeDatabase();
//    }
//    */
//    // Fragment placeholder (có thể bỏ nếu không dùng Fragment)
//    public static class ProductDetailsFragment extends androidx.fragment.app.Fragment {
//        public ProductDetailsFragment() {
//            super(R.layout.activity_product_details);
//        }
//    }
//}
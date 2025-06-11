//package com.baotran.winsnack_group2;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.baotran.winsnack_group2.R;
//
//public class BaseActivity extends AppCompatActivity {
//    private ImageView imgChatbot;
//    private ImageButton btnHome, btnMenu, btnChatbot, btnEvent, btnOthers;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
//
//        // Initialize views
//        imgChatbot = findViewById(R.id.imgChatbot);
//        btnHome = findViewById(R.id.btnHome);
//        btnMenu = findViewById(R.id.btnMenu);
//        btnChatbot = findViewById(R.id.btnChatbot);
//        btnEvent = findViewById(R.id.btnEvent);
//        btnOthers = findViewById(R.id.btnOthers);
//
//        // Set click listeners for common buttons
//        imgChatbot.setOnClickListener(v -> Toast.makeText(this, "Chatbot clicked", Toast.LENGTH_SHORT).show());
//        btnHome.setOnClickListener(v -> navigateToHome());
//        btnMenu.setOnClickListener(v -> navigateToMenu()); // Gán cho Page 1
//        btnChatbot.setOnClickListener(v -> navigateToChatbotPage()); // Gán cho Page 2
//        btnEvent.setOnClickListener(v -> navigateToEvent()); // Gán cho Page 3
//        btnOthers.setOnClickListener(v -> navigateToOthers());
//
//        // Load default fragment
//        if (savedInstanceState == null) {
//            loadDefaultFragment();
//        }
//    }
//
//    protected void loadDefaultFragment() {
//        Fragment fragment = new HomeFragment(); // Đặt Home làm mặc định
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.commit();
//    }
//
//    protected void navigateToHome() {
//        Fragment fragment = new HomeFragment();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    protected void navigateToMenu() {
//        Fragment fragment = new Page1Fragment(); // Ví dụ Page 1
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    protected void navigateToChatbotPage() {
//        Fragment fragment = new Page2Fragment(); // Ví dụ Page 2
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    protected void navigateToEvent() {
//        Fragment fragment = new Page3Fragment(); // Ví dụ Page 3
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//
//    protected void navigateToOthers() {
//        Fragment fragment = new OthersFragment(); // Ví dụ page khác
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_frame, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
//}
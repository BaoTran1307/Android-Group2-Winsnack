package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchWelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_welcome_screen);

        // Khởi tạo các nút (cần cập nhật ID trong XML)
        Button btnContinueAsGuest = findViewById(R.id.btn_continue_as_guest);
        Button btnLogIn = findViewById(R.id.btn_log_in);
        Button btnSignUp = findViewById(R.id.btn_sign_up);

        // Xử lý sự kiện nhấn nút
        btnContinueAsGuest.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchWelcomeScreenActivity.this, OnboardingActivity.class);
            startActivity(intent);
        });

        btnLogIn.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchWelcomeScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchWelcomeScreenActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }
}
package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchWelcomeScreenActivity extends AppCompatActivity {

    private static final String TAG = "LaunchWelcomeScreen"; // Tag để theo dõi log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_welcome_screen);
        Log.d(TAG, "onCreate: Welcome screen loaded");

        // Khởi tạo các nút
        TextView btnContinueAsGuest = findViewById(R.id.btn_continue_as_guest); // Sử dụng TextView
        Button btnLogIn = findViewById(R.id.btn_log_in);
        Button btnSignUp = findViewById(R.id.btn_sign_up);

        // Xử lý sự kiện nhấn nút
        btnContinueAsGuest.setOnClickListener(v -> {
            Log.d(TAG, "Continue as Guest clicked");
            Intent intent = new Intent(LaunchWelcomeScreenActivity.this, OnboardingActivity.class);
            startActivity(intent);
        });

        btnLogIn.setOnClickListener(v -> {
            Log.d(TAG, "Log In clicked");
            Intent intent = new Intent(LaunchWelcomeScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignUp.setOnClickListener(v -> {
            Log.d(TAG, "Sign Up clicked");
            Intent intent = new Intent(LaunchWelcomeScreenActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Welcome screen is starting");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Welcome screen is visible");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Welcome screen is pausing");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Welcome screen is stopping");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Welcome screen is destroyed");
    }
}
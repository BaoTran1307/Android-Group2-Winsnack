package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    private static final String TAG = "OnboardingActivity";
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding1);
        Log.d(TAG, "onCreate: Onboarding page 1 loaded");

        setupButtons();
    }

    private void setupButtons() {
        Button btnSkip = findViewById(R.id.btnSkip);
        Button btnNext = findViewById(R.id.btnNext);

        btnSkip.setOnClickListener(v -> {
            Log.d(TAG, "btnSkip clicked");
            Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        btnNext.setOnClickListener(v -> {
            Log.d(TAG, "btnNext clicked, currentPage: " + currentPage);
            if (currentPage == 1) {
                setContentView(R.layout.onboarding2);
                currentPage = 2;
            } else if (currentPage == 2) {
                setContentView(R.layout.onboarding3);
                currentPage = 3;
            } else if (currentPage == 3) {
                Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            setupButtons(); // Cập nhật lại button sau khi chuyển trang
        });
    }
}
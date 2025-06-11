package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchFirstScreenActivity extends AppCompatActivity {

    private static final String TAG = "LaunchFirstScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_first_screen);
        Log.d(TAG, "onCreate: Splash screen loaded");

        // Chuyển sang LaunchWelcomeScreenActivity sau 2 giây
        new Handler().postDelayed(() -> {
            try {
                Log.d(TAG, "Transitioning to LaunchWelcomeScreenActivity");
                Intent intent = new Intent(LaunchFirstScreenActivity.this, LaunchWelcomeScreenActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Error during transition: " + e.getMessage());
            }
        }, 2000);
    }
}
package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchFirstScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_first_screen);

        // Chuyển sang Launch B sau 2 giây
        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(LaunchFirstScreenActivity.this, LaunchWelcomeScreenActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}
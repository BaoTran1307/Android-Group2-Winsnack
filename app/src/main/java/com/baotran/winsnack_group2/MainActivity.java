package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Không set layout, chuyển ngay sang LaunchFirstScreenActivity
        Intent intent = new Intent(MainActivity.this, LaunchFirstScreenActivity.class);
        startActivity(intent);
        finish();
    }
}
package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baotran.winsnack_group2.R;

public abstract class FooterActivity extends AppCompatActivity {
    protected ImageView imgChatbot;
    protected ImageButton btnHome, btnMenu, btnChatbot, btnEvent, btnOthers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupFooter() {
        imgChatbot = findViewById(R.id.imgChatbot);
        btnHome = findViewById(R.id.btnHome);
        btnMenu = findViewById(R.id.btnMenu);
        btnChatbot = findViewById(R.id.btnChatbot);
        btnEvent = findViewById(R.id.btnEvent);
        btnOthers = findViewById(R.id.btnOthers);

        imgChatbot.setOnClickListener(v -> Toast.makeText(this, "Chatbot clicked", Toast.LENGTH_SHORT).show());
        btnHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
//        btnMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));
        btnChatbot.setOnClickListener(v -> startActivity(new Intent(this, ChatbotActivity.class)));
//        btnEvent.setOnClickListener(v -> startActivity(new Intent(this, EventActivity.class)));
//        btnOthers.setOnClickListener(v -> startActivity(new Intent(this, OtherActivity.class)));
    }
}
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

        if (imgChatbot != null) {
            imgChatbot.setOnClickListener(v -> {
                if (!(this instanceof ChatbotActivity)) {
                    startActivity(new Intent(this, ChatbotActivity.class));
                } else {
                    Toast.makeText(this, "Bạn đang ở trang Chatbot", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {
                if (!(this instanceof HomeActivity)) {
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    Toast.makeText(this, "Bạn đang ở trang Home", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (!(this instanceof CategoryActivity)) {
                    startActivity(new Intent(this, CategoryActivity.class));
                } else {
                    Toast.makeText(this, "Bạn đang ở trang Danh mục", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (btnChatbot != null) {
            btnChatbot.setOnClickListener(v -> {
                if (!(this instanceof ChatbotActivity)) {
                    startActivity(new Intent(this, ChatbotActivity.class));
                } else {
                    Toast.makeText(this, "Bạn đang ở trang Chatbot", Toast.LENGTH_SHORT).show();
                }
            });
        }

 if (btnEvent != null) {
     btnEvent.setOnClickListener(v -> {
         if (!(this instanceof EventActivity)) {
             startActivity(new Intent(this, EventActivity.class));
         } else {
             Toast.makeText(this, "Bạn đang ở trang Sự kiện", Toast.LENGTH_SHORT).show();
         }
     });
 }

 if (btnOthers != null) {
     btnOthers.setOnClickListener(v -> {
         if (!(this instanceof OtherPageActivity)) {
             startActivity(new Intent(this, OtherPageActivity.class));
         } else {
             Toast.makeText(this, "Bạn đang ở trang Khác", Toast.LENGTH_SHORT).show();
         }
     });
 }

    }
}
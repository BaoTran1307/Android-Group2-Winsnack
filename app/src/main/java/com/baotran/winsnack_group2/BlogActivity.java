package com.baotran.winsnack_group2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class BlogActivity extends AppCompatActivity {

    Button btnReadBlog;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog); // XML layout của trang blog

        btnBack = findViewById(R.id.btnBack);
        btnReadBlog = findViewById(R.id.btnReadBlog);

        // Xử lý khi bấm nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlogActivity.this, OtherPageActivity.class);
                startActivity(intent);
                finish(); // Đóng BlogActivity để tránh quay lại bằng nút back
            }
        });

        // Xử lý khi bấm nút Read Blog
        btnReadBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BlogActivity.this, ReadBlogActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.baotran.winsnack_group2;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ✅ Khai báo biến TextView đúng ID
        TextView aboutDesc = findViewById(R.id.aboutDescription);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aboutDesc.setText(Html.fromHtml(getString(R.string.about_us_description), Html.FROM_HTML_MODE_LEGACY));
        } else {
            aboutDesc.setText(Html.fromHtml(getString(R.string.about_us_description)));
        }
    }
}

package com.baotran.winsnack_group2;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baotran.winsnack_group2.adapter.ProductAdapter;
import com.baotran.winsnack_group2.models.Product;

import java.util.ArrayList;
import java.util.List;

public class RecommendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations); // XML layout bạn tự tạo

        recyclerView = findViewById(R.id.recyclerViewRecommend);
        ImageView btnBack = findViewById(R.id.btnBackRecommend);
        btnBack.setOnClickListener(v -> finish());

        // Lấy danh sách sản phẩm từ intent
        productList = (List<Product>) getIntent().getSerializableExtra("recommendList");
        if (productList == null) productList = new ArrayList<>();

        adapter = new ProductAdapter(this, productList, R.layout.item_product); // dùng layout giống BestSeller
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }
}

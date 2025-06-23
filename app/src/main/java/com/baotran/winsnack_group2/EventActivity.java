package com.baotran.winsnack_group2;

import android.os.Bundle;

public class EventActivity extends FooterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setupFooter();
    }
}

package com.king.project.activity;

import android.os.Bundle;

import com.king.project.R;

public class MyInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        changTitle("信息");
    }
}

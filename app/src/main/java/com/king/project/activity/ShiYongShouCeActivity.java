package com.king.project.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.king.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShiYongShouCeActivity extends BaseActivity {

    @BindView(R.id.web_content)
    WebView webContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shi_yong_shou_ce);
        ButterKnife.bind(this);
        changTitle("使用手册");
        init();
    }

    private void init(){
        webContent.loadUrl("http://www.baidu.com");

    }
}

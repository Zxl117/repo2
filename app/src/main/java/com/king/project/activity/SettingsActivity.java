package com.king.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.king.project.R;
import com.king.project.utils.ConstantValue;
import com.king.project.utils.SpUtil;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.lay_jilianglv)
    LinearLayout layJilianglv;
    @BindView(R.id.lay_jiliangleiji)
    LinearLayout layJiliangleiji;
    @BindView(R.id.lay_gongzuoshichang)
    LinearLayout layGongzuoshichang;
    @BindView(R.id.lay_moshi)
    LinearLayout layMoshi;
    @BindView(R.id.lay_kedu)
    LinearLayout layKedu;
    @BindView(R.id.cb_isscreenbright)
    CheckBox cb_isscreenbright;
    @BindView(R.id.btn_back)
    Button btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        changTitle("设置");

        init();

    }

    private void init() {
        layJilianglv.setOnClickListener(this);
        layGongzuoshichang.setOnClickListener(this);
        layJiliangleiji.setOnClickListener(this);
        layMoshi.setOnClickListener(this);
        layKedu.setOnClickListener(this);

        cb_isscreenbright.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.KEY_SCREEN_BRIGHT,b);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_jilianglv:
                startActivity(new Intent(this, Settings_BaoJingActivity.class).putExtra("tag", "1"));
                break;
            case R.id.lay_jiliangleiji:
                startActivity(new Intent(this, Settings_BaoJingActivity.class).putExtra("tag", "2"));
                break;
            case R.id.lay_gongzuoshichang:
                startActivity(new Intent(this, Settings_BaoJingActivity.class).putExtra("tag", "3"));
                break;
            case R.id.lay_moshi:
                startActivity(new Intent(this, Settings_GongZuoMoShiActivity.class));
                break;
            case R.id.lay_kedu:
                startActivity(new Intent(this, Settings_KeDuActivity.class));
                break;

        }

    }
}

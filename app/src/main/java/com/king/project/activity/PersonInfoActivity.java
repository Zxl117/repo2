package com.king.project.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.king.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.lay_info)
    LinearLayout layInfo;
    @BindView(R.id.lay_yijianfankui)
    LinearLayout layYijianfankui;
    @BindView(R.id.lay_shiyongshouce)
    LinearLayout layShiyongshouce;
    @BindView(R.id.lay_call)
    LinearLayout layCall;
    @BindView(R.id.lay_localdata)
    LinearLayout layLocaldata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        changTitle("我的");
        init();
    }

    private void init() {
        layInfo.setOnClickListener(this);
        layYijianfankui.setOnClickListener(this);
        layShiyongshouce.setOnClickListener(this);
        layCall.setOnClickListener(this);
        layLocaldata.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_info:
                startActivity(new Intent(this, MyInfoActivity.class));
                break;
            case R.id.lay_yijianfankui:
                startActivity(new Intent(this, YiJianFanKuiActivity.class));
                break;
            case R.id.lay_shiyongshouce:
                startActivity(new Intent(this, ShiYongShouCeActivity.class));
                break;
            case R.id.lay_call:
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + "10086");
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.lay_localdata:
                startActivity(new Intent(this, LocalDataActivity.class));
                break;

        }


    }
}

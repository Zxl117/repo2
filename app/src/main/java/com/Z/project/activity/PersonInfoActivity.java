package com.Z.project.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;

import com.Z.project.R;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SpUtil;

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
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    @BindView(R.id.iv_imghead)
    ImageView iv_imghead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        ButterKnife.bind(this);
        changTitle("我的");
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String nickname = SpUtil.getString(this, ConstantValue.KEY_NICKNAME,"");
        String  phone = SpUtil.getString(this,ConstantValue.KEY_PHONENUM,"15844866600");
        tv_phone.setText(phone);
        tv_nickname.setText(nickname);
        Drawable drawable=SpUtil.getDrawableByKey(this,ConstantValue.KEY_IMGHEAD);
        iv_imghead.setImageDrawable(drawable);
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

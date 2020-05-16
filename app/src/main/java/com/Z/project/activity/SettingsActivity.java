package com.Z.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.Z.project.R;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SpUtil;
import com.clj.fastble.data.BleDevice;


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
    private  BleDevice bleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        changTitle("设置");

        init();

    }

    private void init() {
        Intent intent =getIntent();
        bleDevice=intent.getParcelableExtra(MainActivity.KEY_DATA);
        layJilianglv.setOnClickListener(this);
        layGongzuoshichang.setOnClickListener(this);
        layJiliangleiji.setOnClickListener(this);
        layMoshi.setOnClickListener(this);
        layKedu.setOnClickListener(this);

        cb_isscreenbright.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕常亮
                }

                SpUtil.putBoolean(getApplicationContext(), ConstantValue.KEY_SCREEN_BRIGHT,b);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Bundle bundle=new Bundle();
        switch (v.getId()) {
            case R.id.lay_jilianglv:
                bundle.putString("tag", "1");
                bundle.putParcelable(MainActivity.KEY_DATA,bleDevice);
                startActivity(new Intent(this, Settings_BaoJingActivity.class).putExtras(bundle));
                break;
            case R.id.lay_jiliangleiji:
                bundle.putString("tag", "2");
                bundle.putParcelable(MainActivity.KEY_DATA,bleDevice);
                startActivity(new Intent(this, Settings_BaoJingActivity.class).putExtras(bundle));
                break;
            case R.id.lay_gongzuoshichang:
                bundle.putString("tag", "3");
                bundle.putParcelable(MainActivity.KEY_DATA,bleDevice);
                startActivity(new Intent(this, Settings_BaoJingActivity.class).putExtras(bundle));
                break;
            case R.id.lay_moshi:
                bundle.putParcelable(MainActivity.KEY_DATA,bleDevice);
                startActivity(new Intent(this, Settings_GongZuoMoShiActivity.class).putExtras(bundle));
                break;
            case R.id.lay_kedu:
                bundle.putParcelable(MainActivity.KEY_DATA,bleDevice);
                startActivity(new Intent(this, Settings_KeDuActivity.class).putExtras(bundle));
                break;

        }

    }
}

package com.Z.project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.Z.project.R;
import com.Z.project.operation.BleOperation;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SpUtil;
import com.clj.fastble.data.BleDevice;


import butterknife.BindView;
import butterknife.ButterKnife;

public class  Settings_GongZuoMoShiActivity extends BaseActivity {

    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.btn_ok)
    Button btn_ok;
    BleDevice bleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__gong_zuo_mo_shi);
        ButterKnife.bind(this);
        changTitle("设置");
        init();
    }

    private void init(){
        bleDevice=getIntent().getParcelableExtra(MainActivity.KEY_DATA);
        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb2.setChecked(false);
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb1.setChecked(false);
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BleOperation bleoperation=new BleOperation(bleDevice,Settings_GongZuoMoShiActivity.this, com.Z.project.operation.ConstantValue.Write);
                if(rb1.isChecked())  //剂量累加模式
                {
                    String leijia="A5A50100000000A5E5";
                    bleoperation.write(getApplicationContext(),leijia,"A5");
                    SpUtil.putBoolean(getApplicationContext(), ConstantValue.KEY_WORK_MODE,true);
                    finish();
                }
                else
                {
                    String qingling="A5A50000000000A5E5";
                    bleoperation.write(getApplicationContext(),qingling,"A5");
                    SpUtil.putBoolean(getApplicationContext(), ConstantValue.KEY_WORK_MODE,false);
                    finish();
                }
            }
        });

    }
}

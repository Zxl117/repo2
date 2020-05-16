package com.Z.project.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.Z.project.R;
import com.Z.project.operation.BleOperation;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SpUtil;
import com.clj.fastble.data.BleDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings_KeDuActivity extends BaseActivity {

    @BindView(R.id.btn_kd_ok)
    Button btn_kd_ok;
    @BindView(R.id.et_kdtext)
    EditText et_kdtext;
    @BindView(R.id.cb_kd)
    CheckBox cb_kd;
    @BindView(R.id.linelayout_set)
    LinearLayout llt_set;
    BleDevice bleDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__ke_du);
        ButterKnife.bind(this);
        changTitle("设置");
        init();
    }
    private void init() {
        bleDevice=getIntent().getParcelableExtra(MainActivity.KEY_DATA);
        cb_kd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b)
               {
                   llt_set.setVisibility(View.VISIBLE);
               }
               else
               {
                   llt_set.setVisibility(View.GONE);
               }
            }
        });

        btn_kd_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                BleOperation bleoperation=new BleOperation(bleDevice,Settings_KeDuActivity.this, com.Z.project.operation.ConstantValue.Write);
                bleoperation.write(getApplicationContext(),et_kdtext.getText().toString(),"A7");
                SpUtil.putString(getApplicationContext(), ConstantValue.KEY_SCALE_COEFFICIENT,et_kdtext.getText().toString());
                finish();
            }
        });
    }
}

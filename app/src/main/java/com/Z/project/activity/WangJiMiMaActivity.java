package com.Z.project.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.Z.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WangJiMiMaActivity extends BaseActivity {


    int timer = 120;
    Handler handler_time = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer--;
            if (timer == 0) {
                tvGetv.setClickable(true);
                tvGetv.setText("获取验证码");
                timer = 120;
            } else {
                handler_time.postDelayed(this, 1000);
                tvGetv.setText(timer + "秒后继续");
                tvGetv.setClickable(false);
            }
        }
    };
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_yanzhengma)
    EditText etYanzhengma;
    @BindView(R.id.tv_getv)
    TextView tvGetv;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_rpwd)
    EditText etRpwd;
    @BindView(R.id.btn_zhuce)
    Button btnZhuce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangjimima);
        ButterKnife.bind(this);

        HiddenLine();
        init();
    }


    private void init() {


        btnZhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
            }
        });

        tvGetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler_time.postDelayed(runnable, 0x3e8);

            }
        });


    }


}

package com.Z.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.Z.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_wangji)
    TextView tvWangji;
    @BindView(R.id.tv_zhuce)
    TextView tvZhuce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        HiddenBtnBack();
        HiddenLine();

        init();

    }


    private void init(){



        btnLogin.setOnClickListener(this);
        tvWangji.setOnClickListener(this);
        tvZhuce.setOnClickListener(this);





    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.btn_login:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.tv_zhuce:
                startActivity(new Intent(this,ZhuCe2Activity.class));
                break;
            case R.id.tv_wangji:
                startActivity(new Intent(this,WangJiMiMaActivity.class));
                break;

        }


    }
}

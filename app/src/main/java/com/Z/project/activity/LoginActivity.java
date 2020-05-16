package com.Z.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Z.project.R;
import com.Z.project.database.User;
import com.Z.project.database.UserService;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_wangji)
    TextView tvWangji;
    @BindView(R.id.tv_zhuce)
    TextView tvZhuce;
    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.et_pwd)
    EditText et_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if(SpUtil.getBoolean(getApplicationContext(), ConstantValue.KEY_LOGIIN,false))
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
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
               login();
               // startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.tv_zhuce:
                startActivity(new Intent(this,ZhuCe2Activity.class));
                break;
            case R.id.tv_wangji:
                startActivity(new Intent(this,WangJiMiMaActivity.class));
                break;
        }
    }

    private void login() {
        User user=new User();
        String name =et_phone.getText().toString().trim();
        String password=et_pwd.getText().toString().trim();
        user.setAccount(name);
        user.setPassword(password);
        UserService service=new UserService(getApplicationContext());
       if (service.login(name,password,getApplicationContext()))
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
       else
       {
           Toast.makeText(getApplicationContext(),"账号或密码错误请重新登录",Toast.LENGTH_SHORT).show();
       }
    }
}

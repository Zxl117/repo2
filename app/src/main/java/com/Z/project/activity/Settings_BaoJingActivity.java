package com.Z.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Z.project.R;
import com.Z.project.operation.BleOperation;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SendMsg2Byte;
import com.Z.project.utils.SpUtil;
import com.clj.fastble.data.BleDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings_BaoJingActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.lay_shichang)
    LinearLayout layShichang;
    @BindView(R.id.lay_comm)
    LinearLayout layComm;
    @BindView(R.id.tv_p)
    TextView tvP;
    @BindView(R.id.btn_certain)
    Button btn_certain;
    @BindView(R.id.cb_setswitch)
    CheckBox cb_setswich;
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.et_hour)
    EditText et_hour;
    @BindView(R.id.et_second)
    EditText et_second;
    @BindView(R.id.et_minute)
    EditText et_minute;
    @BindView(R.id.btn_back)
    Button back;
    private String option; //显示的类别
    private Spinner spinner;
    private BleDevice bleDevice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings__bao_jing);
        ButterKnife.bind(this);
        changTitle("设置");
        init();
    }

    private void init() {
        option = getIntent().getStringExtra("tag");
        bleDevice=getIntent().getParcelableExtra(MainActivity.KEY_DATA);
        spinner = findViewById(R.id.id_spinner);
        String [] doesUnit=new String[]{ "μSv/h","mSv/h","Sv/h"};
        //String [] adddoes=new String[]{"μSv","mSv","Sv"};
        ArrayAdapter<String>adater =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, doesUnit);
        //ArrayAdapter<String>adateradd =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, adddoes);
        spinner.setAdapter(adater);
        cb_setswich.setOnClickListener(this);
        btn_certain.setOnClickListener(this);

        switch (option) {
            case "1":
                tvTag.setText("剂量率");
                setPara();
                break;
            case "2":
                tvTag.setText("累积剂量");
                setPara();
                break;
            case "3":
                tvTag.setText("工作时长");
                layShichang.setVisibility(View.GONE);
                tvP.setVisibility(View.GONE);
                setPara();
                break;
        }
    }

    /**
     * 设置参数给设备
     */
    private void setPara()
    {
        //1.字符串转byte数组；
         HexString();
        //2.按照协议格式发送数据；

    }

    /**
     * 字符串转字节数组
     */
    private void HexString() {


    }

    @Override
    public void onClick(View view)
    {
      switch (view.getId())
      {
          case R.id.cb_setswitch:
              goTO_AlarmPara(cb_setswich.isChecked());
              break;
          case R.id.btn_certain:
              setting_AlarmPara();
              break;
      }
    }

    /**
     * 设置报警阈值
     */
    private void setting_AlarmPara()
    {
        BleOperation bleoperation=new BleOperation(bleDevice,Settings_BaoJingActivity.this, com.Z.project.operation.ConstantValue.Write);
      switch (option)
      {
            case "1": //剂量率
                String doseRate=et_input.getText().toString()+(String)spinner.getSelectedItem();
                bleoperation.write(getApplicationContext(),et_input.getText().toString(),"A1");
                saveBaoJingValue(ConstantValue.KEY_DOSERATE,doseRate);
              break;
            case "2":  //累计剂量
                String dose=et_input.getText().toString()+(String)spinner.getSelectedItem();
               // SendMsg2Byte.sendMsg(dose,"A2");
                bleoperation.write(getApplicationContext(),et_input.getText().toString(),"A2");
                 saveBaoJingValue(ConstantValue.KEY_CUMULATIVE_DOSE,dose);
              break;
             case "3":  //工作时长
                 String hour=et_hour.getText().toString();
                 String minute=et_minute.getText().toString();
                 String second = et_second.getText().toString();
                 String time=hour+minute+second;
                // SendMsg2Byte.sendMsgT(hour,minute,second,"A3");
                 bleoperation.writeTime(getApplicationContext(),hour,minute,second,"A3");
                 saveBaoJingValue(ConstantValue.KEY_WORK_TIME,time);
      }
    }

    /**
     * 保存报警阈值到sp中，并返回设置界面
     * @param key 存储节点的名称
     * @param value  存储的值
     */
       private void saveBaoJingValue(String key ,String value) {
           if(!TextUtils.isEmpty(et_input.getText())||option.equals("3"))
           {
               SpUtil.putString(this, key, value);
               finish();
           }
           else
           {
               Toast.makeText(this,"请输入值，不能为空！",Toast.LENGTH_SHORT).show();
           }
       }

    /**
     * 前往设置报警参数
     */
    private void goTO_AlarmPara(boolean ischeck)
    {
        if(ischeck)
        {
           switch (option)
           {
               case "1":
                   btn_certain.setVisibility(View.VISIBLE);
                   tvP.setVisibility(View.VISIBLE);
                   layComm.setVisibility(View.VISIBLE);
                   break;
               case "2":
                   btn_certain.setVisibility(View.VISIBLE);
                   tvP.setVisibility(View.VISIBLE);
                   layComm.setVisibility(View.VISIBLE);
                   break;
               case"3":
                   layShichang.setVisibility(View.VISIBLE);
                   btn_certain.setVisibility(View.VISIBLE);
                   break;
           }
        }
        else
        {
            et_hour.setText("");
            et_second.setText("");
            et_minute.setText("");
            et_input.setText("");
            layShichang.setVisibility(View.GONE);
            layComm.setVisibility(View.GONE);
            tvP.setVisibility(View.GONE);
            btn_certain.setVisibility(View.GONE);
        }

    }


}

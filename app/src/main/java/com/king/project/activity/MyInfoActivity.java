package com.king.project.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.project.R;
import com.king.project.utils.ConstantValue;
import com.king.project.utils.SpUtil;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener
{

    @BindView(R.id.ll_headimg)
    LinearLayout ll_headimg;
    @BindView(R.id.ll_nickname)
    LinearLayout ll_nickname;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    @BindView(R.id.ll_birthday)
    LinearLayout ll_birthday;
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    int year; //今年
    int month; //当前月份
    int dayOfMonth; //今天

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        changTitle("信息");
        initUI();
    }

    private void initUI() {
        ButterKnife.bind(this);
        ll_headimg.setOnClickListener(this);
        ll_nickname.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        ll_birthday.setOnClickListener(this);
        ll_phone.setOnClickListener(this);


        //存储图片没有写
        String nickname=SpUtil.getString(this,ConstantValue.KEY_NICKNAME,"");
        String sex=SpUtil.getString(this,ConstantValue.KEY_SEX,"男");
        String birthday =SpUtil.getString(this,ConstantValue.KEY_BIRTHDAY,"");
        String phone =SpUtil.getString(this,ConstantValue.KEY_PHONENUM,"");
        tv_nickname.setText(nickname);
        tv_sex.setText(sex);
        tv_birthday.setText(birthday);
        tv_phone.setText(phone);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_headimg:
                break;
            case R.id.ll_sex:
                //弹出对话框
                showSingleDialog();
                break;
            case R.id.ll_phone:
                break;
            case R.id.ll_birthday:
                //获取日历
                dateDialogShow();
                break;
            case R.id.ll_nickname:
                break;
        }
    }
    private void dateDialogShow() {
        Calendar calendar =Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
       DatePickerDialog datePickerDialog=new DatePickerDialog(this,R.style.DateTime,null,year,month,dayOfMonth);
       datePickerDialog.setCancelable(true);
       datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               //确定的逻辑代码在监听中实现
               DatePicker picker = datePickerDialog.getDatePicker();
                year = picker.getYear();
                 month = picker.getMonth();
                 dayOfMonth = picker.getDayOfMonth();
                 SpUtil.putString(getApplicationContext(),ConstantValue.KEY_BIRTHDAY,year+"-"+(month+1)+"-"+dayOfMonth);
                  tv_birthday.setText(year+"-"+(month+1)+"-"+dayOfMonth);
           }
       });
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消什么也不用做
                    }
                });

       datePickerDialog.show();
    }

    private void showSingleDialog() {
        final String []item ={"男","女"};
        AlertDialog.Builder singlesexchoice=new AlertDialog.Builder(this);
        singlesexchoice.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                 tv_sex.setText(item[i]);
                dialogInterface.dismiss();
            }
        });
        singlesexchoice.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        singlesexchoice.show();
    }
}

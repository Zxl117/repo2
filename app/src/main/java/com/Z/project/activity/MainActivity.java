package com.Z.project.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.clj.fastble.BleManager;

import com.clj.fastble.data.BleDevice;
import com.Z.project.R;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import java.util.List;



import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    //绑定控件

    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_staus)
    TextView tvStaus;
    @BindView(R.id.btn_Conn)
    Button btnConn;
    @BindView(R.id.img_conn)
    ImageView imgConn;
    @BindView(R.id.lay_daiceshi)
    LinearLayout layDaiceshi;
    @BindView(R.id.lay_ceshizhong)
    LinearLayout layCeshizhong;
    @BindView(R.id.lay_zanting)
    LinearLayout layZanting;
    @BindView(R.id.btn_zanting)
    LinearLayout btnZanting;
    @BindView(R.id.btn_jisxu)
    LinearLayout btnJisxu;
    @BindView(R.id.btn_jieshu)
    LinearLayout btnJieshu;
    @BindView(R.id.tv_jiliang)
    TextView txt;
    @BindView(R.id.iv_map)
    ImageView ivmap;


    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    private static final int UPDATE_DOSERATE=3;
    private static final int UPDATE_SUMDOSE=4;
    private static final int UPDATE_MAXDOSE=5;
    private static final int UPDATE_TIME=6;

    private int seconds=0;    //秒
    private boolean running=false;  //时间是否要更新
    private float addDose;           //累积剂量
    private float doseRate;          //剂量率
    private float maxDoseRate;




    /**
     * 返回device
     */
    public static final String KEY_DATA = "key_data";
    private BleDevice bleDevice;


    private int flag = -1;//-1 带连接蓝牙 0 待测试 1 测试中 2 测试暂停
    private Handler mHandler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        ButterKnife.bind(this);
        HiddenBtnBack();
        init();  //初始化数据

    }

    @Override
    protected void onResume() {
        checkPermissions();
        super.onResume();
    }

    private void init() {

        layDaiceshi.setVisibility(View.VISIBLE);
        layCeshizhong.setVisibility(View.GONE);
        layZanting.setVisibility(View.GONE);
        //左右按钮显示和赋予图标
        imgLeft.setVisibility(View.VISIBLE);
        imgRight.setVisibility(View.VISIBLE);

        imgLeft.setImageResource(R.mipmap.caidan);

        imgRight.setImageResource(R.mipmap.shezhi);

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,SettingsActivity.class));

            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,PersonInfoActivity.class));

            }
        });


        btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == -1) {
                    checkPermissions();
                    Intent intent = new Intent(MainActivity.this, SheBeiListActivity.class);
                    startActivityForResult(intent, 101);
                } else if (flag == 0) {
                    layDaiceshi.setVisibility(View.GONE);
                    layCeshizhong.setVisibility(View.VISIBLE);   running=true;
                    layZanting.setVisibility(View.GONE);
                    initData();
                }
//                else if (flag == 1) {
//                    layDaiceshi.setVisibility(View.GONE);
//                    layCeshizhong.setVisibility(View.GONE);
//                    layZanting.setVisibility(View.VISIBLE);
//                }else if (flag == 2) {
//                    layDaiceshi.setVisibility(View.GONE);
//                    layCeshizhong.setVisibility(View.VISIBLE);
//                    layZanting.setVisibility(View.GONE);
//                }


            }
        });


        btnZanting.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                layDaiceshi.setVisibility(View.GONE);
                layCeshizhong.setVisibility(View.GONE);
                layZanting.setVisibility(View.VISIBLE);
                running =false;        //测量时间暂停
                return false;
            }

        });

        btnJisxu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layDaiceshi.setVisibility(View.GONE);
                layCeshizhong.setVisibility(View.VISIBLE);
                layZanting.setVisibility(View.GONE);         running =true;       // 剂量时间开始计时

            }
        });


        btnJieshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layDaiceshi.setVisibility(View.VISIBLE);
                layCeshizhong.setVisibility(View.GONE);
                layZanting.setVisibility(View.GONE);
                flag = -1;
                btnConn.setText("搜索设备");
                running =false ;
                seconds=0;
            }
        });

    ivmap.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(getApplicationContext(),MapActivity.class);

            startActivity(intent);
        }
    });
    }



    /**
     * 显示数据到界面上。
     */
    private void initData() {
        showJiliang();
        showTime();
        showMaxjilaing();
        showAddjiliang();

    }

    /**
     * 更新实时剂量率
     */
    private void showJiliang() {
//        BleOperation bleOperation=new BleOperation(bleDevice,MainActivity.this);
//        //  bleOperation.notify(MainActivity.this,txt);
//        addDose= (int) bleOperation.read(MainActivity.this,txt);
        runData(UPDATE_DOSERATE);
    }
    /**
     * 显示累计剂量
     */
    private void showAddjiliang()
    {

        runData(UPDATE_SUMDOSE);


    }

    /**
     * 显示最大剂量率
     */
    private void showMaxjilaing()
    {
        runData(UPDATE_MAXDOSE);

    }

    /**
     * 显示测量时间
     */
    private void showTime() {

        runData(UPDATE_TIME);

    }

    /**
     * 刷新时间
     */
    private void runData(final int choseWay )
    {

        mHandler.post(new Runnable() {

            @Override

            public void run() {
                doseRate= (float) (Math.random()*2);
                switch (choseWay) {
                 case UPDATE_TIME:
                 int hours = seconds / 3600;
                 int minutes = (seconds % 3600) / 60;
                 int secs = seconds % 60;
                 String time = String.format("%02d:%02d:%02d", hours, minutes, secs);

                 if (running) {
                     seconds++;
                 }

                 ((TextView) findViewById(R.id.id_time)).setText(time);
                 mHandler.postDelayed(this, 1000);
                 break;
                 case UPDATE_DOSERATE:

                     ((TextView) findViewById(R.id.tv_jiliang)).setText(String.format("%.1f",doseRate));
                     mHandler.postDelayed(this, 2000);
                 break;

                 case UPDATE_MAXDOSE:
                     if(maxDoseRate<=doseRate)
                         maxDoseRate=doseRate;
                     ((TextView) findViewById(R.id.tv_MaxDoseRate)).setText(String.format("%.1f",maxDoseRate));
                     mHandler.postDelayed(this, 2000);
                     break;
                 case UPDATE_SUMDOSE:
                     addDose+=(doseRate/3600);
                     ((TextView) findViewById(R.id.tv_LeijiDose)).setText(String.format("%.2f",addDose)+"μSv");
                     mHandler.postDelayed(this, 2000);

                     break;

             }
            }
        });

    }

     private  String  maxValue (List <Float> list,float value)
     {
              if(list.get(0)<value)
              {
               list.remove(0);
              }

               return  null;
     }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 101&&requestCode==101) {
                bleDevice=data.getParcelableExtra(KEY_DATA);
                tvStaus.setText("已连接" + bleDevice.getName().toString());
                btnConn.setText("开始测量");
                flag = 0;
                imgConn.setVisibility(View.VISIBLE);
            }
            if (requestCode == REQUEST_CODE_OPEN_GPS) {
                if (checkGPSIsOpen()) {

                }
            }

        } catch (Exception e) {

        }
    }


    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "请先打开蓝牙", Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    /**
     * 检查权限
     */
    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {

                  //  startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


}

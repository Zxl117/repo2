package com.Z.project.activity;

import android.Manifest;
import android.location.LocationManager;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.Z.project.database.Realtime_data;
import com.Z.project.operation.BleOperation;
import com.Z.project.operation.ConstantValue;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.clj.fastble.BleManager;

import com.clj.fastble.data.BleDevice;
import com.Z.project.R;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private static final String TAG ="MainActivity" ;

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
    @BindView(R.id.btn_jixu)
    LinearLayout btnJixu;
    @BindView(R.id.btn_jieshu)
    LinearLayout btnJieshu;
    @BindView(R.id.tv_jiliang)
    TextView txt;
    @BindView(R.id.iv_map)
    ImageView ivmap;
    @BindView(R.id.tv_does)
    TextView tvdoes;
    @BindView(R.id.lay_map)
    LinearLayout laymap;
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;


    private MapView mMapView = null;    // 定义百度地图组件
    private BaiduMap mBaiduMap;         // 定义百度地图对象
    private LocationClient mLocationClient;     //定义LocationClient
    private BDLocationListener myListener = new MyLocationListener();  //定义位置监听
    private boolean isFirstLoc = true;  //定义第一次启动
    private MyLocationConfiguration.LocationMode mCurrentMode;  //定义当前定位模式
    private Marker mk;
    private double mlatitude = 0.0d;
    private double mlongitude = 0.0d;
    private TextView mGuide;
    private BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
    ImageView iv_waypoint;
    public static MyLocationData locData;
    private LatLng ll;
    private static LatLng ll2;
    InfoWindow mInfoWindow;


    private Handler mHandler = new Handler();
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    private static final int UPDATE_DOSERATE = 3;
    private static final int UPDATE_DOSERATE_INMAP = 7;
    private static final int UPDATE_DOSE = 4;
    private static final int UPDATE_MAXDOSE = 5;
    private static final int UPDATE_TIME = 6;

    private int seconds = 0; //秒
    private boolean running = false;  //时间是否要更新
    private float Dose;       //累积剂量
    private float doseRate;    //剂量率
    private float maxDoseRate;
    private boolean isMapEnable = false;
    private Realtime_data realtime_data;

    /**
     * 返回device
     */
    public static final String KEY_DATA = "key_data";
    private BleDevice bleDevice;


    private int flag = -1;//-1 待连接蓝牙 0 待测试 1 测试中 2 测试暂停 3。gps模式

    private int CurrentIntervalStageOne = 5; //第一阶段5秒
    private int CurrentIntervalStageTwo = 5;  //第二阶段5秒
    private BleOperation bleOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }
        BleManager.getInstance().init(getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setConnectOverTime(20000)
                .setOperateTimeout(5000);

        ButterKnife.bind(this);
        HiddenBtnBack();
        init();  //初始化数据
        initMap();
    }

    private void initMap() {
        isMapEnable = true;
        mMapView = (MapView) findViewById(R.id.bmapView);
        iv_waypoint = findViewById(R.id.iv_waypoint);
        mBaiduMap = mMapView.getMap();
        mLocationClient = new LocationClient(getApplicationContext());  //创建LocationClient类
        mLocationClient.registerLocationListener(myListener);   //注册监听函数
    }

    @Override
    protected void onResume() {
        checkPermissions();
        if (isMapEnable)
            mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isMapEnable) mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (isMapEnable) {
            mMapView.onDestroy();
            mLocationClient.stop();
            mBaiduMap.setMyLocationEnabled(false);
            mMapView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
    }

    private void init() {

        layDaiceshi.setVisibility(View.VISIBLE);
        layCeshizhong.setVisibility(View.GONE);
        layZanting.setVisibility(View.GONE);
        laymap.setVisibility(View.GONE);

        //左右按钮显示和赋予图标
        imgLeft.setVisibility(View.VISIBLE);
        imgRight.setVisibility(View.VISIBLE);

        imgLeft.setImageResource(R.mipmap.caidan);

        imgRight.setImageResource(R.mipmap.shezhi);

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra(KEY_DATA, bleDevice);
                startActivity(intent);

            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, PersonInfoActivity.class));

            }
        });


        btnConn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == -1) {
                    checkPermissions();
                    if (BleManager.getInstance().isConnected(bleDevice)) {
                        BleManager.getInstance().disconnect(bleDevice);
                    }
                    Intent intent = new Intent(MainActivity.this, SheBeiListActivity.class);
                    startActivityForResult(intent, 101);
                } else if (flag == 0) {
                    layDaiceshi.setVisibility(View.GONE);
                    layCeshizhong.setVisibility(View.VISIBLE);
                    running = true;
                    layZanting.setVisibility(View.GONE);
                    laymap.setVisibility(View.GONE);





                    initLocation();
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
                laymap.setVisibility(View.GONE);
                layZanting.setVisibility(View.VISIBLE);
                running = false;        //测量时间暂停
                return false;
            }

        });

        btnJixu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layDaiceshi.setVisibility(View.GONE);
                layCeshizhong.setVisibility(View.VISIBLE);
                layZanting.setVisibility(View.GONE);
                laymap.setVisibility(View.GONE);
                running = true;       // 剂量时间开始计时


            }
        });


        btnJieshu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layDaiceshi.setVisibility(View.VISIBLE);
                layCeshizhong.setVisibility(View.GONE);
                layZanting.setVisibility(View.GONE);
                flag = -1;
                btnConn.setText("搜索设备");
                tvStaus.setText("设备未连接");
                //蓝牙断开
                if (BleManager.getInstance().isConnected(bleDevice) && bleDevice != null) {
                    BleManager.getInstance().disconnect(bleDevice);
                }
                ///1.显示地图界面，报表数据。2.时间节点、地理位置存入表中。
                running = false;
                seconds = 0;
                return false;
            }
        });

        ivmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layDaiceshi.setVisibility(View.GONE);
                layCeshizhong.setVisibility(View.GONE);
                layZanting.setVisibility(View.GONE);
                laymap.setVisibility(View.VISIBLE);
                rl_title.setVisibility(View.GONE);
                flag = 3;
                initMapData();
               // runData(UPDATE_DOSERATE_INMAP);
            }
        });
    }

    public void On_Back_Celiang(View v) {
        rl_title.setVisibility(View.VISIBLE);
        layDaiceshi.setVisibility(View.GONE);
        layCeshizhong.setVisibility(View.VISIBLE);
        layZanting.setVisibility(View.GONE);
        laymap.setVisibility(View.GONE);
    }


    private void initMapData() {
        runDoes();
    }

    private void runDoes() {
        // bleOperation.notify(this,tvdoes);
    }

    /**
     * 显示数据到界面上
     */
    private void initData() {
        showDoesRate();
        showTime();

    }

    /**
     * 更新实时剂量率
     */
    private void showDoesRate() {
        runData(UPDATE_DOSERATE );
        //Log.d(TAG,"纬度"+ locData.latitude);
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
    private void runData(final int choseWay) {

        mHandler.post(new Runnable() {

            @Override
            public void run() {

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
                        if (bleOperation == null) {
                            bleOperation = new BleOperation(bleDevice,MainActivity.this, ConstantValue.Notify);
                        }
                        TextView tv_LeijiDose=findViewById(R.id.tv_LeijiDose);
                        TextView tv_MaxDoseRate=findViewById(R.id.tv_MaxDoseRate);
                        doseRate = bleOperation.notify(MainActivity.this,txt,tvdoes,tv_LeijiDose,tv_MaxDoseRate);
                        break;

                }
            }
        });

    }

    private String maxValue(List<Float> list, float value) {
        if (list.get(0) < value) {
            list.remove(0);
        }

        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 101 && requestCode == 101) {
                bleDevice = data.getParcelableExtra(KEY_DATA);
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

    //
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //销毁后不再处理新接收的位置
            if (location == null )
                return;
            // 构造定位数据
            locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            // 设置定位数据
            ll2 = new LatLng(location.getLatitude(), location.getLongitude());
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {  //如果是第一次定位,就定位到以自己为中心
                //获取用户当前经纬度
                ll = new LatLng(location.getLatitude(), location.getLongitude());
                mlatitude=ll.latitude;
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);  //更新坐标位置
                mBaiduMap.animateMapStatus(u);                            //设置地图位置
                isFirstLoc = false;                                      //取消第一次定位
            }
        }
    }

    private void initLocation() {
        //该方法实现初始化定位
        //创建LocationClientOption对象，用于设置定位方式
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");  //设置坐标类型
        option.setScanSpan(1000);      //1秒定位一次
        option.setOpenGps(true);      //打开GPS
        mLocationClient.setLocOption(option);  //保存定位参数与信息
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;  //设置定位模式
        //设置自定义定位图标
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        //位置构造方式，将定位模式，定义图标添加其中
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfigeration(config);  //地图显示定位图标
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                if (null != mk) {
//                    mk.remove();
//                }
                mlatitude = latLng.latitude;
                mlongitude = latLng.longitude;

                MarkerOptions ooA = new MarkerOptions().position(latLng).icon(bd).zIndex(9).draggable(true);
                mk = (Marker) mBaiduMap.addOverlay(ooA);
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
        iv_waypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarkerOptions ooA = new MarkerOptions().position(ll2).icon(bd).zIndex(9).draggable(true);
                mk = (Marker) mBaiduMap.addOverlay(ooA);
                //用来构造InfoWindow的Button
                Button button = new Button(getApplicationContext());
                button.setText("纬度:" + ll2.latitude + "  经度:" + ll2.longitude);

                //构造InfoWindow
                //point 描述的位置点
                //-100 InfoWindow相对于point在y轴的偏移量
                mInfoWindow = new InfoWindow(button, ll2, -100);
                //使InfoWindow生效
                mBaiduMap.showInfoWindow(mInfoWindow);
            }
        });
    }


}

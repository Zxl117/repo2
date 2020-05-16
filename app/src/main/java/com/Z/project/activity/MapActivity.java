package com.Z.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.Z.project.R;
import com.clj.fastble.data.BleDevice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity{
    @BindView(R.id.tv_does)
    TextView  tvdoes;
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
    private MyLocationData locData;
    private LatLng ll;
    private LatLng ll2;
    InfoWindow mInfoWindow;
    Handler mHandler =new Handler() ;
    BleDevice bleDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.bmapView);
        iv_waypoint = findViewById(R.id.iv_waypoint);
        mBaiduMap = mMapView.getMap();
        mLocationClient = new LocationClient(getApplicationContext());  //创建LocationClient类
        mLocationClient.registerLocationListener(myListener);   //注册监听函数
        ButterKnife.bind(this);
        initdata();
        initLocation();
    }

    /**
     * 初始化数据
     */
    private void initdata() {

        runDoes();
    }

    /**
     * 实时更新剂量率
     */
    private void runDoes()
    {
//        mHandler.post(new Runnable() {
////            @Override
////            public void run()
////            {
////                tvdoes.setText(String.format("%.1f",0));
////                mHandler.postDelayed(this,+2000);
////            }
////        });
        bleDevice=getIntent().getParcelableExtra(MainActivity.KEY_DATA);
        BleOperation bleOperation=new BleOperation(bleDevice,MapActivity.this, ConstantValue.Notify);
      // bleOperation.notify(MapActivity.this,tvdoes);
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
        mBaiduMap.setMyLocationConfiguration(config);  //地图显示定位图标
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

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //销毁后不再处理新接收的位置
            if (location == null || mMapView == null)
                return;
            // 构造定位数据
            locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            // 设置定位数据
              ll2=new LatLng(location.getLatitude(), location.getLongitude());
              mBaiduMap.setMyLocationData(locData);
              if (isFirstLoc) {  //如果是第一次定位,就定位到以自己为中心
                //获取用户当前经纬度
                ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);  //更新坐标位置
                mBaiduMap.animateMapStatus(u);                            //设置地图位置
                isFirstLoc = false;                                      //取消第一次定位
            }
        }
    }

    @Override
    protected void onStart() {  //地图定位与Activity生命周期绑定
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
    }
    @Override
    protected void onStop() {  //停止地图定位
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

}

package com.Z.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.Z.project.R;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Record_mapActivity extends AppCompatActivity {

    @BindView(R.id.ivStar1)
    ImageView ivStar1;
    @BindView(R.id.ivStar2)
    ImageView ivStar2;
    @BindView(R.id.ivStar3)
    ImageView ivStar3;
    @BindView(R.id.tvResult)
    TextView tvResult;
    @BindView(R.id.tvDistancet)
    TextView tvDistancet;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvCalorie)
    TextView tvCalorie;
    @BindView(R.id.mapView)
    MapView mapView;
    //运动计算相关

    //地图中定位的类
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private MyLocationConfiguration.LocationMode mCurrentMode;  //定义当前定位模式


    private BaiduMap bMap;
    private LocationClient mLocationClient;     //定义LocationClient

    private Polyline mOriginPolyline;
    private PolylineOptions polylineOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_record);
        ButterKnife.bind(this);
        initPolyline();
        if (bMap == null) bMap = mapView.getMap();
        setUpMap();

    }

    private void setUpMap() {


    }


    private void initPolyline() {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.colorAccent));
        polylineOptions.width(20);
      //     polylineOptions.useGradient(true);    设置是否有渐变色。


    }
}

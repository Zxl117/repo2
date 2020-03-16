package com.king.project.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.king.project.R;
import com.king.project.adapter.DeviceAdapter;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SheBeiListActivity extends BaseActivity {

      @BindView(R.id.lv_list)
       ListView lvList;
       DeviceAdapter mDeviceAdapter;
        private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_she_bei_list);
        ButterKnife.bind(this);
        changTitle("设备列表");
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScan();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopscan();
    }

    private void stopscan() {

        BleManager.getInstance().cancelScan();
    }

    private void init(){

            mDeviceAdapter = new DeviceAdapter(this);
            progressDialog = new ProgressDialog(this);
            mDeviceAdapter.setOnDeviceClickListener(new DeviceAdapter.OnDeviceClickListener() {
                @Override
                public void onConnect(BleDevice bleDevice) {
                    if (!BleManager.getInstance().isConnected(bleDevice)) {
                        BleManager.getInstance().cancelScan();
                        connect(bleDevice);
                    }
                }

                @Override
                public void onDisConnect(BleDevice bleDevice) {
                    if (BleManager.getInstance().isConnected(bleDevice)) {
                        BleManager.getInstance().disconnect(bleDevice);
                    }
                }

                @Override
                public void onDetail(BleDevice bleDevice) {
                    if (BleManager.getInstance().isConnected(bleDevice)) {
                       Intent intent = getIntent();
                       intent.putExtra(MainActivity.KEY_DATA, bleDevice);
                       setResult(001,intent);

                       finish();
                    }
                }
            });
           lvList.setAdapter(mDeviceAdapter);

    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                progressDialog.dismiss();
              //  Toast.makeText(SheBeiListActivity.this, getString(R.string.connect_fail), Toast.LENGTH_LONG).show();        //改动
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (isActiveDisConnected) {
                    Toast.makeText(SheBeiListActivity.this, getString(R.string.active_disconnected), Toast.LENGTH_LONG).show();
                } else {

//                    Toast.makeText(SheBeiListActivity.this, getString(R.string.disconnected)+",,,,,,,,,,,", Toast.LENGTH_LONG).show();     //改动
//                     Intent intent =new Intent(SheBeiListActivity.this,MainActivity.class);
//                      startActivity(intent);
                }

            }
        });
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();

                //  btnscan.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                //没添加动画
                // btnscan.setText(getString(R.string.start_scan));
            }
        });
    }


}

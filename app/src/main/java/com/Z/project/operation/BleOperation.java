package com.Z.project.operation;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;


import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.Z.project.utils.Rec_Msg;
import com.Z.project.utils.SendMsg2Byte;


import java.util.UUID;


public class BleOperation {
    private BleDevice bleDevice;
    private BluetoothGattService bluetoothGattService;
    private BluetoothGattCharacteristic characteristic;
    private int charaProp;
    private Activity activity;
    BluetoothGatt gatt;
    double value;//剂量率
    double sumValue; //累积剂量
    public BleOperation(BleDevice bleDevice, Activity activity) {
        this.bleDevice = bleDevice;
        gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        bluetoothGattService = gatt.getService(UUID.fromString("00001811-0000-1000-8000-00805f9b34fb"));
        characteristic = bluetoothGattService.getCharacteristic(UUID.fromString("00002a46-0000-1000-8000-00805f9b34fb"));  //2a44 写  2a46 notify 2a47 读
        this.activity = activity;
    }


    /**
     * @param context 上下文环境
     * @param txt    显示文本的textview 控件
     * @return        返回读取的数据
     */
    public double read (final Context context, final TextView txt) {

        BleManager.getInstance().read(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(final byte[] data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                value= Rec_Msg.showData(data);
                                sumValue+=(value/60);
                                addText(txt,Double.toString(value));    //HexUtil.formatHexString(data, true)
                            }
                        });
                    }
                    @Override
                    public void onReadFailure(final BleException exception) {
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              addText(txt, exception.toString());
                          }
                      });
                    }
                });
        return sumValue;
    }

    /**
     * @param context 上下文环境
     * @param txt    显示的文本
     * @param sendMsg   要发送的文本
     */
    public void write(final Context context, final TextView txt,TextView sendMsg) {
        String hex = sendMsg.getText().toString();
        String msg = SendMsg2Byte.sendMsg(hex);
        if(TextUtils.isEmpty(hex))
        {
            return;
        }
        BleManager.getInstance().write(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                addText(txt, "write success, current: " + current
                                        + " total: " + total
                                        + " justWrite: " + HexUtil.formatHexString(justWrite, true));
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                addText(txt, exception.toString());
                            }
                        }).start();
                    }
                });

    }

    public void notify(final Context context, final TextView txt) {
        BleManager.getInstance().notify(
                bleDevice,
                characteristic.getService().getUuid().toString(),
                characteristic.getUuid().toString(),
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addText(txt, "notify success");
                            }
                        });
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addText(txt, exception.toString());
                            }
                        });
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addText(txt, HexUtil.formatHexString(characteristic.getValue(), true));      //characteristic.getValue() 要替换成目标数据

                            }
                        });
                    }
                });

}


    private void runOnUiThread( Runnable runnable )
    {

        if ( activity!= null)
            activity.runOnUiThread(runnable);
    }

      private void addText(TextView textView, String content) {
        textView.setText(content);
        //textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight())  {
            textView.scrollTo(0, offset - textView.getHeight());
         }
     }

}















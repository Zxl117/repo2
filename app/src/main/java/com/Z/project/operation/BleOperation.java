package com.Z.project.operation;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;


import com.Z.project.database.Realtime_data;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.Z.project.utils.Rec_Msg;
import com.Z.project.utils.SendMsg2Byte;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class BleOperation {
    private BleDevice bleDevice;
    private BluetoothGattService bluetoothGattServiceNotify;
    private BluetoothGattService bluetoothGattServiceWrite;
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGattCharacteristic characteristicNotify;
    private BluetoothGattCharacteristic characteristicWrite;
    private Activity activity;
    BluetoothGatt gatt;
    float value;//剂量率
    float sumValue; //累积剂量
    float maxdoes;  //最大剂量率
    Realtime_data realtime_data;
    private float doessum=0;

    public BleOperation(BleDevice bleDevice, Activity activity,String type) {
        this.bleDevice = bleDevice;
        gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
        if(type.equals(ConstantValue.Notify))
        {
            bluetoothGattServiceNotify = gatt.getService(UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"));
            characteristicNotify = bluetoothGattServiceNotify.getCharacteristic(UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb"));
        }
       else
        {
            bluetoothGattServiceWrite=gatt.getService(UUID.fromString("0000ffe5-0000-1000-8000-00805f9b34fb"));
            characteristicWrite = bluetoothGattServiceWrite.getCharacteristic(UUID.fromString("0000ffe9-0000-1000-8000-00805f9b34fb"));
        }

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
     * @param sendMsg   要发送的文本
     */
    public void write(final Context context, String sendMsg, String type) {
        String hex;
         if(type.equals("A5"))   //剂量累加
         {
             hex = sendMsg;
         }
         else
         {
             hex = SendMsg2Byte.sendMsg(sendMsg,type);
         }

        if(TextUtils.isEmpty(hex))
        {
            return;
        }
        BleManager.getInstance().write(
                bleDevice,
                characteristicWrite.getService().getUuid().toString(),
                characteristicWrite.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
//                                addText(txt, "write success, current: " + current
//                                        + " total: " + total
//                                        + " justWrite: "+HexUtil.formatHexString(justWrite, true));
                            //    Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                addText(txt, exception.toString());
                            }
                        }).start();
                    }
                });

    }
    public void writeTime(final Context context, String sendMsg1,String sendMsg2,String sendMsg3, String type) {

        String hex = SendMsg2Byte.sendMsgT(sendMsg1,sendMsg2,sendMsg3,type);

        if(TextUtils.isEmpty(hex))
        {
            return;
        }
        BleManager.getInstance().write(
                bleDevice,
                characteristicWrite.getService().getUuid().toString(),
                characteristicWrite.getUuid().toString(),
                HexUtil.hexStringToBytes(hex),
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
//                                addText(txt, "write success, current: " + current
//                                        + " total: " + total
//                                        + " justWrite: "+HexUtil.formatHexString(justWrite, true));
                                //    Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                addText(txt, exception.toString());
                            }
                        }).start();
                    }
                });

    }
    public float notify(final Context context, final TextView txt1, final TextView txt2,final TextView txt3,final TextView txt4) {
        if (realtime_data == null) {
            realtime_data = new Realtime_data(context);
        }
        BleManager.getInstance().notify(
                bleDevice,
                characteristicNotify.getService().getUuid().toString(),
                characteristicNotify.getUuid().toString(),
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                value=Rec_Msg.showData(characteristicNotify.getValue());
//                                addText(txt, String.valueOf(value));
                            }
                        });
                    }

                    @Override
                    public void onNotifyFailure(final BleException exception) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                value = Rec_Msg.showData(characteristicNotify.getValue());
                                realtime_data.setDoesrate(value);
                                realtime_data.dbWrite(realtime_data, context);
//                                Date date = new Date();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                //  Toast.makeText(context,dateFormat.format(date),Toast.LENGTH_SHORT).show();
                                doessum = does(value);
                                maxdoes= maxdoes(value);
                                addText(txt1, String.valueOf(value));
                                addText(txt2, String.valueOf(value));
                                addText(txt3, String.valueOf(doessum));
                                addText(txt4, String.valueOf(maxdoes));
                            }
                        });
                    }
                });
        return value;
    }
        private float does (float doseRate)
        {
            float does;
            does=doseRate*(5+10)/3600;
            doessum += does;
            return doessum;
        }
        private float maxdoes (float doesRate)
        {
            if(maxdoes<doesRate)
                maxdoes=doesRate;
                return maxdoes;
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















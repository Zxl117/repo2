package com.Z.project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.Z.project.activity.MainActivity;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Realtime_data {
    private DatabaseHelper dbHelper;
    private float doesrate;
    private Date date;


    private LatLng ll2;
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    private String datetime;

    public float getDoesrate() {
        return doesrate;
    }

    public void setDoesrate(float doesrate) {
        this.doesrate = doesrate;
    }


    public Realtime_data(Context context) {

        dbHelper=new DatabaseHelper(context);

    }
    public void dbWrite(Realtime_data realtime_data,Context ctx)
    {
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        ContentValues values =new ContentValues(); //相当于map
        values.put("dose",realtime_data.getDoesrate());
        values.put("longtitude", MainActivity.locData.longitude);
        values.put("latitude", MainActivity.locData.latitude);
        sdb.insert(Constants.TABLE_REALTIME_RECORD,null,values);
        Log.d("tips","纬度"+ MainActivity.locData.latitude+"经度"+MainActivity.locData.longitude+"位置");
        sdb.close();
    }




}

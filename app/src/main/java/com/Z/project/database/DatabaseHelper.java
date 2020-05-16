package com.Z.project.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * @param context 上下文环境
     *  name     数据库名称
     *  factory   游标工厂
     *  version  版本号
     */

    public DatabaseHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.VERSION_CODE);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
         //创建时回调
         //创建字段
        // sql create table table_name(_id integer,name varchar ,age integer);
        String sql ="create table "+Constants.TABLE_USER+"(_id integer primary key autoincrement,name varchar ,password varchar,imagehead varchar,sex boolean ,birthday date )";
        String sql1 ="create table "+Constants.TABLE_REALTIME_RECORD+"(currentTime TIMESTAMP default (datetime('now','localtime')),dose float ,longtitude decimal,latitude decimal)";
        db.execSQL(sql);
        db.execSQL(sql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //升级数据库回调
    }
}

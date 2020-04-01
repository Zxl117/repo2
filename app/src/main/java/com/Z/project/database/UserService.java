package com.Z.project.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class UserService {
    private DatabaseHelper dbHelper;
    public UserService(Context context){
        dbHelper=new DatabaseHelper(context);
    }

    //登录用
    public boolean login(String username,String password){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select * from user where name=? and password=?";
        Cursor cursor=sdb.rawQuery(sql, new String[]{username,password});
        if(cursor.moveToFirst()==true){
            cursor.close();
            sdb.close();
            return true;
        }
        cursor.close();
        sdb.close();
        return false;
    }

    //注册用
    public boolean register(User user,Context ctx){
        //用getReadable和getWriteable都可以创建或者打开一个数据库并返回一个可对数据库进行读写操作的对象，当数据库满R可以只读，W会报错
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
       // Cursor query = sdb.rawQuery(Constants.TABLE_NAME ,new String[]{user.getAccount()});
        String sql="select * from user where name=?";
        Cursor query = sdb.rawQuery(sql,new String[]{user.getAccount()});
        if(query.moveToFirst())
        {
            Toast.makeText(ctx,"该账号已注册，请重新注册",Toast.LENGTH_SHORT).show();
            query.close();
            sdb.close();
            return false;
        }
        else {

                   //  String sql="insert into "+ Constants.TABLE_NAME +"(name,password) values(?,?)";
                 //  Object obj[]={user.getAccount(),user.getPassword()};.
                // sdb.execSQL(sql, obj);
            ContentValues values =new ContentValues(); //相当于map
            values.put("name",user.getAccount());
            values.put("password",user.getPassword());
            sdb.insert(Constants.TABLE_NAME,null,values);
            sdb.close();
            return true;
        }


    }

}


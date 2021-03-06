package com.Z.project.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SpUtil
{

    private  static SharedPreferences sp;

    /**
     * 写入Boolean变量至sp中
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点的值Boolean
     */
    public static void putBoolean (Context ctx,String key ,boolean value) {
        //存储节点文件名称，读写方式
        if(sp==null)
        {

            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        sp.edit().putBoolean(key,value).commit();
    }


    /**
     * 读取节点表示在sp中
     * @param ctx 上下文环境
     * @param key 存储节点名称
     * @param defvalue  没有此节点的默认值
     * @return   默认值或者此节点读取到的结果
     */
    public static  boolean getBoolean(Context ctx ,String key ,boolean defvalue) {
        if (sp==null)
        {
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return  sp.getBoolean(key,defvalue);

    }


    /**
     * 写入字符串变量至sp中
     * @param ctx  上下文环境
     * @param key  存储节点名称
     * @param value  存储节点值string
     */
    public static void putString (Context ctx ,String key ,String value)
    {
        if (sp==null)
        {
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        sp.edit().putString(key,value).commit();

    }

    /**
     * 读取字符串变量至sp中
     * @param ctx  上下文环境
     * @param key   存储节点名称
     * @param value   存储节点值string
     * @return
     */
    public  static String getString(Context ctx ,String key ,String value){

        if (sp==null)
        {
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
      return   sp.getString(key,value);

    }




    /**
     * @param ctx
     * @param key
     */
    public static void saveDrawable(Context ctx, Bitmap bitmap ,String key) {
        if (sp==null)
        {
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        SharedPreferences.Editor editor=sp.edit();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
        String imageBase64 = new String(Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT));
        editor.putString(key,imageBase64 );
        editor.commit();
    }

    public static Drawable getDrawableByKey(Context ctx, String key) {
        if (sp==null)
        {
            sp=ctx.getSharedPreferences("config",Context.MODE_PRIVATE);

        }
        String temp = sp.getString(key, "");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        return Drawable.createFromStream(bais, "");
    }



}

package com.Z.project.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.Z.project.R;
import com.Z.project.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


public class BaseActivity extends FragmentActivity {


    public SharedPreferences sp;
    public boolean isfirst=false;
    private static PermissionListener mListener;

    private Activity currActivity;
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        requestWindowFeature(0x1);
        sp = getSharedPreferences("info", 0x0);
        isfirst = true;
        String[] permissions = {"android.permission.ACCESS_FINE_LOCATION",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.CALL_PHONE"
        };
        requestRuntimePermission(permissions, new PermissionListener() {
            public void onGranted() {
            }
            public void onDenied(List<String> deniedPermission) {
               // Utils.showToast(getApplicationContext(), "\u6743\u9650\u5df2\u88ab\u62d2\u7edd\uff0c\u8bf7\u5728\u8bbe\u7f6e\u4e2d\u5fc3\u8fdb\u884c\u6388\u6743");
            }
        });
        DatabaseHelper helper=new DatabaseHelper(this);
        helper.getWritableDatabase();
        helper.getReadableDatabase();
        currActivity=this;

    }



    public void changTitle(String title) {
        ((TextView) findViewById(R.id.tv_title)).setVisibility(View.VISIBLE);
        if (title.length() > 11) {
            ((TextView) findViewById(R.id.tv_title)).setText(title.subSequence(
                    0, 11) + "...");
        } else {
            ((TextView) findViewById(R.id.tv_title)).setText(title);
        }
    }

    public void On_Back(View v) {
        onBackPressed();
    }

    public void HiddenLine() {
        findViewById(R.id.view_line).setVisibility(View.GONE);
    }


    public void HiddenBtnBack() {
        ((Button)findViewById(R.id.btn_back)).setVisibility(View.GONE);
    }

    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {

        mListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onGranted();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    if (deniedPermissions.isEmpty()) {
                        mListener.onGranted();
                    } else {
                        mListener.onDenied(deniedPermissions);
                    }
                }
                break;
            default:
                break;
        }
    }



    public interface PermissionListener {
        /**
         * 成功获取权限
         */
        void onGranted();

        /**
         * 为获取权限
         * @param deniedPermission
         */
        void onDenied(List<String> deniedPermission);

    }

}

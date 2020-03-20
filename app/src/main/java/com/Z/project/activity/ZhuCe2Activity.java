package com.Z.project.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Z.project.R;

import com.mob.tools.utils.ResHelper;
import com.mob.tools.utils.SharePrefrenceHelper;

import org.json.JSONObject;



import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.gui.RegisterPage;


public class ZhuCe2Activity extends BaseActivity implements View.OnClickListener {
    /**
     * 验证页，包括短信验证和语音验证，默认使用中国区号
     */
    private static final String TAG ="ZhuCe2Activity" ;
    private static final String KEY_START_TIME = "start_time";
    private SharePrefrenceHelper helper;
    private EventHandler eventHandler;
    private static final int REQUEST_CODE_VERIFY = 1001;
    private int currentSecond;
    private Toast toast;
    private static final int COUNTDOWN = 60;
    private static final String TEMP_CODE = "1319972";
    private Handler handler;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_rpwd)
    EditText etRpwd;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvCode)
    TextView tvCode;
    @BindView(R.id.ck_is)
    CheckBox ckIs;
    @BindView(R.id.tv_xieyi)
    TextView tvXieyi;
    @BindView(R.id.btnVerify)
    Button btnVerify;

    TextView tvToast;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce2);
        ButterKnife.bind(this);
        HiddenLine();
        initListener();
        //默认获取短信和验证按钮不可点击，输入达到规范后，可点击
        btnVerify.setEnabled(false);
        tvCode.setEnabled(false);
        helper = new SharePrefrenceHelper(this);
        helper.open("sms_sp");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        SMSSDK.unregisterEventHandler(eventHandler);
    }



    private void initListener() {

        RegisterPage page = new RegisterPage();
        page.setTempCode(null);

        tvCode.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //手机号输入大于5位，获取验证码按钮可点击
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCode.setEnabled(etPhone.getText() != null && etPhone.getText().length() > 5);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            //验证码输入6位并且手机大于5位，验证按钮可点击
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnVerify.setEnabled(etCode.getText() != null && etCode.getText().length() >= 6 && etPhone.getText() != null && etPhone.getText().length() > 5);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (tvCode != null) {
                    if (currentSecond > 0) {
                        tvCode.setText(getString(R.string.smssdk_get_code) + " (" + currentSecond + "s)");
                        tvCode.setEnabled(false);
                        currentSecond--;
                        handler.sendEmptyMessageDelayed(0, 1000);
                    } else {
                        tvCode.setText(R.string.smssdk_get_code);
                        tvCode.setEnabled(true);
                    }
                }
            }
        };
        eventHandler = new EventHandler() {

            public void afterEvent(final int event, final int result, final Object data) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                currentSecond = COUNTDOWN;
                                handler.sendEmptyMessage(0);
                                helper.putLong(KEY_START_TIME, System.currentTimeMillis());
                            } else {
                                if (data != null && (data instanceof UserInterruptException)) {
                                    // 由于此处是开发者自己决定要中断发送的，因此什么都不用做
                                    return;
                                }
                                processError(data);
                            }
                        }
                    });
                }

        };
        SMSSDK.registerEventHandler(eventHandler);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVerify:
                if (!isNetworkConnected()) {
                    Toast.makeText(ZhuCe2Activity.this, getString(R.string.smssdk_network_error), Toast.LENGTH_SHORT).show();
                    break;
                }
                SMSSDK.submitVerificationCode("86",etPhone.getText().toString().trim(), etCode.getText().toString());
                break;
            case R.id.tvCode:
                //获取验证码间隔时间小于1分钟，进行toast提示，在当前页面不会有这种情况，但是当点击验证码返回上级页面再进入会产生该情况
                long startTime = helper.getLong(KEY_START_TIME);
                if (System.currentTimeMillis() - startTime < COUNTDOWN * 1000) {
                    showErrorToast(getString(R.string.smssdk_busy_hint));
                    break;
                }
                if (!isNetworkConnected()) {
                    Toast.makeText(ZhuCe2Activity.this, getString(R.string.smssdk_network_error), Toast.LENGTH_SHORT).show();
                    break;
                }

                SMSSDK.getVerificationCode("86", etPhone.getText().toString().trim(), null, null);
                break;
        }

    }
    private boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void showErrorToast(String text) {
        if (toast == null) {
            toast = new Toast(this);
            View rootView = LayoutInflater.from(this).inflate(R.layout.smssdk_error_toast_layout, null);
            tvToast = rootView.findViewById(R.id.tvToast);
            toast.setView(rootView);
            toast.setGravity(Gravity.CENTER, 0, ResHelper.dipToPx(this, -100));
        }
        tvToast.setText(text);
        toast.show();
    }
    private void processError(Object data) {
        int status = 0;
        // 根据服务器返回的网络错误，给toast提示
        try {
            ((Throwable) data).printStackTrace();
            Throwable throwable = (Throwable) data;

            JSONObject object = new JSONObject(
                    throwable.getMessage());
            String des = object.optString("detail");
            status = object.optInt("status");
            if (!TextUtils.isEmpty(des)) {
                showErrorToast(des);
                return;
            }
        } catch (Exception e) {
            Log.w(TAG, "", e);
        }
        // 如果木有找到资源，默认提示
        int resId = ResHelper.getStringRes(getApplicationContext(),
                "smsdemo_network_error");
        String netErrMsg = getApplicationContext().getResources().getString(resId);
        showErrorToast(netErrMsg);
    }
}

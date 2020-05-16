package com.Z.project.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.Z.project.R;
import com.Z.project.utils.ConstantValue;
import com.Z.project.utils.SpUtil;
import com.Z.project.utils.Utils;
import com.Z.project.view.RoundImageView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener
{
    @BindView(R.id.img_head)
    RoundImageView imgHead;
    @BindView(R.id.ll_nickname)
    LinearLayout ll_nickname;
    @BindView(R.id.ll_sex)
    LinearLayout ll_sex;
    @BindView(R.id.ll_birthday)
    LinearLayout ll_birthday;
    @BindView(R.id.ll_phone)
    LinearLayout ll_phone;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_phonenumber)
    TextView tv_phone;
    @BindView(R.id.tv_nickname)
    TextView tv_nickname;
    Uri uri;
    int year; //今年
    int month; //当前月份
    int dayOfMonth; //今天
    private String nickname;
    private String phone;
    private int REQUEST_TO_PHOTOALBUM;
    private int REQUEST_TO_PHOTOCUTED;
    private File tempFile;
    Uri uritempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        changTitle("信息");
        initUI();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

    private void initUI() {
        ButterKnife.bind(this);
        ll_nickname.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        ll_birthday.setOnClickListener(this);
        ll_phone.setOnClickListener(this);
        imgHead.setOnClickListener(this);
        REQUEST_TO_PHOTOALBUM = 0x65;
        REQUEST_TO_PHOTOCUTED = 0x66;


        Drawable drawable =SpUtil.getDrawableByKey(this,ConstantValue.KEY_IMGHEAD);
        nickname = SpUtil.getString(this,ConstantValue.KEY_NICKNAME,"");
        String sex=SpUtil.getString(this,ConstantValue.KEY_SEX,"男");
        String birthday =SpUtil.getString(this,ConstantValue.KEY_BIRTHDAY,"");
        phone = SpUtil.getString(this,ConstantValue.KEY_PHONENUM,"15844866600");
        tv_nickname.setText(nickname);
        tv_sex.setText(sex);
        tv_birthday.setText(birthday);
        tv_phone.setText(phone);
        imgHead.setImageDrawable(drawable);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_sex:
                //弹出对话框
                showSingleDialog();
                break;
            case R.id.ll_phone:
                break;
            case R.id.ll_birthday:
                //获取日历
                dateDialogShow();
                break;
            case R.id.ll_nickname:
                setNickNameDialog();
                break;
            case R.id.img_head:
                uploadHead();
                break;
        }
    }

    private void uploadHead() {
        String[] stringItems = {
                "\u4ece\u624b\u673a\u76f8\u518c\u4e2d\u9009\u62e9"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.widthScale(1f)
                .layoutAnimation(null)
                .isTitleShow(false)
                .dividerHeight(0.5f)
                .cornerRadius(0)
                .itemTextColor(Color.parseColor("#333333"))
                .cancelText(Color.parseColor("#333333"))
                .itemPressColor(Color.parseColor("#cfcfcf"))
                .show();


        dialog.setOnOperItemClickL(new OnOperItemClickL() {

            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 1: {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        } else {
                            File mFile = new File(dir, "head.jpg");
                            if (mFile.exists()) {
                                mFile.delete();
                                mFile = new File(dir, "head.jpg");
                            } else {
                                Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".FileProvider".toLowerCase(), mFile);
                                    openCameraIntent.putExtra("output", imageUri);
                                    startActivityForResult(openCameraIntent, 0xc);
                                    break;
                                } else {
                                    openCameraIntent.putExtra("output", Uri.fromFile(mFile));
                                    startActivityForResult(openCameraIntent, 0xc);
                                    break;
                                }

                            }
                        }
                        break;


                    }

                    case 0: {
                        intent = new Intent("android.intent.action.PICK");
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_TO_PHOTOALBUM);
                        break;
                    }
                }
                dialog.dismiss();
            }
        });
    }
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }

    }
    private void setNickNameDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view =View.inflate(this,R.layout.setting_textinput,null);
        dialog.setView(view);
        dialog.show();
        Button bt_submit=view.findViewById(R.id.bt_submit);
        Button bt_cancel=view.findViewById(R.id.bt_cancel);
        EditText et_input=view.findViewById(R.id.et_input);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(et_input.getText()))
                {
                    SpUtil.putString(getApplicationContext(), ConstantValue.KEY_NICKNAME, String.valueOf(et_input.getText()));
                    tv_nickname.setText(et_input.getText());
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"请输入昵称",Toast.LENGTH_SHORT).show();
                }

            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialog.dismiss();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -0x1) {
            if (requestCode == 0xc) {
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File mFile = new File(dir, "head.jpg");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = getImageContentUri(mFile);
                } else {
                    uri = Uri.fromFile(mFile);
                }
                cutImage(uri);
                return;
            }
            if (requestCode == REQUEST_TO_PHOTOALBUM) {
                if (data != null) {
                    uri = data.getData();
                    cutImage(uri);
                }
                return;
            }
            if (requestCode == REQUEST_TO_PHOTOCUTED) {
                try {
//                    Bitmap bitmap = (Bitmap) data.getParcelableExtra("data");

                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uritempFile));

                    imgHead.setImageBitmap(bitmap);
                    SpUtil.saveDrawable(getApplication(),bitmap,ConstantValue.KEY_IMGHEAD);
                    if (tempFile == null) {
                        tempFile = new File(Environment.getExternalStorageDirectory(), "/head.jpg");
                    }
                    Utils.saveBitmapFile(bitmap, tempFile);

                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void cutImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0x1);
        intent.putExtra("aspectY", 0x1);
        intent.putExtra("outputX", 0xc8);
        intent.putExtra("outputY", 0xc8);
        intent.putExtra("return-data", true);

        try {
            uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        } catch (Exception e) {

        }
        startActivityForResult(intent, REQUEST_TO_PHOTOCUTED);
    }
    private void dateDialogShow() {
        Calendar calendar =Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
       DatePickerDialog datePickerDialog=new DatePickerDialog(this,R.style.DateTime,null,year,month,dayOfMonth);
       datePickerDialog.setCancelable(true);
       datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确认", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               //确定的逻辑代码在监听中实现
               DatePicker picker = datePickerDialog.getDatePicker();
                year = picker.getYear();
                 month = picker.getMonth();
                 dayOfMonth = picker.getDayOfMonth();
                 SpUtil.putString(getApplicationContext(),ConstantValue.KEY_BIRTHDAY,year+"-"+(month+1)+"-"+dayOfMonth);
                  tv_birthday.setText(year+"-"+(month+1)+"-"+dayOfMonth);
           }
       });
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消什么也不用做
                    }
                });

       datePickerDialog.show();
    }
    private void showSingleDialog() {
        final String []item ={"男","女"};
        AlertDialog.Builder singlesexchoice=new AlertDialog.Builder(this);
        singlesexchoice.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                 tv_sex.setText(item[i]);
                dialogInterface.dismiss();
            }
        });
        singlesexchoice.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        singlesexchoice.show();
    }
}

package com.king.project.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import com.lidroid.xutils.BitmapUtils;
//import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
//import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
//import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

public class CommonUtil {



	public static boolean isToday(String str){
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = fmt.parse(str);
			if (fmt.format(date).toString().equals(fmt.format(new Date()).toString())) {//格式化为相同格式
				return true;
			} else {
				return false;
			}
		}catch (Exception e){

		}
		return false;

	}



	public static String getMonth(String str){
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = fmt.parse(str);
			String m=(date.getMonth()+1)+"";
		    return  (date.getMonth()+1)+"";
		}catch (Exception e){

		}
		return "";

	}

	public static String getDay(String str){
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = fmt.parse(str);
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(date);
		    //String m=fmt.getCalendar().get(Calendar.DAY_OF_MONTH)+"";
			return fmt.getCalendar().get(Calendar.DAY_OF_MONTH)+"";
//			Date date = fmt.parse(str);
//			String m=date.get+"";
//			return  date.getDay()+"";
		}catch (Exception e){

		}
		return "";

	}

	public static long dateToStamp(String datestr){
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(datestr);
			return date.getTime();
		}catch (Exception e){

		}
		return 0;

	}


	public static void showInfoDialog(Context context, String message) {
		showInfoDialog(context, message, "提示", "确定", null);
	}

	/**
	 * 获取版本号
	 *
	 * @return 当前应用的版本号
	 */
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "1.0";
		}
	}


	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics metric = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}


	public static void showInfoDialog(Context context, String message,
                                      String titleStr, String positiveStr,
                                      DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
			onClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		localBuilder.show();
	}

	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
	 *
	 * @param context
	 * @return
	 */
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return 0;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return 1;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							String extraInfo = netWorkInfo.getExtraInfo();
							if ("cmwap".equalsIgnoreCase(extraInfo)
									|| "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
								return 2;
							}
							return 3;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 获取现在时间
	 *
	 * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
	 */

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
		int picWidth = bitmap.getWidth();
		int picHeight = bitmap.getHeight();
		int picScreenHeight = 0;
		picScreenHeight = (screenWidth * picHeight) / picWidth;
		return picScreenHeight;
	}



//	public static void bindViewSelector(Context context, final View view,
//			String nornalImageurl, final String pressedImageUrl) {
//		final StateListDrawable stateListDrawable = new StateListDrawable();
//		final BitmapUtils utils = new BitmapUtils(context);
//		utils.display(view, nornalImageurl, new BitmapLoadCallBack<View>() {
//
//			@Override
//			public void onLoadCompleted(View container, String uri,
//					Bitmap bitmap, BitmapDisplayConfig config,
//					BitmapLoadFrom from) {
//				Drawable normalDrawable = new BitmapDrawable(bitmap);
//				stateListDrawable.addState(
//						new int[] { android.R.attr.state_active },
//						normalDrawable);
//				stateListDrawable.addState(new int[] {
//						android.R.attr.state_focused,
//						android.R.attr.state_enabled }, normalDrawable);
//				stateListDrawable.addState(
//						new int[] { android.R.attr.state_enabled },
//						normalDrawable);
//				utils.display(container, pressedImageUrl,
//						new BitmapLoadCallBack<View>() {
//
//							@Override
//							public void onLoadCompleted(View container,
//									String uri, Bitmap bitmap,
//									BitmapDisplayConfig config,
//									BitmapLoadFrom from) {
//								stateListDrawable.addState(new int[] {
//										android.R.attr.state_pressed,
//										android.R.attr.state_enabled },
//										new BitmapDrawable(bitmap));
//
//								view.setBackgroundDrawable(stateListDrawable);
//
//							}
//
//							@Override
//							public void onLoadFailed(View container,
//									String uri, Drawable drawable) {
//								// TODO Auto-generated method stub
//
//							}
//						});
//			}
//
//			@Override
//			public void onLoadFailed(View container, String uri,
//					Drawable drawable) {
//
//			}
//		});
//
//	}

	private static Drawable createDrawable(Drawable d, Paint p) {

		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

		return new BitmapDrawable(bitmap);
	}

	/** 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果 */
	public static StateListDrawable createSLD(Context context, Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, paint);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
		Bitmap image = null;
		AssetManager am = ct.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

	// public static <Params, Progress, Result> void executeAsyncTask(
	// AsyncTask<Params, Progress, Result> task, Params... params) {
	// if (Build.VERSION.SDK_INT >= 11) {
	// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	// } else {
	// task.execute(params);
	// }
	// }

	public static String getUploadtime(long created) {
		StringBuffer when = new StringBuffer();
		int difference_seconds;
		int difference_minutes;
		int difference_hours;
		int difference_days;
		int difference_months;
		long curTime = System.currentTimeMillis();
		difference_months = (int) (((curTime / 2592000) % 12) - ((created / 2592000) % 12));
		if (difference_months > 0) {
			when.append(difference_months + "月");
		}

		difference_days = (int) (((curTime / 86400) % 30) - ((created / 86400) % 30));
		if (difference_days > 0) {
			when.append(difference_days + "天");
		}

		difference_hours = (int) (((curTime / 3600) % 24) - ((created / 3600) % 24));
		if (difference_hours > 0) {
			when.append(difference_hours + "小时");
		}

		difference_minutes = (int) (((curTime / 60) % 60) - ((created / 60) % 60));
		if (difference_minutes > 0) {
			when.append(difference_minutes + "分钟");
		}

		difference_seconds = (int) ((curTime % 60) - (created % 60));
		if (difference_seconds > 0) {
			when.append(difference_seconds + "秒");
		}

		return when.append("前").toString();
	}

	// public static boolean hasToken(Context ct) {
	// String token = SharePrefUtil.getString(ct, "token", "");
	// if (TextUtils.isEmpty(token)) {
	// return false;
	// } else {
	// return true;
	// }
	// }

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static String formatDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}


	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		return getFormatSize(cacheSize);
	}


	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	// 获取文件
//Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
//Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 *
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
//        return size + "Byte";  
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}


	//保存文件到指定路径
	public static boolean saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
		File appDir = new File(storePath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			//通过io流的方式来压缩保存图片
			boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();

			//把文件插入到系统图库
			//MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

			//保存图片后发送广播通知更新数据库
			Uri uri = Uri.fromFile(file);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
			if (isSuccess) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	//保存文件到指定路径
	public static String saveImageToGallery2(Context context, Bitmap bmp) {
		// 首先保存图片
		String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
		File appDir = new File(storePath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			//通过io流的方式来压缩保存图片
			boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();

			//把文件插入到系统图库
			//MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

			//保存图片后发送广播通知更新数据库
			Uri uri = Uri.fromFile(file);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
			if (isSuccess) {
				return file.getAbsolutePath();
			} else {
				return "";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}


}

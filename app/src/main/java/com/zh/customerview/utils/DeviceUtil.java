package com.zh.customerview.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by hongzhang on 2018/4/8.
 */

public class DeviceUtil {

    private static int displayHeight = -1;
    private static int displayWidth = -1;
    private static float displayDensity = -1;

    public static int getDisplayHeight(Context context) {
        if (displayHeight == -1) {
            getDisplayMetrics(context);
        }
        return displayHeight;
    }

    public static int getDisplayWidth(Context context) {
        if (displayWidth == -1) {
            getDisplayMetrics(context);
        }
        return displayWidth;
    }

    private static void getDisplayMetrics(Context context) {
        DisplayMetrics mDisplayMetrics = context.getResources().getDisplayMetrics();
        displayHeight = mDisplayMetrics.heightPixels;
        displayWidth = mDisplayMetrics.widthPixels;
        displayDensity = mDisplayMetrics.density;
    }

    public static int dp2px(Context context, int dip) {
        if (displayDensity == -1) {
            getDisplayMetrics(context);
        }
        return (int) (dip * displayDensity + 0.5f);
    }

//    public static int dp2px(int dip) {
//        return dp2px(BaseApplication.applicationContext, dip);
//    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 判断应用客户端是否安装过     
     */
    public static boolean isAppInstall(Context context,String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
//    public static boolean isNetworkConnected(Context context) {
//        if (context != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
//                    .getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
//            if (mNetworkInfo != null) {
//                return mNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }




    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
    /**
     * 跳转三星应用商店
     * @param context {@link Context}
     * @param packageName 包名
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public static boolean goToSamsungMarket(Context context, String packageName) {
//        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + packageName);
        Uri uri = Uri.parse("http://apps.samsung.com/appquery/appDetail.as?appId=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.sec.android.app.samsungapps");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}

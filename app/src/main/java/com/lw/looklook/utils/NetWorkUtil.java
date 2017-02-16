package com.lw.looklook.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lw on 2017/1/31.
 */

public class NetWorkUtil {
    /**
     *  判断当前网络是否可用
     * @param context
     * @return
     */
    public static boolean getCurrentNetWorkState(Context context){
        System.out.println("context1:" + context + ";connectivityManager:" );
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        System.out.println("context2:" + context + ";connectivityManager:" + connectivityManager );
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return  networkInfo !=null && networkInfo.isConnected() ;

    }

    /**
     * 检测wifi是否连接
     *
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 检测3G是否连接
     *
     * @return
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }
}

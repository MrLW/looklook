package com.lw.looklook.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lw.looklook.R;

/**
 * Created by lw on 2017/1/31.
 */
public class SharePrefenceUtil {

    /**
     *  获取Nevigation的position
     * @param context
     * @return
     */
    public static int getNevigationItem(Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(context.getString(R.string.nevigation_item),-1);
    }

    /**
     * 设置Nevigation的position
     * @param context
     * @param t
     */
    public static void setNevigationItem(Context context,int t){
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(context.getString(R.string.nevigation_item),t);
        editor.commit();
    }

}

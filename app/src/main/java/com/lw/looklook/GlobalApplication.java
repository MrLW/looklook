package com.lw.looklook;

import android.app.Application;

/**
 * Created by lw on 2017/2/1.
 */

public class GlobalApplication extends Application {
    private static GlobalApplication myApplication;

    public static Application getContext() {

        return myApplication;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;

    }
}

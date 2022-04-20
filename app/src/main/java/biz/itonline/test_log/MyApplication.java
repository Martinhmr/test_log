package biz.itonline.test_log;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


/**
 * Třída používaná pro získání contextu aplikace
 */
public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}

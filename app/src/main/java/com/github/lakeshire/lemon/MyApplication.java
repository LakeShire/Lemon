package com.github.lakeshire.lemon;

import android.app.Application;
import android.graphics.Typeface;
import android.util.Log;

import com.norbsoft.typefacehelper.TypefaceCollection;
import com.norbsoft.typefacehelper.TypefaceHelper;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        Logger.initialize(Settings.getInstance().isShowMethodLink(true).isShowThreadInfo(false).setMethodOffset(0).setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT));

        TypefaceCollection typeface = new TypefaceCollection.Builder().set(Typeface.NORMAL, Typeface.createFromAsset(getAssets(), "fonts/hanyi.ttf")).create();
        TypefaceHelper.init(typeface);
    }

}

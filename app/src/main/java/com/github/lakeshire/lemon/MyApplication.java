package com.github.lakeshire.lemon;

import android.app.Application;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();	
		CrashHandler.getInstance().init(this);
		Logger.initialize(Settings.getInstance().isShowMethodLink(true).isShowThreadInfo(false).setMethodOffset(0).setLogPriority(BuildConfig.DEBUG ? Log.VERBOSE : Log.ASSERT));
	}

}

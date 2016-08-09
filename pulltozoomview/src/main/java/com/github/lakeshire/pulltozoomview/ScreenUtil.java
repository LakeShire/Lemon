package com.github.lakeshire.pulltozoomview;

import android.app.Activity;
import android.view.Display;
import android.view.WindowManager;

public class ScreenUtil {
    public static int getScreenHeight(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getHeight();
    }
}

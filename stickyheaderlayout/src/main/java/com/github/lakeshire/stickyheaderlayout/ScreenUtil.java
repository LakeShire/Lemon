package com.github.lakeshire.stickyheaderlayout;

import android.content.Context;

public class ScreenUtil {
    public static int dp2px(Context context, float dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}

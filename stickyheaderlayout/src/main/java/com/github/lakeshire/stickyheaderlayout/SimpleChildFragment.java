package com.github.lakeshire.stickyheaderlayout;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.ListView;

/**
 * child fragment that only has a listview
 */
public class SimpleChildFragment extends Fragment implements StickyHeaderLayout.IHandler {

    ListView mListView;

    @Override
    public boolean checkTop() {
        ListView absListView = mListView;
        return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void scroll(int distance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mListView.scrollListBy(distance);
        } else {
            mListView.scrollTo(0, distance);
        }
    }
}

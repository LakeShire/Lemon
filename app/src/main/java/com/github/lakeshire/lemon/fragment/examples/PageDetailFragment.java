package com.github.lakeshire.lemon.fragment.examples;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemon.util.ScreenUtil;
import com.github.lakeshire.lemon.view.ScrollableView;

public class PageDetailFragment extends BasePullFragment {

    private static final String EXTRA_NAME = "extra_name";
    private static final String EXTRA_ICON = "extra_icon";
    private String name;
    private int icon;
    private ScrollableView mDetailView;
    private ImageView mIvIcon;
    private View.OnClickListener mListener;
    private ImageView mIvIcon1;
    private ImageView mIvIcon2;

    public PageDetailFragment() {
        super();
    }

    public static PageDetailFragment newInstance(String name, int res) {
        PageDetailFragment f = new PageDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_NAME, name);
        bundle.putInt(EXTRA_ICON, res);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        Bundle args = getArguments();
        if (args != null) {
            name = args.getString(EXTRA_NAME);
            icon = args.getInt(EXTRA_ICON);
        }
    }

    @Override
    public void initUi() {
        super.initUi();
        mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailView = (ScrollableView) View.inflate(getActivity(), R.layout.view_scroll, null);

                int[] location = new int[2];
                v.getLocationOnScreen(location);
                mDetailView.setIconPosition(location[0], location[1] - ScreenUtil.getStatusBarHeight(getActivity()));
                mDetailView.setIcon(icon);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                getActivity().getWindow().addContentView(mDetailView, lp);
                mDetailView.setAdded(true);
                mDetailView.setCallback(new ScrollableView.Callback() {
                    @Override
                    public void autoDismiss() {

                    }

                    @Override
                    public void onSlideInComplete() {

                    }
                });

                ImageView ivClose = (ImageView) mDetailView.findViewById(R.id.iv_close);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        };

        mIvIcon = (ImageView) find(R.id.iv_icon);
        mIvIcon1 = (ImageView) find(R.id.iv_icon1);
        mIvIcon2 = (ImageView) find(R.id.iv_icon2);
        mIvIcon.setImageResource(icon);
        mIvIcon1.setImageResource(icon);
        mIvIcon2.setImageResource(icon);
        mIvIcon.setOnClickListener(mListener);
        mIvIcon1.setOnClickListener(mListener);
        mIvIcon2.setOnClickListener(mListener);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_detail_page;
    }

    public void dismissDetailView() {
        ((ViewGroup) mDetailView.getParent()).removeView(mDetailView);
        mDetailView.setAdded(false);
    }

    public String getTitle() {
        return name;
    }

    @Override
    public boolean onBackPressed() {
        if (mDetailView != null && mDetailView.isAdded()) {
            dismissDetailView();
            return true;
        } else {
            return false;
        }
    }
}

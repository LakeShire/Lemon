package com.github.lakeshire.lemonapp.fragment.examples;

import android.animation.ValueAnimator;
import android.support.v4.app.FragmentManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.pulltozoomview.PullToZoomView;

import java.util.List;

import in.srain.cube.views.ptr.header.MaterialProgressDrawable;

/**
 * Created by nali on 2016/7/11.
 */
public class PullToZoomFragment extends BaseFragment {
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDatas;
    private CommonListFragment2 mFragment;
    private TextView mTvRefresh;
    private ImageView mIvArrow;
    private MaterialProgressDrawable mMpd;
    private ValueAnimator mAnimator;
    private ValueAnimator mInitAnimator;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mypulltozoom;
    }

    @Override
    public void initUi() {
        super.initUi();

//        mDatas = new ArrayList<>();
//        mDatas.add("水井坊");
//        mDatas.add("古井贡酒");
//        mDatas.add("五粮液");
//        mDatas.add("贵州茅台");
//        mDatas.add("泸州老窖");
//        mDatas.add("水井坊");
//        mDatas.add("古井贡酒");
//        mDatas.add("五粮液");
//        mDatas.add("贵州茅台");
//        mDatas.add("泸州老窖");
//        mDatas.add("水井坊");
//        mDatas.add("古井贡酒");
//        mDatas.add("五粮液");
//        mDatas.add("贵州茅台");
//        mDatas.add("泸州老窖");
//        mListView = (ListView) find(R.id.list);
//        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatas);
//        mListView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

        RelativeLayout rlContainer = (RelativeLayout) find(R.id.container);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        mFragment = new CommonListFragment2();
        fm.beginTransaction().add(R.id.content, mFragment).commit();

        PullToZoomView ptrView = (PullToZoomView) find(R.id.ptrview);
        ptrView.setPtrHandler(new PullToZoomView.PtrHandler() {

            @Override
            public boolean checkTop() {
                return mFragment.checkTop();
            }

            @Override
            public void scroll(int y) {
                mFragment.scroll(y);
            }

            @Override
            public void onRefresh(PullToZoomView view) {
//                mFragment.onRefresh(view);
                view.onRefreshCompleted();
            }
        });
        ptrView.setZoomable(true);

//        mIvArrow = (ImageView) find(R.id.iv_arraow);
//        ptrView.setPtrUiHandler(new PTRView.PtrUiHandler() {
//            @Override
//            public void uiChangeOnRefreshPositionChange(float rate) {
//                mIvArrow.setVisibility(View.VISIBLE);
//                mIvArrow.setRotation(360 * rate);
//            }
//
//            @Override
//            public void uiChangeOnRefreshCompleted() {
////                mFragment.uiChangeOnRefreshCompleted();
////                mAnimator.end();
////                mIvArrow.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void uiChangeOnRefreshStart() {
////                mFragment.uiChangeOnRefreshStart();
//                mAnimator = ValueAnimator.ofInt(0, 360);
//                mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        int angle = (int) mIvArrow.getRotation();
//                        Logger.d("angle: " + angle);
//                        if (angle > 360) {
//                            angle -= 360;
//                        }
//                        mIvArrow.setRotation(angle + 1);
//                    }
//                });
//                mAnimator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                mAnimator.setRepeatMode(ValueAnimator.INFINITE);
//                mAnimator.setDuration(360);
//                mAnimator.start();
//            }
//        });
    }
}

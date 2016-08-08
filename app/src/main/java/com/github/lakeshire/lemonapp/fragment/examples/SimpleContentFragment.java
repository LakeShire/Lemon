package com.github.lakeshire.lemonapp.fragment.examples;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemon.util.BitmapUtil;
import com.github.lakeshire.lemonapp.R;

import butterknife.Bind;

/**
 *
 * 这个页面展示了通用容器CommonAdapter的使用
 * 方便的使用适配器，根据模型类型不同使用不同的布局
 *
 */
public class SimpleContentFragment extends BaseFragment {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_RES = "extra_res";
    private String mTitle;
    private int mRes;

//    @Bind(R.id.iv_pic)
    ImageView mIvPic;

    @Bind(R.id.tv_title)
    TextView mTvTitle;
//    private Bitmap mBitmap;

    public SimpleContentFragment(String title) {
        super();
        mTitle = title;
//        mRes = res;
    }

    public SimpleContentFragment() {
        super();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_simple_content;
    }

    @Override
    public void initUi() {
        super.initUi();

        mIvPic = (ImageView) find(R.id.iv_pic);

//        ImageUtil.getInstance(getActivity()).setImage(mIvPic, R.drawable.image1, 0, 0, false);
        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap mBitmap = BitmapUtil.reduce(getActivity(), R.drawable.image1, 1024, 1024);
                return mBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mIvPic.setImageBitmap(bitmap);
            }
        }.execute();

        mTvTitle.setText(mTitle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mBitmap != null) {
//            mIvPic.setImageBitmap(null);
//            mBitmap.recycle();
//            mBitmap = null;
//        }
    }
}

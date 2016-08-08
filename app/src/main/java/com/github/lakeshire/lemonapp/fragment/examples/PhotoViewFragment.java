package com.github.lakeshire.lemonapp.fragment.examples;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.lemonapp.view.photoview.PhotoViewAttacher;

import butterknife.Bind;

public class PhotoViewFragment extends BaseFragment {

    @Bind(R.id.image)
    ImageView mImage;
    private PhotoViewAttacher mAttacher;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_photoview;
    }

    @Override
    public void initUi() {
        super.initUi();

        // Set the Drawable displayed
        Drawable bitmap = getResources().getDrawable(R.drawable.image2);
        mImage.setImageDrawable(bitmap);

        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(mImage);
    }
}

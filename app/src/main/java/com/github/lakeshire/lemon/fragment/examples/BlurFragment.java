package com.github.lakeshire.lemon.fragment.examples;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.base.BaseFragment;
import com.github.lakeshire.lemon.view.blureffect.Blur;
import com.github.lakeshire.lemon.view.blureffect.ImageUtils;
import com.github.lakeshire.lemon.view.blureffect.ScrollableImageView;

import java.io.File;

/**
 * Created by nali on 2016/4/28.
 */
public class BlurFragment extends BaseFragment {

    @Override
    public int getLayoutId() {
        return R.layout.fragment_blur;
    }

    private static final String BLURRED_IMG_PATH = "blurred_image.png";
    private static final int TOP_HEIGHT = 700;
    private ListView mList;
    private ImageView mBlurredImage;
    private View headerView;
    private ImageView mNormalImage;
    private ScrollableImageView mBlurredImageHeader;
    private Switch mSwitch;
    private float alpha;

    @Override
    public void initUi() {
        super.initUi();

        final int screenWidth = ImageUtils.getScreenWidth(getActivity());

        // Find the view
        mBlurredImage = (ImageView) find(R.id.blurred_image);
        mNormalImage = (ImageView) find(R.id.normal_image);
        mBlurredImageHeader = (ScrollableImageView) find(R.id.blurred_image_header);
        mSwitch = (Switch) find(R.id.background_switch);
        mList = (ListView) find(R.id.list);

        // prepare the header ScrollableImageView
        mBlurredImageHeader.setScreenWidth(screenWidth);

        // Action for the switch
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBlurredImage.setAlpha(alpha);
                } else {
                    mBlurredImage.setAlpha(0f);

                }

            }
        });

        // Try to find the blurred image
        final File blurredImage = new File(getActivity().getFilesDir() + BLURRED_IMG_PATH);
        if (!blurredImage.exists()) {

            // launch the progressbar in ActionBar
            getActivity().setProgressBarIndeterminateVisibility(true);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    // No image found => let's generate it!
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.image, options);
                    Bitmap newImg = Blur.fastblur(getActivity(), image, 12);
                    ImageUtils.storeImage(newImg, blurredImage);
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            updateView(screenWidth);

                            // And finally stop the progressbar
                            getActivity().setProgressBarIndeterminateVisibility(false);
                        }
                    });

                }
            }).start();

        } else {

            // The image has been found. Let's update the view
            updateView(screenWidth);

        }

        String[] strings = getActivity().getResources().getStringArray(R.array.list_content);

        // Prepare the header view for our list
        headerView = new View(getActivity());
        headerView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, TOP_HEIGHT));
        mList.addHeaderView(headerView);
        mList.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_common_text, R.id.textview, strings));
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            /**
             * Listen to the list scroll. This is where magic happens ;)
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // Calculate the ratio between the scroll amount and the list
                // header weight to determinate the top picture alpha
                alpha = (float) -headerView.getTop() / (float) TOP_HEIGHT;
                // Apply a ceil
                if (alpha > 1) {
                    alpha = 1;
                }

                // Apply on the ImageView if needed
                if (mSwitch.isChecked()) {
                    mBlurredImage.setAlpha(alpha);
                }

                // Parallax effect : we apply half the scroll amount to our
                // three views
                mBlurredImage.setTop(headerView.getTop() / 2);
                mNormalImage.setTop(headerView.getTop() / 2);
                mBlurredImageHeader.handleScroll(headerView.getTop() / 2);

            }
        });
    }

    private void updateView(final int screenWidth) {
        Bitmap bmpBlurred = BitmapFactory.decodeFile(getActivity().getFilesDir() + BLURRED_IMG_PATH);
        bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred, screenWidth, (int) (bmpBlurred.getHeight()
                * ((float) screenWidth) / (float) bmpBlurred.getWidth()), false);

        mBlurredImage.setImageBitmap(bmpBlurred);

        mBlurredImageHeader.setoriginalImage(bmpBlurred);
    }
}

package com.github.lakeshire.lemonapp.fragment.examples;

import android.view.View;
import android.widget.Toast;

import com.github.lakeshire.lemon.fragment.base.BasePullFragment;
import com.github.lakeshire.lemonapp.R;
import com.github.lakeshire.pathmenu.PathMenu;

public class PathMenuFragment extends BasePullFragment {

    int icons[] = {R.drawable.path_music, R.drawable.path_location, R.drawable.path_photo, R.drawable.path_quote, R.drawable.path_sleep};

    PathMenu mPathMenu;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_path_menu;
    }

    @Override
    public void initUi() {
        super.initUi();

        mPathMenu = (PathMenu) find(R.id.path_menu);
        View.OnClickListener[] listeners = new View.OnClickListener[5];
        listeners[0] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked music", Toast.LENGTH_SHORT).show();
            }
        };
        listeners[1] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked location", Toast.LENGTH_SHORT).show();
            }
        };
        listeners[2] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked photo", Toast.LENGTH_SHORT).show();
            }
        };
        listeners[3] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked quote", Toast.LENGTH_SHORT).show();
            }
        };
        listeners[4] = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "clicked sleep", Toast.LENGTH_SHORT).show();
            }
        };
        mPathMenu.initView(getActivity(), icons, listeners);
    }
}

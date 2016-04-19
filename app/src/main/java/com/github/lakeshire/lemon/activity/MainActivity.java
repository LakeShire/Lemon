package com.github.lakeshire.lemon.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.github.lakeshire.lemon.R;
import com.github.lakeshire.lemon.fragment.ExampleListFragment;
import com.github.lakeshire.lemon.fragment.base.BaseFragment;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = new ExampleListFragment();
            fm.beginTransaction().add(R.id.container, fragment).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        BaseFragment f = (BaseFragment) fm.findFragmentById(R.id.container);
        if (!f.onBackPressed()) {
            super.onBackPressed();
        }
	}
}

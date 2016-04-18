package com.github.lakeshire.lemon.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.github.lakeshire.lemon.R;

public class BaseActivity extends AppCompatActivity {

	public void startFragment(Class<?> clazz) {
    	try {
    		FragmentManager fm = getSupportFragmentManager();
			Fragment fragment = (Fragment) clazz.newInstance();
			if (fragment != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, fragment);
				ft.addToBackStack(clazz.getSimpleName()).commit();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
    }
	
	public boolean endFragment() {
    	FragmentManager fm = getSupportFragmentManager();
    	if (fm.getBackStackEntryCount() > 0) {
    		fm.popBackStackImmediate();
    		return true;
    	} else {
    		return false;
    	}
    }
}

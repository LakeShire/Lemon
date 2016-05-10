package com.github.lakeshire.lemon.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 页面适配器
 *
 * @author louis.liu
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    protected Fragment[] mFragments;
    protected String[] mTitles;
    protected String mClazz;

    public PagerAdapter(FragmentManager fm, String[] titles, String clazz) {
        super(fm);
        mTitles = titles;
        mFragments = new Fragment[titles.length];
        mClazz = clazz;
    }

    /**
     * Return the Fragment associated with a specified position.
     */
    @SuppressWarnings("finally")
    public Fragment getItem(int position) {
        Class<?> c;
        Fragment fragment = null;
        try {
            if (mFragments[position] == null) {
                c = Class.forName(mClazz);
                Constructor<?> cons = c.getDeclaredConstructor(new Class[]{String.class});
                cons.setAccessible(true);
                fragment = (Fragment) cons.newInstance(new Object[]{mTitles[position]});
                mFragments[position] = fragment;
            } else {
                fragment = mFragments[position];
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
//        ((Fragment) object).onDestroy();
    }
}

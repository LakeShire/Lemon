package com.github.lakeshire.stickyheaderlayout;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 页面适配器
 * 
 * @author louis.liu
 * 
 */
public class PagerAdapter extends FragmentPagerAdapter {

	private Fragment[] mFragments;
	private List<PageInfo> mPages;
	private String[] mTitles;

	public PagerAdapter(FragmentManager fm, List<PageInfo> pages, String[] titles) {
		super(fm);
		mFragments = new Fragment[pages.size()];
		mPages = pages;
		mTitles = titles;
	}

	public PagerAdapter(FragmentManager fm)
	{
		super(fm);
	}

	@SuppressWarnings("finally")
	public Fragment getItem(int position) {
		Class<?> c;
		Fragment fragment = null;
		try {
			if (mFragments[position] == null) {
				String clazz = mPages.get(position).clazz;
				Bundle bundle = mPages.get(position).bundle;
				c = Class.forName(clazz);
				Constructor<?> cons = c.getDeclaredConstructor();
				cons.setAccessible(true);
				fragment = (Fragment) cons.newInstance();
				if (bundle != null) {
					fragment.setArguments(bundle);
				}
				mFragments[position] = fragment;
			} else {
				fragment = mFragments[position];
			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		finally {
			return fragment;
		}
	}

	@Override
	public int getCount()
	{
		return mTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position)
	{
		return mTitles[position];
	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}
}

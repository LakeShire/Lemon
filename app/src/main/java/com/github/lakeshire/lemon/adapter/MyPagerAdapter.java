package com.github.lakeshire.lemon.adapter;

import android.support.v4.app.FragmentManager;

/**
 * Created by nali on 2016/4/21.
 */
public class MyPagerAdapter extends PagerAdapter {

    public MyPagerAdapter(FragmentManager fm, String[] titles, String clazz) {
        super(fm, titles, clazz);
    }
//
//
//    public Fragment getItem(int position) {
//        Class<?> c;
//        Fragment fragment = null;
//        try {
//            if (mFragments[position] == null) {
//                c = Class.forName(mClazz);
//                Constructor<?> cons = c.getDeclaredConstructor(new Class[]{String.class});
//                cons.setAccessible(true);
//                fragment = (Fragment) cons.newInstance(new Object[]{mTitles[position]});
//                mFragments[position] = fragment;
//            } else {
//                fragment = mFragments[position];
//            }
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } finally {
//            return fragment;
//        }
//    }
}

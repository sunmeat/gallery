package com.sunmeat.myapplication;

import static com.sunmeat.myapplication.ImageData.IMAGE_URLS;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

// АДАПТЕР ДЛЯ ПРОГРУЗКИ КАРТИНОК ВО ВЬЮ-ПЕЙДЖЕР (СЛАЙДЕР/СВАЙПЕР)

public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    public ImagePagerAdapter(Fragment fragment) {
        // Note: Initialize with the child fragment manager.
        super(fragment.getChildFragmentManager());
    }

    @Override
    public int getCount() {
        return IMAGE_URLS.length;
    }

    @Override
    public Fragment getItem(int position) {
        //return ImageFragment.newInstance(IMAGE_DRAWABLES[position]);
        return ImageFragment.newInstance(IMAGE_URLS[position]);
    }
}

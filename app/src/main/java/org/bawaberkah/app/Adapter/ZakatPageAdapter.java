package org.bawaberkah.app.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.bawaberkah.app.Fragment.ZakatMaalFragment;
import org.bawaberkah.app.Fragment.ZakatPenghasilanFragment;

public class ZakatPageAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public ZakatPageAdapter(FragmentManager fm, int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        //Bundle bundle = new Bundle();
        //bundle.putString("my_key", "Test dari Pager");

        switch (position) {
            case 0:
                ZakatPenghasilanFragment home = new ZakatPenghasilanFragment();
                //home.setArguments(bundle);
                return home;
            case 1:
                ZakatMaalFragment about = new ZakatMaalFragment();
                return about;

            default:
                return null;
        }
    }
}

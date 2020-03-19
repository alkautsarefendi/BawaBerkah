package org.bawaberkah.app.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.bawaberkah.app.Adapter.ZakatPageAdapter;
import org.bawaberkah.app.Fragment.ZakatMaalFragment;
import org.bawaberkah.app.Fragment.ZakatPenghasilanFragment;
import org.bawaberkah.app.R;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;

public class HitungZakatActivity extends AppCompatActivity implements MaterialTabListener {

    TabLayout tabHost;
    ViewPager viewPager;
    ViewPagerAdapter androidAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitung_zakat);

        //tab host
        tabHost = findViewById(R.id.tabLayout);
        tabHost.addTab(tabHost.newTab().setText("Zakat Penghasilan"));
        tabHost.addTab(tabHost.newTab().setText("Zakat Maal"));
        tabHost.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager =(ViewPager)findViewById(R.id.viewpager);
        PagerAdapter tabsAdapter = new ZakatPageAdapter(getSupportFragmentManager(), tabHost.getTabCount());
        viewPager.setAdapter(tabsAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabHost));
        tabHost.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //tab on selected
    @Override
    public void onTabSelected(MaterialTab materialTab) {

        viewPager.setCurrentItem(materialTab.getPosition());
    }

    //tab on reselected
    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    //tab on unselected
    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    // view pager adapter
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private final Bundle fragmentBundle;

        public ViewPagerAdapter(FragmentManager fragmentManager, Bundle data) {
            super(fragmentManager);
            fragmentBundle = data;
        }

        public Fragment getItem(int num) {
            switch (num) {
                case 0:
                    final ZakatPenghasilanFragment fragmentZakatPenghasilan = new ZakatPenghasilanFragment();
                    fragmentZakatPenghasilan.setArguments(this.fragmentBundle);
                    return fragmentZakatPenghasilan;
                case 1:
                    final ZakatMaalFragment fragmentZakatMaal = new ZakatMaalFragment();
                    fragmentZakatMaal.setArguments(this.fragmentBundle);
                    return fragmentZakatMaal;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }
    public void goBack(View view) {
        onBackPressed();
    }
}

package com.aji.donasi.activities;

import android.os.Bundle;

import com.aji.donasi.Helper;
import com.aji.donasi.MessageEvent;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.aji.donasi.R;
import com.aji.donasi.fragments.DetailKontenFragment;
import com.aji.donasi.fragments.DonaturFragment;
import com.aji.donasi.fragments.PerkembanganFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DetailKontenActivity extends AppCompatActivity {

    private ImageView gambar;
    private String gambarkonten;

    ViewPager viewPager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailkonten);

        EventBus.getDefault().register(this);

        //Mendapat viewpager untuk setiap tab
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        //Mengatur tab ke dalam toolbar
        tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        gambar = findViewById(R.id.gambar);

        String imagePath= Helper.IMAGE_URL_KONTEN +gambarkonten;

        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .into(gambar);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        gambarkonten = event.gambar;
    }

    @Override public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DetailKontenFragment(), "Detail");
        adapter.addFragment(new DonaturFragment(), "Donatur");
        adapter.addFragment(new PerkembanganFragment(), "Perkembangan");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

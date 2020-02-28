package com.aji.donasi.activities;

import android.content.Intent;
import android.os.Bundle;

import com.aji.donasi.MessageEvent;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

    ViewPager viewPager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailkonten);

        //Mendapat viewpager untuk setiap tab
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        //Mengatur tab ke dalam toolbar
        tabs = findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        gambar = findViewById(R.id.gambar);

    }

//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        Toast.makeText(DetailKontenActivity.this, String.valueOf(event.id_konten), Toast.LENGTH_SHORT).show();
//    }
//    @Override public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//    @Override public void onPause() {
//        super.onPause();
//        EventBus.getDefault().unregister(this);
//    }

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

        public Adapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

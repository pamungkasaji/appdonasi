package com.aji.donasi.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.MessageEvent;
import com.aji.donasi.models.Konten;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.Toolbar;

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
    private Konten konten;

    //EventBus
    //private String gambarkonten;
    private Konten kontenMessage;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbarLayout.setTitle("My Toolbar Title");
        collapsingToolbarLayout.setContentScrimResource(R.color.colorPrimary);

        String imagePath= Helper.IMAGE_URL_KONTEN +kontenMessage.getGambar();

        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .into(gambar);

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KontenMessage event) {
        kontenMessage = event.konten;
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

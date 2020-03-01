package com.aji.donasi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aji.donasi.Helper;
import com.aji.donasi.R;
import com.aji.donasi.Session;
import com.aji.donasi.activities.BuatKontenActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class BuatFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabs;
    private TextView autentikasi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_buat, container, false);
        // Setting ViewPager for each Tabs
        viewPager = view.findViewById(R.id.view_pager);
        //setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = view.findViewById(R.id.tab_layout);
        //tabs.setupWithViewPager(viewPager);

        autentikasi = view.findViewById(R.id.autentikasi);

        initView();

        return view;
    }

    private void initView(){
        if(Session.getInstance(getActivity()).isLoggedIn()) {
            setupViewPager(viewPager);
            tabs.setupWithViewPager(viewPager);
            autentikasi.setVisibility(View.GONE);
        } else {
            autentikasi.setVisibility(View.VISIBLE);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ListGalangDanaFragment(), "Galang Dana");
        adapter.addFragment(new DonasiMasukFragment(), "Donasi Masuk");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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

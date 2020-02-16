package com.aji.donasi.activities;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.aji.donasi.R;
import com.aji.donasi.fragments.DetailKontenFragment;
import com.aji.donasi.fragments.DonaturFragment;
import com.aji.donasi.fragments.PerkembanganFragment;

import java.util.ArrayList;
import java.util.List;

import static com.aji.donasi.fragments.HomeFragment.EXTRA_IDKONTEN;

public class DetailKontenActivity extends AppCompatActivity {

    private int id_konten;

    private Button buttonDonasi;
    private ImageView gambar;

    ViewPager viewPager;
    TabLayout tabs;

    public List<FragmentCommunicator> fragmentCommunicators = new ArrayList<>();

    public interface FragmentCommunicator {
        void sendData(Integer data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailkonten);

        // Setting ViewPager for each Tabs
        viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        tabs = (TabLayout) findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);

        //buttonDonasi = findViewById(R.id.buttongallery);

        //DonaturFragment donaturFragment = DonaturFragment.newInstance("example text ", 123);

        gambar = findViewById(R.id.gambar);

        Intent intent = getIntent();
        id_konten = intent.getIntExtra(EXTRA_IDKONTEN, 0);
    }

    private void setupViewPager(ViewPager viewPager) {
        //DetailKontenActivity.Adapter adapter = new DetailKontenActivity().Adapter(getSupportFragmentManager());
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DetailKontenFragment(), "Detail");
        adapter.addFragment(new DonaturFragment(), "Donatur");
        adapter.addFragment(new PerkembanganFragment(), "Perkembangan");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (FragmentCommunicator fragmentCommunicator : fragmentCommunicators) {
                    fragmentCommunicator.sendData(id_konten);
                }
            }

//            @Override
//            public void onPageSelected(int position) {
//                for (int i=0; i<1; i++){
//                    FragmentCommunicator fragmentCommunicator = fragmentCommunicators.get(i);
//                    fragmentCommunicator.sendData(1);
//                }
//            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        Fragment fragment = new DonaturFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", 1);
//        fragment.setArguments(bundle);
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

package com.aji.donasi.activities;

import android.content.Intent;
import android.os.Bundle;

import com.aji.donasi.Helper;
import com.aji.donasi.KontenMessage;
import com.aji.donasi.Session;
import com.aji.donasi.api.KontenClient;
import com.aji.donasi.api.NetworkClient;
import com.aji.donasi.IsUserMessage;
import com.aji.donasi.fragments.DetailFragment;
import com.aji.donasi.models.Konten;
import com.aji.donasi.models.KontenResponse;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.aji.donasi.R;
import com.aji.donasi.fragments.DonaturFragment;
import com.aji.donasi.fragments.PerkembanganFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.graphics.Color.WHITE;

public class DetailActivity extends AppCompatActivity {

    private Button beriDonasi;
    private ImageView gambar;
    private String token;
    private ProgressBar progressBar;

    private static final String TAG = "DetailActivity";

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

        progressBar = findViewById(R.id.progBar);

        gambar = findViewById(R.id.gambar);
        beriDonasi = findViewById(R.id.beriDonasi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);

        collapsingToolbarLayout.setTitle(kontenMessage.getJudul());
        collapsingToolbarLayout.setContentScrimResource(R.color.colorPrimary);
        collapsingToolbarLayout.setCollapsedTitleTextColor(WHITE);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);

        String imagePath= Helper.IMAGE_URL_KONTEN +kontenMessage.getGambar();

        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.loading)
                .into(gambar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        beriDonasi.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, BeriDonasiActivity.class);
            startActivity(intent);
        });

        beriDonasi.setEnabled(kontenMessage.getStatus().equals("aktif"));

        if(Session.getInstance(DetailActivity.this).isLoggedIn()) {
            beriDonasi.setVisibility(View.GONE);
            token = Session.getInstance(DetailActivity.this).getToken();
            isUser();
        }
    }

    private void isUser(){
        progressBar.setVisibility(View.VISIBLE);

        Retrofit retrofit = NetworkClient.getApiClient();
        KontenClient kontenClient = retrofit.create(KontenClient.class);

        Call<KontenResponse> call = kontenClient.isUser(kontenMessage.getId(), token);

        call.enqueue(new Callback<KontenResponse>() {
            @Override
            public void onResponse(Call<KontenResponse> call, Response<KontenResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    EventBus.getDefault().postSticky(new IsUserMessage(true));
                    Log.d(TAG, "Is user iya");
                } else {
                    beriDonasi.setVisibility(View.VISIBLE);
                    EventBus.getDefault().postSticky(new IsUserMessage(false));
                    Log.d(TAG, "bukan user");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<KontenResponse> call, Throwable t) {
                Log.e(TAG, "Request gagal");
                progressBar.setVisibility(View.GONE);
            }
        });
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
        adapter.addFragment(new DetailFragment(), "Detail");
        adapter.addFragment(new DonaturFragment(), "Donatur");
        adapter.addFragment(new PerkembanganFragment(), "Perkembangan");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private Adapter(FragmentManager manager) {
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

package com.stanislavveliky.coachingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii on 7/17/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class ExistingClientActivity extends AppCompatActivity implements SessionListTabFragment.OnFragmentInteractionListener, EditClientFragment.OnFragmentInteractionListener {

    private static final String TAG = "ExistingClientActivity";
    TabLayout mTabLayout;
    ViewPager mViewPager;
    UUID mClientId;

    private static final String EXTRA_CLIENT_ID  = "com.stanislavveliky.coachingapp.client_id";

    public static Intent newIntent(Context packageContext, UUID uuid)
    {
        Intent intent = new Intent(packageContext, ExistingClientActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mClientId = (UUID) getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.client_info));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.sessions));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount(), mClientId);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

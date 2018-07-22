package com.stanislavveliky.coachingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumTabs;
    private UUID mClientId;

    public PagerAdapter(FragmentManager fm, int numTabs, UUID clientId)
    {
        super(fm);
        mNumTabs = numTabs;
        mClientId = clientId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return EditClientFragment.newInstance(mClientId);
            case 1: return SessionListTabFragment.newInstance(mClientId);
            default: return null;

        }
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }
}

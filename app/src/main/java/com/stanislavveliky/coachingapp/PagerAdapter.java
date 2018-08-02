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
    public static final int EDIT_CLIENT_FRAGMENT = 0;
    public static final int SESSION_LIST_FRAGMENT = 1;
    public static final int CONTACT_INFO_FRAGMENT = 2;

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
            case EDIT_CLIENT_FRAGMENT:
                return EditClientFragment.newInstance(mClientId);
            case SESSION_LIST_FRAGMENT:
                return SessionListTabFragment.newInstance(mClientId);
            case CONTACT_INFO_FRAGMENT:
                return ContactInfoFragment.newInstance(mClientId);
            default: return null;

        }
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }
}

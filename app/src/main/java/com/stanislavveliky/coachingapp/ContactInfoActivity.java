package com.stanislavveliky.coachingapp;

import android.support.v4.app.Fragment;

/**
 * Created by Stanislav Ostrovskii
 * Copyright (c) 2018 Stanislav Ostrovskii
 * This activity to hold ContactInfoFragment may be redundant and inefficient.
 * However, ContactInfoFragment does not have another activity to be placed in
 * when started via New Client Activity
 */

public class ContactInfoActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ContactInfoFragment();
    }
}

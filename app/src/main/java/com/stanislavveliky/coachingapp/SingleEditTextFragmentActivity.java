package com.stanislavveliky.coachingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.UUID;


/**
 * Created by Stanislav Ostrovskii
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class SingleEditTextFragmentActivity extends SingleFragmentActivity {
    private static final String EXTRA_FRAGMENT_TYPE = "com.stanislavveliky.coachingapp.fragment_type";
    private static final String EXTRA_CLIENT_ID = "com.stanislavveliky.coachingapp.client_id";
    static final int DIET_FRAGMENT = 0;
    static final int SLEEP_FRAGMENT = 1;
    static final int EXERCISE_FRAGMENT = 2;
    static final int SUBSTANCE_FRAGMENT = 3;
    static final int OTHER_FRAGMENT = 4;
    static final int EXPECTATIONS_FRAGMENT = 5;

    public static Intent newIntent(Context packageContext, int type, UUID uuid)
    {
        Intent intent = new Intent(packageContext, SingleEditTextFragmentActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_TYPE, type);
        intent.putExtra(EXTRA_CLIENT_ID, uuid);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int type = getIntent().getIntExtra(EXTRA_FRAGMENT_TYPE, DIET_FRAGMENT);
        Serializable uuid = getIntent().getSerializableExtra(EXTRA_CLIENT_ID);
        return SingleEditTextFragment.newInstance(type, uuid);
    }

}

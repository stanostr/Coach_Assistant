package com.stanislavveliky.coachingapp;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii on 7/20/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class SessionActivity extends SingleFragmentActivity {

    private static final String EXTRA_CLIENT_ID  = "com.stanislavveliky.coachingapp.client_id";
    private static final String EXTRA_SESSION_DATE = "com.stanislavveliky.coachingapp.session_date";

    @Override
    protected Fragment createFragment() {
        return SessionFragment.newInstance(getIntent().getSerializableExtra(EXTRA_CLIENT_ID), getIntent().getSerializableExtra(EXTRA_SESSION_DATE));
    }

    public static Intent newIntent(Context packageContext, UUID uuid, Date date)
    {
        Intent intent = new Intent(packageContext, SessionActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, uuid);
        intent.putExtra(EXTRA_SESSION_DATE, date);
        return intent;
    }
}

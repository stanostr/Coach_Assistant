package com.stanislavveliky.coachingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


import java.util.UUID;

public class NewClientActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment()
    {
        return EditClientFragment.newInstance(null);
    }


}

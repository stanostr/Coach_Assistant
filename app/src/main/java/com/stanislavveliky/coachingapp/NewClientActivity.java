package com.stanislavveliky.coachingapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stanislavveliky.coachingapp.model.Client;

public class NewClientActivity extends SingleFragmentActivity
        implements EditClientFragment.Callbacks {

    @Override
    protected Fragment createFragment()
    {
        return EditClientFragment.newInstance(null);
    }


    @Override
    public void onContactFragmentRequested(Client client) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContactInfoFragment fragment = ContactInfoFragment.newInstance(client.getId());
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}

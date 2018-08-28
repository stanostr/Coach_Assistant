package com.stanislavveliky.coachingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;


import static com.stanislavveliky.coachingapp.ContactInfoFragment.EXTRA_TIMEZONE;

/**
 * Created by Stanislav Ostrovskii on 8/8/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class TimezoneDialogFragment extends DialogFragment
implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "TimezoneDialogFragment";
    private static final String ARG_TIMEZONE = "timezone";
    private Spinner mSpinner;
    private DateTimeZone mTimeZone;


    public static TimezoneDialogFragment newInstance(DateTimeZone timeZone)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIMEZONE, timeZone);
        TimezoneDialogFragment fragment = new TimezoneDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle bundle)
    {
        mTimeZone = (DateTimeZone) getArguments().getSerializable(ARG_TIMEZONE);
        if(mTimeZone==null)
        {
            mTimeZone = DateTimeZone.getDefault();
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_spinner, null);
        mSpinner = view.findViewById(R.id.timezone_spinner);
        populateAndUpdateTimeZone();
        mSpinner.setOnItemSelectedListener(this);

        return new AlertDialog.Builder(getContext())
                .setView(view).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mTimeZone!=null) {
                    sendResult(Activity.RESULT_OK, mTimeZone);
                }
            }
        })
                .setTitle(R.string.select_timezone).show();

    }

    private void populateAndUpdateTimeZone() {

        //populate spinner with a simplified list of timezones
        ArrayList<DateTimeZone> idList = getTimezoneList();
        String[] displayNameArray = new String[idList.size()];
        for(int i=0; i<idList.size(); i++)
        {
            String name = idList.get(i).getName(new Date().getTime());
            displayNameArray[i] = name;
        }
        Arrays.sort(displayNameArray);
        ArrayAdapter idAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                displayNameArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(idAdapter);

        // now set the spinner to default timezone from the time zone settings
        for (int i = 0; i < idAdapter.getCount(); i++) {
            if (idAdapter.getItem(i).equals(mTimeZone.getName(new Date().getTime()))) {
                mSpinner.setSelection(i);
            }
        }
    }

    public static ArrayList<DateTimeZone> getTimezoneList()
    {
        ArrayList<DateTimeZone> timeZones = new ArrayList<>();
        for(String timezoneID: TimeZone.getAvailableIDs())
        {
            try {
                timeZones.add(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezoneID)));
            } catch (IllegalArgumentException e) {

            }
        }
        return timeZones;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        for (String availId : DateTimeZone.getAvailableIDs()) {
            if (item.trim().equalsIgnoreCase(DateTimeZone.forID(availId).getName(new Date().getTime()))) {
                mTimeZone = DateTimeZone.forID(availId);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendResult(int resultCode, DateTimeZone timezone)
    {
        if(getTargetFragment()==null)
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIMEZONE, timezone);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

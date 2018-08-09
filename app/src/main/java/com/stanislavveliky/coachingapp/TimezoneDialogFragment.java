package com.stanislavveliky.coachingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TimeZone mTimeZone;


    public static TimezoneDialogFragment newInstance(TimeZone timeZone)
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
        mTimeZone = (TimeZone) getArguments().getSerializable(ARG_TIMEZONE);
        if(mTimeZone==null)
        {
            mTimeZone = TimeZone.getDefault();
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
        ArrayList<TimeZone> idList = getFilteredTimezoneList();
        String[] displayNameArray = new String[idList.size()];
        for(int i=0; i<idList.size(); i++)
        {
            displayNameArray[i] = idList.get(i).getDisplayName();
        }
        ArrayAdapter idAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                displayNameArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(idAdapter);

        // now set the spinner to default timezone from the time zone settings
        for (int i = 0; i < idAdapter.getCount(); i++) {
            if (idAdapter.getItem(i).equals(mTimeZone.getDisplayName())) {
                mSpinner.setSelection(i);
            }
        }
    }

    public static ArrayList<TimeZone> getFilteredTimezoneList()
    {
        String[] temp = TimeZone.getAvailableIDs();
        List<String> timezoneList = new ArrayList<String>();
        ArrayList<TimeZone> simplifiedTimezoneList = new ArrayList<TimeZone>();
        for (String tz : temp)
        {
            timezoneList.add(tz);
        }
        Collections.sort(timezoneList);
        String filterList = "Canada|Mexico|Chile|Cuba|Brazil|Japan|Turkey|Mideast|Africa|America|Asia|Atlantic|Australia|Europe|Indian|Pacific";
        Pattern p = Pattern.compile("^(" + filterList + ").*");
        for (String tz : timezoneList)
        {
            Matcher m = p.matcher(tz);
            if (m.find())
            {
                TimeZone timeZone = TimeZone.getTimeZone(tz);
                Iterator<TimeZone> iter = simplifiedTimezoneList.iterator();
                while (iter.hasNext()) {
                    TimeZone t = iter.next();
                    if (t.getDisplayName().equals(timeZone.getDisplayName()))
                        iter.remove();
                }
                simplifiedTimezoneList.add(TimeZone.getTimeZone(tz));
            }
        }
        return simplifiedTimezoneList;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        for (String availId : TimeZone.getAvailableIDs()) {
            if (item.trim().equalsIgnoreCase(TimeZone.getTimeZone(availId).getDisplayName())) {
                mTimeZone = TimeZone.getTimeZone(availId);
                break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendResult(int resultCode, TimeZone timezone)
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

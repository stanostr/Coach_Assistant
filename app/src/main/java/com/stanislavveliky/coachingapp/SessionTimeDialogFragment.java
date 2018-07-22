package com.stanislavveliky.coachingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Stanislav Ostrovskii on 7/21/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class SessionTimeDialogFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    private TimePicker mTimePicker;

    public static SessionTimeDialogFragment newInstance(Date date)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        SessionTimeDialogFragment fragment = new SessionTimeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        mTimePicker = view.findViewById(R.id.session_time_picker);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }
        else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.choose_time_session)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour;
                        int minute;
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        }
                        else {
                           hour = mTimePicker.getCurrentHour();
                           minute = mTimePicker.getCurrentMinute();
                        }
                        Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                }).create();
    }

    private void sendResult(int resultCode, Date date)
    {
        if(getTargetFragment()==null)
        {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(SessionFragment.EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

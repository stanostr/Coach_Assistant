package com.stanislavveliky.coachingapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;
import com.stanislavveliky.coachingapp.model.Session;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class SessionFragment extends Fragment {
    private static final String TAG = "SessionFragment";
    private static final String ARG_SESSION_DATE = "session_date";
    private static final String ARG_CLIENT_ID = "client_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";
    public static final String EXTRA_DATE = "com.stanislavveliky.coaching_app.session_date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private Session mSession;
    private boolean mDateUnset;
    private boolean mTimeUnset;
    private ActionBar mActionBar;

    private Button mDateButton;
    private Button mTimeButton;
    private Button mSaveButton;
    private Button mDeleteButton;
    private EditText mSessionNotes;
    private Switch mPaidSwitch;

    private Date mSessionOriginalDate;

    public static Fragment newInstance(Serializable id, Serializable date) {

        SessionFragment fragment = new SessionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SESSION_DATE, date);
        args.putSerializable(ARG_CLIENT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        UUID clientId = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
        Date sessionDate = (Date) getArguments().getSerializable(ARG_SESSION_DATE);
        Client client = DatabaseHandler.get(getActivity()).getClientByID(clientId);
        mActionBar.setSubtitle(client.getDisplayName());
        if(sessionDate==null)
        {
            mActionBar.setTitle(R.string.new_session);
            mSession = new Session(clientId);
            mDateUnset = true;
            mTimeUnset = true;
        }
        else {
            String dateString = getResources().getString(R.string.session_with_semicolon) + " " +
                    DateFormat.getDateInstance(DateFormat.SHORT).format(sessionDate);
            mActionBar.setTitle(dateString);
            mSession = DatabaseHandler.get(getActivity()).getSession(clientId, sessionDate);
            mSessionOriginalDate = mSession.getSessionDate();
            mDateUnset = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_session_info, group, false);
        mDateButton = view.findViewById(R.id.session_date_button);
        mTimeButton = view.findViewById(R.id.session_time_button);
        if(!mDateUnset)
        {
            mDateButton.setText(DateFormat.getDateInstance().format(mSession.getSessionDate()));
            mTimeButton.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mSession.getSessionDate()));

        }
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DateDialogFragment dialog = DateDialogFragment.newInstance(mSession.getSessionDate(),
                        getResources().getString(R.string.choose_date_session));
                dialog.setTargetFragment(SessionFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
                mDateUnset = false;
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                SessionTimeDialogFragment dialog = SessionTimeDialogFragment.newInstance(mSession.getSessionDate());
                dialog.setTargetFragment(SessionFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        mSaveButton = view.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mDateUnset) {
                    if (mSessionOriginalDate != null) {
                        DatabaseHandler.get(getActivity()).updateSessionDate(mSession, mSessionOriginalDate);
                    }
                    else
                    {
                        if(mTimeUnset)
                        {
                            Calendar calendar =  Calendar.getInstance();
                            calendar.setTime(mSession.getSessionDate());
                            int year = calendar.get(calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = 0;
                            int minute = 0;
                            Date simplifiedDate = new GregorianCalendar(year, month, day, hour, minute).getTime();
                            mSession.setSessionDate(simplifiedDate);
                        }
                        DatabaseHandler.get(getActivity()).addSession(mSession);
                    }
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.set_session_date).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                }

            }
        });

        mDeleteButton = view.findViewById(R.id.delete_button);
        if(mDateUnset)
        {
            mDeleteButton.setEnabled(false);
        }
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.confirm_delete_session).setMessage(R.string.delete_session_dialog)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHandler.get(getActivity()).deleteSession(mSession);
                                NavUtils.navigateUpFromSameTask(getActivity());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();

            }
        });

        mPaidSwitch = view.findViewById(R.id.paid_switch);
        mPaidSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mSession.setPaid(isChecked);
            }
        });

        mSessionNotes = view.findViewById(R.id.session_notes_view);
        if(mSession.getSessionNotes()!=null)
        {
            mSessionNotes.setText(mSession.getSessionNotes());
        }
        mSessionNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSession.setSessionNotes(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }

    @Override
    public void onPause()
    {
        if(mDateUnset) DatabaseHandler.get(getActivity()).deleteSession(mSession);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode==REQUEST_DATE || requestCode==REQUEST_TIME)
        {
            Date date = (Date) data.getSerializableExtra(EXTRA_DATE);
            mSession.setSessionDate(date);
            if(requestCode == REQUEST_TIME)
            {
                mTimeButton.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(mSession.getSessionDate()));

            }
            else if (requestCode == REQUEST_DATE)
            {
                mDateButton.setText(DateFormat.getDateInstance().format(mSession.getSessionDate()));
                String dateString = getResources().getString(R.string.session_with_semicolon) + " " +
                        DateFormat.getDateInstance(DateFormat.SHORT).format(mSession.getSessionDate());
                mActionBar.setTitle(dateString);
                mDateUnset=false;
                mDeleteButton.setEnabled(true);
            }
        }
    }
}

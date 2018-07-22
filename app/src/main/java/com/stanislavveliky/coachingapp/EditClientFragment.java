package com.stanislavveliky.coachingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;

import java.util.UUID;

import static com.stanislavveliky.coachingapp.SingleEditTextFragmentActivity.*;

/**
 * Created by Stanislav Ostrovskii 7/5/2018
 * Copyright (c) 2018 Stanislav Ostrovskii
 * A fragment to display client information for viewing and editing.
 */

public class EditClientFragment extends Fragment {
    private static final String ARG_CLIENT_ID = "client_id";

    private Client mClient;

    private ActionBar mToolbar;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mDOBEditText;
    private EditText mLocationEditText;
    private EditText mTimeZoneEditText;

    private Switch mActiveSwitch;

    private Button mExpandDietButton;
    private Button mExpandSleepButton;
    private Button mExpandExerciseButton;
    private Button mExpandSubstanceButton;
    private Button mExpandOtherButton;
    private Button mExpandExpectationsButton;


    public static EditClientFragment newInstance(UUID id)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT_ID, id);
        EditClientFragment fragment = new EditClientFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID clientID = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
        mClient = DatabaseHandler.get(getActivity()).getClientByID(clientID);
        mToolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(mClient==null)
        {
            mClient = new Client();
            DatabaseHandler.get(getActivity()).addClient(mClient);
            mToolbar.setTitle(R.string.new_client);
        }
        else
        {
            mToolbar.setTitle(mClient.getDisplayName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_client_edit, group, false);


        mFirstNameEditText = view.findViewById(R.id.first_name_edit_text);
        if(mClient.getFirstName() != null)
        {
            mFirstNameEditText.setText(mClient.getFirstName());
        }
        mFirstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0 || charSequence.toString().trim()=="")
                    mClient.setFirstName(null);
                else mClient.setFirstName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLastNameEditText = view.findViewById(R.id.last_name_edit_text);
        if(mClient.getLastName() != null)
        {
            mLastNameEditText.setText(mClient.getLastName());

        }
        mLastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()==0 || charSequence.toString().trim()=="")
                    mClient.setLastName(null);
                else mClient.setLastName(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDOBEditText = view.findViewById(R.id.date_edit_text);
        if(mClient.getDateOfBirthString() != null)
        {
            mDOBEditText.setText(mClient.getDateOfBirthString());
        }
        mDOBEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0 || charSequence.toString().trim()=="")
                    mClient.setDateOfBirthString(null);
                else mClient.setDateOfBirthString(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLocationEditText = view.findViewById(R.id.location_edit_text);
        if(mClient.getLocation() != null)
        {
            mLocationEditText.setText(mClient.getLocation());
        }
        mLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0 || charSequence.toString().trim()=="")
                    mClient.setLocation(null);
                else mClient.setLocation(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mTimeZoneEditText = view.findViewById(R.id.timezone_edit_text);
        if(mClient.getTimeZone() != null)
        {
            mTimeZoneEditText.setText(mClient.getTimeZone());
        }
        mTimeZoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0 || charSequence.toString().trim()=="")
                    mClient.setTimeZone(null);
                else mClient.setTimeZone(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mActiveSwitch = view.findViewById(R.id.active_switch);
        mActiveSwitch.setChecked(mClient.isActive());
        mActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mClient.setActive(isChecked);
            }
        });

        mExpandDietButton = view.findViewById(R.id.expand_diet_button);
        mExpandDietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getFirstName()==null && mClient.getLastName()==null){
                    createNoNameDialog();
                }
                else {
                    Intent intent = SingleEditTextFragmentActivity.newIntent(getContext(),
                            DIET_FRAGMENT, mClient.getId());
                    startActivity(intent);
                }
            }
        });

        mExpandSleepButton = view.findViewById(R.id.expand_sleep_button);
        mExpandSleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getFirstName()==null && mClient.getLastName()==null){
                    createNoNameDialog();
                }
                else {
                    Intent intent = SingleEditTextFragmentActivity.newIntent(getContext(),
                            SLEEP_FRAGMENT, mClient.getId());
                    startActivity(intent);
                }
            }
        });

        mExpandExerciseButton = view.findViewById(R.id.expand_exercise_button);
        mExpandExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getFirstName()==null && mClient.getLastName()==null){
                    createNoNameDialog();
                }
                else {
                    Intent intent = SingleEditTextFragmentActivity.newIntent(getContext(),
                            EXERCISE_FRAGMENT, mClient.getId());
                    startActivity(intent);
                }
            }
        });

        mExpandSubstanceButton = view.findViewById(R.id.expand_substance_button);
        mExpandSubstanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getFirstName()==null && mClient.getLastName()==null){
                    createNoNameDialog();
                }
                else {
                    Intent intent = SingleEditTextFragmentActivity.newIntent(getContext(),
                            SUBSTANCE_FRAGMENT, mClient.getId());
                    startActivity(intent);
                }
            }
        });

        mExpandOtherButton = view.findViewById(R.id.expand_other_button);
        mExpandOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getFirstName()==null && mClient.getLastName()==null){
                    createNoNameDialog();
                }
                else {
                    Intent intent = SingleEditTextFragmentActivity.newIntent(getContext(),
                            OTHER_FRAGMENT, mClient.getId());
                    startActivity(intent);
                }
            }
        });

        mExpandExpectationsButton = view.findViewById(R.id.expand_expectations_button);
        mExpandExpectationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getFirstName()==null && mClient.getLastName()==null){
                    createNoNameDialog();
                }
                else {
                    Intent intent = SingleEditTextFragmentActivity.newIntent(getContext(),
                            EXPECTATIONS_FRAGMENT, mClient.getId());
                    startActivity(intent);
                }
            }
        });

        return view;
    }
    @Override
    public void onResume()
    {
        //FIXME this is redundant. Need more efficient implementation.
        mClient = DatabaseHandler.get(getActivity()).getClientByID(mClient.getId());
        super.onResume();
    }


    @Override
    public void onPause()
    {
        super.onPause();
        if(mClient.isEmpty())
        {
            DatabaseHandler.get(getActivity()).deleteClient(mClient.getId());
        }
        else {
            DatabaseHandler.get(getActivity()).updateClient(mClient);
        }
    }

    private void createNoNameDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.no_name).setMessage(R.string.set_client_name_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

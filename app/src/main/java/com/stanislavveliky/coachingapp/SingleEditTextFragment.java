package com.stanislavveliky.coachingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;

import java.io.Serializable;
import java.util.UUID;
/**
 * Created by Stanislav Ostrovskii on 7/5/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class SingleEditTextFragment extends Fragment {
    static final int DIET_FRAGMENT = 0;
    static final int SLEEP_FRAGMENT = 1;
    static final int EXERCISE_FRAGMENT = 2;
    static final int SUBSTANCE_FRAGMENT = 3;
    static final int OTHER_FRAGMENT = 4;
    static final int EXPECTATIONS_FRAGMENT = 5;
    private static final String ARG_FRAGMENT_TYPE = "fragment_type";
    private static final String ARG_CLIENT_ID = "cliend_id";
    private int mType;
    private Client mClient;
    private EditText mEditText;

    public static SingleEditTextFragment newInstance(int type, Serializable id)
    {
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_FRAGMENT_TYPE, type);
        arguments.putSerializable(ARG_CLIENT_ID, id);
        SingleEditTextFragment fragment = new SingleEditTextFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        mType = getArguments().getInt(ARG_FRAGMENT_TYPE);
        UUID uuid = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
        mClient = DatabaseHandler.get(getActivity()).getClientByID(uuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.single_edit_text_fragment, group, false);
        mEditText = view.findViewById(R.id.main_edit_text);
        if(mClient!=null) {
            String toolbarText = mClient.getDisplayName() + ": ";
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(toolbarText);
            switch(mType)
            {
                case DIET_FRAGMENT:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.diet);
                    mEditText.setText(mClient.getDiet());
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            mClient.setDiet(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                case SLEEP_FRAGMENT:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.sleep_patterns);
                    mEditText.setHint(mClient.getSleep());
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            mClient.setSleep(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                case EXERCISE_FRAGMENT:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.exercise);
                    mEditText.setHint(mClient.getExercise());
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            mClient.setExercise(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                case SUBSTANCE_FRAGMENT:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.substance_use);
                    mEditText.setHint(mClient.getSubstanceUse());
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            mClient.setSubstanceUse(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                case OTHER_FRAGMENT:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.additional_info);
                    mEditText.setHint(mClient.getOtherDetails());
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            mClient.setOtherDetails(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
                case EXPECTATIONS_FRAGMENT:
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.expectations);
                    mEditText.setHint(mClient.getExpectations());
                    mEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            mClient.setExpectations(charSequence.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                    break;
            }
        }
        else
        {
            mEditText.setText(R.string.error_text);
            mEditText.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onPause()
    {
        DatabaseHandler.get(getActivity()).updateClient(mClient);
        super.onPause();
    }

}

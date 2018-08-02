package com.stanislavveliky.coachingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii 7/5/2018
 * Copyright (c) 2018 Stanislav Ostrovskii
 * A fragment to display client contact information for viewing, editing, and contacting the client.
 */

public class ContactInfoFragment extends Fragment {
    private static final String ARG_CLIENT_ID = "client_id";
    private Client mClient;

    private ImageButton mEditPhone;
    private ImageButton mEditEmail;
    private ImageButton mEditLocation;
    private ImageButton mEditTimeZone;

    private Button mCallButton;
    private Button mEmailButton;
    private Button mLocationLookupButton;

    private EditText mPhoneText;
    private EditText mEmailText;
    private EditText mLocationText;
    private EditText mTimezoneText;

    public static ContactInfoFragment newInstance(UUID clientId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT_ID, clientId);
        ContactInfoFragment fragment = new ContactInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        UUID uuid = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
        mClient = DatabaseHandler.get(getActivity()).getClientByID(uuid);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(mClient!=null)
        {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle(mClient.getDisplayName());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_contact_view, group, false);
        mEditPhone = view.findViewById(R.id.edit_phone_button);
        mEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallButton.setVisibility(View.GONE);
                mPhoneText.setVisibility(View.VISIBLE);
                mPhoneText.requestFocus();
            }
        });
        mEditEmail = view.findViewById(R.id.edit_email_button);
        mEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmailButton.setVisibility(View.GONE);
                mEmailText.setVisibility(View.VISIBLE);
                mEmailText.requestFocus();
            }
        });
        mEditLocation = view.findViewById(R.id.edit_location_button);
        mEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLocationLookupButton.setVisibility(View.GONE);
                mLocationText.setVisibility(View.VISIBLE);
                mLocationText.requestFocus();
            }
        });
        mEditTimeZone = view.findViewById(R.id.edit_timezone_button);

        mCallButton = view.findViewById(R.id.phone_call_button);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getPhoneNumber()!=null)
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.call_prompt)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                                    dialIntent.setData(Uri.parse("tel:"+ mClient.getPhoneNumber()));//change the number
                                    startActivity(dialIntent);
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_menu_call).show();
                }
            }
        });
        mEmailButton = view.findViewById(R.id.email_button);
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getEmail()!=null)
                {
                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mClient.getEmail()});
                    startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_mail)));
                }
            }
        });

        mLocationLookupButton = view.findViewById(R.id.location_button);
        mLocationLookupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getLocation()!=null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.open_maps_prompt)
                          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int which) {
                                    String locationString = mLocationLookupButton.getText().toString()
                                        .replaceAll(" ", "+");
                                    locationString = "geo:0,0?q=" + locationString;
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(locationString));
                                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                }
                            }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_map).show();
                }
            }
        });

        mPhoneText = view.findViewById(R.id.phone_edit_text);
        if(mClient.getPhoneNumber()!=null)
        {
            mPhoneText.setText(mClient.getPhoneNumber());
            mCallButton.setText(mClient.getPhoneNumber());
        }
        mPhoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()==0 || charSequence.toString().trim()=="")
                    mClient.setPhoneNumber(null);
                else mClient.setPhoneNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPhoneText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mClient.setPhoneNumber(mPhoneText.getText().toString());
                    mPhoneText.setVisibility(View.GONE);
                    mCallButton.setVisibility(View.VISIBLE);
                    mCallButton.setText(mClient.getPhoneNumber());
                    DatabaseHandler.get(getActivity()).updateClient(mClient);
                }
            }
        });

        mEmailText = view.findViewById(R.id.email_edit_text);
        if(mClient.getEmail()!=null)
        {
            mEmailText.setText(mClient.getEmail());
            mEmailButton.setText(mClient.getEmail());
        }
        mEmailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()!=0)
                    mClient.setEmail(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEmailText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    mClient.setEmail(mEmailText.getText().toString());
                    mEmailText.setVisibility(View.GONE);
                    mEmailButton.setVisibility(View.VISIBLE);
                    mEmailButton.setText(mClient.getEmail());
                    DatabaseHandler.get(getActivity()).updateClient(mClient);
                }
            }
        });

        mLocationText = view.findViewById(R.id.location_text);
        if(mClient.getLocation()!=null)
        {
            mLocationText.setText(mClient.getLocation());
            mLocationLookupButton.setText(mClient.getLocation());
        }
        mLocationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mClient.setLocation(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLocationText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus)
                {
                    mClient.setLocation(mLocationText.getText().toString());
                    mLocationText.setVisibility(View.GONE);
                    mLocationLookupButton.setVisibility(View.VISIBLE);
                    mLocationLookupButton.setText(mClient.getLocation());
                    DatabaseHandler.get(getActivity()).updateClient(mClient);
                }
            }
        });

        mTimezoneText = view.findViewById(R.id.timezone_text);
        if(mClient.getTimeZoneString()!=null)
        {
            mLocationText.setText(mClient.getTimeZoneString());
        }
        return view;
    }

    private View.OnClickListener timezoneClickListener()
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

}

package com.stanislavveliky.coachingapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.ImageButton;

import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;

import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii 7/5/2018
 * Copyright (c) 2018 Stanislav Ostrovskii
 * A fragment to display client contact information for viewing, editing, and contacting the client.
 */

public class ContactInfoFragment extends Fragment {
    public static final String EXTRA_TIMEZONE = "com.stanislavveliky.coaching_app.client_timezone";

    private static final String ARG_CLIENT_ID = "client_id";
    private static final int REQUEST_TIMEZONE = 0;
    private static final String DIALOG_TIMEZONE = "DialogTimezone";
    private static final String TAG = "ContactInfoFragment";
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
    private Button mTimezoneButton;

    private DateTimeZone mClientZone;

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
        if(mClient.getTimeZoneString()!=null) {
            mClientZone = DateTimeZone.forID(mClient.getTimeZoneString());
        }
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
        mEditTimeZone.setOnClickListener(timezoneClickListener());

        mCallButton = view.findViewById(R.id.phone_call_button);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mClient.getPhoneNumber()!=null)
                {
                    if(mClient.getTimeZoneString()==null)
                    {
                        noTimezoneIssuesDialog();
                        return;
                    }
                    TimeZone tz = DateTimeZone.forID(mClient.getTimeZoneString()).toTimeZone();

                    Calendar c = Calendar.getInstance(tz);
                    boolean isDST = tz.inDaylightTime(new Date());
                    if(isDST) {
                        int sec = tz.getDSTSavings() / 1000;// for no. of seconds
                        Log.d(TAG, " is in DST " + sec);
                        c.add(Calendar.SECOND, sec);
                    }
                    if(c.get(Calendar.HOUR_OF_DAY)<8 || c.get(Calendar.HOUR_OF_DAY) >21) {
                        String am_pm = " AM";
                        if (c.get(Calendar.AM_PM) == Calendar.PM) {
                            am_pm = " PM";
                        }

                        String time = String.format("%02d", c.get(Calendar.HOUR)) + ":" +
                                String.format("%02d", c.get(Calendar.MINUTE)) + am_pm;
                        new AlertDialog.Builder(getActivity()).setTitle(R.string.call_prompt)
                                .setIcon(android.R.drawable.ic_menu_call)
                                .setMessage(String.format(getResources().getString(R.string.client_time_warning), time))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startCallActivity();
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                    else {
                       noTimezoneIssuesDialog();
                    }
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

        mTimezoneButton = view.findViewById(R.id.timezone_text);
        if(mClient.getTimeZoneString()!=null)
        {
            try {
                mTimezoneButton.setText(getTimezoneDisplayName());
            } catch (NullPointerException e) {
                mTimezoneButton.setText(R.string.error_text);
            }
        }
        mTimezoneButton.setOnClickListener(timezoneClickListener());
        return view;
    }


    private View.OnClickListener timezoneClickListener()
    {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                TimezoneDialogFragment dialog = TimezoneDialogFragment.newInstance(mClientZone);
                dialog.setTargetFragment(ContactInfoFragment.this, REQUEST_TIMEZONE);
                dialog.show(fragmentManager, DIALOG_TIMEZONE);
            }
        };
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode==REQUEST_TIMEZONE)
        {
            mClientZone = (DateTimeZone) data.getSerializableExtra(EXTRA_TIMEZONE);
            mClient.setTimeZoneString(mClientZone.getID());
            mTimezoneButton.setText(mClientZone.getName(new Date().getTime()));
            DatabaseHandler.get(getActivity()).updateClient(mClient);
        }
    }

    private String getTimezoneDisplayName()
    {
        return TimeZone.getTimeZone(mClient.getTimeZoneString()).getDisplayName();
    }

    private void startCallActivity()
    {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:"+ mClient.getPhoneNumber()));//change the number
        startActivity(dialIntent);
    }

    private void noTimezoneIssuesDialog()
    {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.call_prompt)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startCallActivity();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setIcon(android.R.drawable.ic_menu_call).show();
    }


}

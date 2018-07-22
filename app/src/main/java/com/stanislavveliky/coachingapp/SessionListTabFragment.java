package com.stanislavveliky.coachingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;
import com.stanislavveliky.coachingapp.model.Session;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.UUID;

import static com.stanislavveliky.coachingapp.model.DatabaseHandler.NEWEST_FIRST;
import static com.stanislavveliky.coachingapp.model.DatabaseHandler.OLDEST_FIRST;
import static com.stanislavveliky.coachingapp.model.DatabaseHandler.PAID_FIRST;
import static com.stanislavveliky.coachingapp.model.DatabaseHandler.UNPAID_FIRST;


/**
 * Created by Stanislav Ostrovskii on 7/17/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class SessionListTabFragment extends Fragment {
    private static final String ARG_CLIENT_ID = "client_id";
    private static final String TAG = "SessionTab";

    private Client mClient;
    private ActionBar mToolbar;
    private RecyclerView mSessionListView;
    private TextView mNotificationText;
    private Button mOrderMenuButton;
    private ImageButton mAddSessionButton;
    private SessionAdapter mSessionAdapter;

    public static SessionListTabFragment newInstance(UUID clientId)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CLIENT_ID, clientId);
        SessionListTabFragment fragment = new SessionListTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UUID clientId = (UUID) getArguments().getSerializable(ARG_CLIENT_ID);
        mClient = DatabaseHandler.get(getActivity()).getClientByID(clientId);
        mToolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(mClient==null)
        {
            mToolbar.setTitle(R.string.error);
        }
        else
        {
            mToolbar.setTitle(mClient.getDisplayName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_session_tab, group, false);
        mOrderMenuButton = view.findViewById(R.id.filter_sessions_button);
        mOrderMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu orderMenu = new PopupMenu(getActivity(), mOrderMenuButton);
                orderMenu.getMenuInflater().inflate(R.menu.menu_filter_session, orderMenu.getMenu());
                orderMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId())
                        {
                            case R.id.newest_first:
                                mOrderMenuButton.setText(R.string.order_newest_first);
                                updateRecyclerView(NEWEST_FIRST);
                                return true;
                            case R.id.oldest_first:
                                mOrderMenuButton.setText(R.string.order_oldest_first);
                                updateRecyclerView(OLDEST_FIRST);
                                return true;
                            case R.id.paid:
                                mOrderMenuButton.setText(R.string.order_paid_first);
                                updateRecyclerView(PAID_FIRST);
                                return true;
                            case R.id.unpaid:
                                mOrderMenuButton.setText(R.string.order_unpaid_first);
                                updateRecyclerView(UNPAID_FIRST);
                                return true;
                            default: return true;
                        }
                    }
                });
                orderMenu.show();
            }
        });

        mAddSessionButton = view.findViewById(R.id.add_session_button);
        mAddSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SessionActivity.newIntent(getActivity(), mClient.getId(), null);
                startActivity(intent);
            }
        });

        mNotificationText = view.findViewById(R.id.session_list_notification_text);

        mSessionListView = view.findViewById(R.id.session_list_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if(mSessionListView!=null)
        {
            mSessionListView.setLayoutManager(layoutManager);
            updateRecyclerView(NEWEST_FIRST);
        }
        else{
            mNotificationText.setText(R.string.error_text);
            mNotificationText.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class SessionHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Session mSession;
        private TextView mDateView;
        private TextView mPaidView;

        public SessionHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mDateView = itemView.findViewById(R.id.session_date_view);
            mPaidView = itemView.findViewById(R.id.session_paid_view);
        }

        @Override
        public void onClick(View view) {
            Intent intent = SessionActivity.newIntent(getContext(),
                    mSession.getClientId(), mSession.getSessionDate());
            startActivity(intent);
        }

        public void bindSession(Session session)
        {
            mSession = session;
            String formattedDate = DateFormat.getDateInstance(DateFormat.SHORT).format(mSession.getSessionDate());
            mDateView.setText(formattedDate);
            if(mSession.isPaid())
            {
                mPaidView.setText(R.string.paid);
                mPaidView.setTextColor(getResources().getColor(R.color.colorGreen));
            }
            else {
                mPaidView.setText(R.string.unpaid);
                mPaidView.setTextColor(getResources().getColor(R.color.colorRed));
            }
        }

    }

    private void updateRecyclerView(int orderCode)
    {
        ArrayList<Session> sessions = DatabaseHandler.get(getActivity()).getSessions(orderCode, mClient.getId());
        if(sessions.size()==0)
        {
            mNotificationText.setVisibility(View.VISIBLE);
            mNotificationText.setText(R.string.no_sessions);
        }
        else {
            mNotificationText.setVisibility(View.GONE);
        }

        if(mSessionAdapter==null)
        {
            mSessionAdapter = new SessionAdapter(sessions);
            mSessionListView.setAdapter(mSessionAdapter);
        }
        else
        {
            mSessionAdapter.setSessions(sessions);
            mSessionAdapter.notifyDataSetChanged();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private class SessionAdapter extends RecyclerView.Adapter<SessionHolder>{
        private ArrayList<Session> mSessions;

        public SessionAdapter(ArrayList<Session> sessions) {
            mSessions = sessions;
        }

        public void setSessions (ArrayList<Session> sessions)
        {
            mSessions = sessions;
        }

        @NonNull
        @Override
        public SessionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_session, parent, false);
            return new SessionHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull SessionHolder holder, int position) {
            Session session = mSessions.get(position);
            holder.bindSession(session);
        }

        @Override
        public int getItemCount() {
            return mSessions.size();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() called");
        updateRecyclerView(NEWEST_FIRST);
    }

}

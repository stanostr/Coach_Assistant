package com.stanislavveliky.coachingapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by Stanislav Ostrovskii on 6/29/2018.
 */

public class ClientListFragment extends Fragment {
    private RecyclerView mClientListView;
    private ClientAdapter mClientAdapter;
    private TextView mNotificationText;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_client_list, group, false);

        mClientListView = view.findViewById(R.id.client_list_view);
        mNotificationText = view.findViewById(R.id.client_list_notification_text);
        mClientListView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateRecyclerView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        updateRecyclerView();
    }

    private class ClientAdapter extends RecyclerView.Adapter<ClientHolder>
    {
        private ArrayList<Client> mClients;

        public ClientAdapter(ArrayList<Client> clients)
        {
            mClients = clients;
        }

        public void setClients(ArrayList<Client> clients)
        {
            mClients = clients;
        }

        @Override
        public ClientHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_client, parent, false);
            return new ClientHolder(view);
        }

        @Override
        public void onBindViewHolder(ClientHolder clientHolder, int position)
        {
            Client client = mClients.get(position);
            clientHolder.bindClient(client);
        }

        public int getItemCount()
        {
            return mClients.size();
        }
    }

    private void updateRecyclerView()
    {
        DatabaseHandler databaseHandler = DatabaseHandler.get(getActivity());
        ArrayList<Client> clients = databaseHandler.getClients();
        if(clients.size()==0)
        {
            mNotificationText.setVisibility(View.VISIBLE);
            mNotificationText.setText(R.string.no_clients);
        }
        else {
            //possibly redundant but it was malfunctioning prior
            mNotificationText.setVisibility(View.GONE);
        }

        if(mClientAdapter==null)
        {

            mClientAdapter = new ClientAdapter(clients);
            mClientListView.setAdapter(mClientAdapter);
        }
        else
        {
            mClientAdapter.setClients(clients);
            mClientAdapter.notifyDataSetChanged();
        }

    }

    private class ClientHolder extends RecyclerView.ViewHolder  implements View.OnClickListener
    {
        private Client mClient;
        private TextView mClientNameView;
        private ImageView mInactiveIcon;
        private ImageButton mDeleteClientButton;

        public ClientHolder(View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);
            mClientNameView = itemView.findViewById(R.id.client_name_text_view);
            mInactiveIcon = itemView.findViewById(R.id.inactive_icon);
            mDeleteClientButton = itemView.findViewById(R.id.delete_client_button);
            mDeleteClientButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.confirm_delete_client).setMessage(R.string.delete_client_dialog)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseHandler.get(getActivity()).deleteClient(mClient.getId());
                            updateRecyclerView();
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
        }

        public void onClick(View view)
        {
            Intent intent = ExistingClientActivity.newIntent(getContext(), mClient.getId());
            startActivity(intent);
        }

        public void bindClient(Client client)
        {
            mClient = client;
            mClientNameView.setText(client.getDisplayName());
            if(mClient.isActive())
            {
                mInactiveIcon.setVisibility(View.INVISIBLE);
            }
            else {
                mInactiveIcon.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_client_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.add_client:
                Intent intent = new Intent(getActivity(), NewClientActivity.class);
                startActivity(intent);
                return true;
            case R.id.search_clients:
                getActivity().onSearchRequested();
                return true;
            default: return true;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateRecyclerView();
    }
}

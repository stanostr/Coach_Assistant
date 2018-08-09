package com.stanislavveliky.coachingapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.stanislavveliky.coachingapp.database.ApplicationCursorWrapper;
import com.stanislavveliky.coachingapp.database.DbSchema.ClientTable;
import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.DatabaseHandler;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii on 7/31/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 * An activity to handle searches of clients by name.
 */

public class SearchableClientActivity extends AppCompatActivity {
    private TextView mTopTextView;
    private ListView mListView;

    public void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_searchable);
        mListView = findViewById(R.id.client_search_list_view);
        mTopTextView = findViewById(R.id.client_search_top_text);
        handleIntent(getIntent());
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            showResults(query);
        }
    }

    private void showResults(String query)
    {
        query = query.trim();
        String[] whereArgs = {"%" + query + "%", "%" + query + "%"};
        String whereClause = ClientTable.Cols.FIRST_NAME  + " like ? collate nocase or "
                + ClientTable.Cols.LAST_NAME + " like ? collate nocase";
        if(query.contains(" ")) {
            whereArgs = query.split(" ");
            whereArgs[0] =  "%" + whereArgs[0];
            whereArgs[1] =  whereArgs[1] + "%";
            whereClause = ClientTable.Cols.FIRST_NAME  + " like ? collate nocase and "
                    + ClientTable.Cols.LAST_NAME + " like ? collate nocase";
        }


        ApplicationCursorWrapper cursorWrapper = DatabaseHandler.get(this).queryClients(whereClause, whereArgs);
        if(cursorWrapper==null)
        {
            mTopTextView.setText(getString(R.string.no_results, new Object[] {query}));

        }
        else {
            final ArrayList<Client> clients = DatabaseHandler
                    .get(getApplicationContext()).getClientsWithSearchStringInName(query);
            int count = cursorWrapper.getCount();
            String topString = getResources().getQuantityString(R.plurals.client_search_results,
                    count, new Object[] {count, query});
            mTopTextView.setText(topString);
            SimpleCursorAdapter results = new SimpleCursorAdapter(this,
                    R.layout.result_client, cursorWrapper,
                    new String[]{ClientTable.Cols.FIRST_NAME, ClientTable.Cols.LAST_NAME},
                    new int[]{R.id.result_first_name, R.id.result_last_name});
            mListView.setAdapter(results);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UUID uuid = clients.get(position).getId();
                    Intent intent = ExistingClientActivity.newIntent(getApplicationContext(), uuid);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_client, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.search_clients:
                onSearchRequested();
                return true;
            default: return false;
        }
    }

}

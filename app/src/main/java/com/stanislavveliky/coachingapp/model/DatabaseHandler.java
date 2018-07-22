package com.stanislavveliky.coachingapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.stanislavveliky.coachingapp.database.ClientDatabaseHelper;
import com.stanislavveliky.coachingapp.database.ApplicationCursorWrapper;
import com.stanislavveliky.coachingapp.database.DbSchema.ClientTable;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static com.stanislavveliky.coachingapp.database.DbSchema.*;

/**
 * Created by Stanislav Ostrovskii on 6/29/2018.
 * A singleton class to manage the client list.
 */

public class DatabaseHandler {
    private static final String TAG = "DatabaseHandler";
    public static final int NEWEST_FIRST = 0;
    public static final int OLDEST_FIRST = 1;
    public static final int PAID_FIRST = 2;
    public static final int UNPAID_FIRST = 3;

    private static DatabaseHandler sDatabaseHandler;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DatabaseHandler get(Context context)
    {
        if(sDatabaseHandler ==null)
        {
            sDatabaseHandler = new DatabaseHandler(context);
        }
        return sDatabaseHandler;
    }

    private DatabaseHandler(Context context)
    {
        mContext = context;
        mDatabase = new ClientDatabaseHelper(mContext).getWritableDatabase();
    }

    public ArrayList<Client> getClients() {
        ArrayList<Client> clientList = new ArrayList<>();
        ApplicationCursorWrapper cursorWrapper = queryClients(null, null);
        try {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast())
            {
                clientList.add(cursorWrapper.getClient());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return clientList;
    }

    public Client getClientByID(UUID uuid)
    {
        if(uuid==null) return null;
        ApplicationCursorWrapper cursorWrapper = queryClients(ClientTable.Cols.ID + " = ?", new String[] {uuid.toString()});
        try {
            if(cursorWrapper.getCount()==0)
            {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getClient();
        } finally {
            cursorWrapper.close();
        }
    }

    public void addClient(Client client)
    {
        ContentValues values = getClientContentValues(client);
        mDatabase.insert(ClientTable.NAME, null, values);
    }

    public void deleteClient(UUID uuid)
    {
        //first deletes sessions for the client, then deletes the client.
        mDatabase.delete(SessionTable.NAME, SessionTable.Cols.CLIENT_ID + " = ?", new String[] {uuid.toString()});
        mDatabase.delete(ClientTable.NAME, ClientTable.Cols.ID + " = ?", new String[] {uuid.toString()});
    }

    public void updateClient(Client client)
    {
        String uuidString = client.getId().toString();
        ContentValues values = getClientContentValues(client);
        mDatabase.update(ClientTable.NAME, values,
                ClientTable.Cols.ID +
                        "= ?", new String[] { uuidString});
    }

    public ArrayList<Session> getSessions(int orderCode, UUID clientId)
    {
        String orderBy;
        switch(orderCode)
        {
            case NEWEST_FIRST:   orderBy= SessionTable.Cols.DATE + " DESC";
                break;
            case OLDEST_FIRST:   orderBy= SessionTable.Cols.DATE + " ASC";
                break;
            case PAID_FIRST: orderBy = SessionTable.Cols.IS_PAID + " DESC, " +
                    SessionTable.Cols.DATE + " DESC";
                break;
            case UNPAID_FIRST: orderBy = SessionTable.Cols.IS_PAID + " ASC, " +
                    SessionTable.Cols.DATE + " DESC";
                break;
            default: orderBy = null;
        }


        ArrayList<Session> sessionList = new ArrayList<>();
        ApplicationCursorWrapper cursorWrapper = querySessions(SessionTable.Cols.CLIENT_ID + "= ?", new String[] {clientId.toString()}, orderBy);
        try {
            cursorWrapper.moveToFirst();
            while(!cursorWrapper.isAfterLast())
            {
                sessionList.add(cursorWrapper.getSession());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return sessionList;
    }

    public Session getSession(UUID uuid, Date date)
    {
        if(uuid==null || date==null) return null;
        ApplicationCursorWrapper cursorWrapper = querySessions(SessionTable.Cols.CLIENT_ID + " = ? AND " + SessionTable.Cols.DATE + "= ?", new String[] {uuid.toString(), String.valueOf(date.getTime())}, null);
        try {
            if(cursorWrapper.getCount()==0)
            {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getSession();
        } finally {
            cursorWrapper.close();
        }
    }

    public void addSession(Session session)
    {
        ContentValues values = getSessionContentValues(session);
        mDatabase.insert(SessionTable.NAME, null, values);
    }

    /**
     * Use this method to update any Session fields EXCEPT the Date.
     * You must use updateSessionDate to update the Date!
     * Using this method to update the date will have no effect and will not save any changes!
     * @param session
     */
    public void updateSession(Session session) {
        ContentValues values = getSessionContentValues(session);
        String uuidString = session.getClientId().toString();
        String dateString = String.valueOf(session.getSessionDate().getTime());
        mDatabase.update(SessionTable.NAME, values,
                SessionTable.Cols.CLIENT_ID +
                        " = ? AND " + SessionTable.Cols.DATE + " = ?", new String[] { uuidString, dateString});

    }

    /**
     * Due to the date being part of the primary key, this method must be used to update the date in a session.
     * The first parameter should be the session with the new date.
     * @param session with the new date
     * @param oldDate
     */
    public void updateSessionDate(Session session, Date oldDate)
    {
        ContentValues values = getSessionContentValues(session);
        String uuidString = session.getClientId().toString();
        String dateString = String.valueOf(oldDate.getTime());
        mDatabase.update(SessionTable.NAME, values,
                SessionTable.Cols.CLIENT_ID +
                        " = ? AND " + SessionTable.Cols.DATE + " = ?", new String[] { uuidString, dateString});
    }

    public void deleteSession(UUID uuid, Date date)
    {
        mDatabase.delete(SessionTable.NAME,
                SessionTable.Cols.CLIENT_ID + " = ? AND "  + SessionTable.Cols.DATE + " = ?",
                new String[] {
                    uuid.toString(), String.valueOf(date.getTime())
                });
    }

    public void deleteSession(Session session)
    {
        String uuidString = session.getClientId().toString();
        String dateString = String.valueOf(session.getSessionDate().getTime());
        mDatabase.delete(SessionTable.NAME,
                SessionTable.Cols.CLIENT_ID + " = ? AND "  + SessionTable.Cols.DATE + " = ?",
                new String[] { uuidString, dateString});
    }

    private static ContentValues getClientContentValues(Client client)
    {
        ContentValues values = new ContentValues();
        values.put(ClientTable.Cols.ID, client.getId().toString());
        values.put(ClientTable.Cols.FIRST_NAME, client.getFirstName());
        values.put(ClientTable.Cols.LAST_NAME, client.getLastName());
        values.put(ClientTable.Cols.LOCATION, client.getLocation());
        values.put(ClientTable.Cols.TIME_ZONE, client.getTimeZone());
        values.put(ClientTable.Cols.DOB, client.getDateOfBirthString());
        values.put(ClientTable.Cols.GENDER, (client.isGender())? 1: 0);
        values.put(ClientTable.Cols.EXERCISE, client.getExercise());
        values.put(ClientTable.Cols.DIET, client.getDiet());
        values.put(ClientTable.Cols.SUBSTANCE, client.getSubstanceUse());
        values.put(ClientTable.Cols.SLEEP, client.getSleep());
        values.put(ClientTable.Cols.OTHER, client.getOtherDetails());
        values.put(ClientTable.Cols.EXPECTATIONS, client.getExpectations());
        values.put(ClientTable.Cols.IS_ACTIVE, (client.isActive())? 1: 0);

        return values;
    }

    private static ContentValues getSessionContentValues(Session session) {
        ContentValues values = new ContentValues();
        values.put(SessionTable.Cols.CLIENT_ID, session.getClientId().toString());
        values.put(SessionTable.Cols.DATE, session.getSessionDate().getTime());
        values.put(SessionTable.Cols.NOTES, session.getSessionNotes());
        values.put(SessionTable.Cols.IS_PAID, (session.isPaid()) ? 1 : 0);
        return values;
    }

    @NonNull
    private ApplicationCursorWrapper queryClients(String whereClause, String[] whereArgs)
    {
        Cursor cursor =  mDatabase.query(ClientTable.NAME, null,
                whereClause, whereArgs, null, null, null);
        return new ApplicationCursorWrapper(cursor);
    }

    private ApplicationCursorWrapper querySessions(String whereClause, String [] whereArgs, String orderBy)
    {
        Cursor cursor = mDatabase.query(SessionTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy);
        return new ApplicationCursorWrapper(cursor);
    }
}

package com.stanislavveliky.coachingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stanislavveliky.coachingapp.database.DbSchema.*;
import com.stanislavveliky.coachingapp.model.Client;

/**
 * Created by Stanislav Ostrovskii on 7/12/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class ClientDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 6;
    private static final String DATABASE_NAME = "ClientDatabase.db";

    public ClientDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + ClientTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ClientTable.Cols.ID + " text unique, " +
                ClientTable.Cols.FIRST_NAME + " text, " +
                ClientTable.Cols.LAST_NAME + " text, " +

                ClientTable.Cols.LOCATION + " text, " +
                ClientTable.Cols.PHONE_NUMBER + " text, " +
                ClientTable.Cols.EMAIL + " text, " +
                ClientTable.Cols.TIME_ZONE + " text, " +

                ClientTable.Cols.DOB + " text, " +
                ClientTable.Cols.DATE_OF_BIRTH + " text, " +
                ClientTable.Cols.GENDER + " integer, " +
                ClientTable.Cols.EXERCISE + " text, " +
                ClientTable.Cols.DIET + " text, " +
                ClientTable.Cols.SUBSTANCE + " text, " +
                ClientTable.Cols.SLEEP + " text, " +
                ClientTable.Cols.OTHER + " text, " +
                ClientTable.Cols.EXPECTATIONS + " text , " +
                ClientTable.Cols.IS_ACTIVE + " integer )"
        );
        db.execSQL("create table " + SessionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SessionTable.Cols.CLIENT_ID + " text, " +
                SessionTable.Cols.DATE + " text unique, " +
                SessionTable.Cols.NOTES + " text, " +
                SessionTable.Cols.IS_PAID + " integer, " +
                " foreign key (" + SessionTable.Cols.CLIENT_ID + ") references " +
                ClientTable.NAME + "(" + ClientTable.Cols.ID + "))"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("Drop table if exists " + SessionTable.NAME);
        db.execSQL("Drop table if exists " + ClientTable.NAME);
        onCreate(db);
    }



}

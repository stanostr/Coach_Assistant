package com.stanislavveliky.coachingapp.database;

import android.database.Cursor;

import com.stanislavveliky.coachingapp.database.DbSchema.ClientTable;
import com.stanislavveliky.coachingapp.database.DbSchema.SessionTable;
import com.stanislavveliky.coachingapp.model.Client;
import com.stanislavveliky.coachingapp.model.Session;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class ApplicationCursorWrapper extends android.database.CursorWrapper {
    public ApplicationCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Client getClient()
    {
        UUID id = UUID.fromString(getString(getColumnIndex(ClientTable.Cols.ID)));
        String firstName = getString(getColumnIndex(ClientTable.Cols.FIRST_NAME));
        String lastName = getString(getColumnIndex(ClientTable.Cols.LAST_NAME));
        String location = getString(getColumnIndex(ClientTable.Cols.LOCATION));
        String phoneNumber = getString(getColumnIndex(ClientTable.Cols.PHONE_NUMBER));
        String email = getString(getColumnIndex(ClientTable.Cols.EMAIL));
        String timeZone = getString(getColumnIndex(ClientTable.Cols.TIME_ZONE));
        String dateOfBirthString = getString(getColumnIndex(ClientTable.Cols.DOB));
        long dateOfBirth = getLong(getColumnIndex(ClientTable.Cols.DATE_OF_BIRTH));


        boolean gender = (getInt(getColumnIndex(ClientTable.
                Cols.GENDER))==1) ? true: false ;
        String exercise = getString(getColumnIndex(ClientTable.Cols.EXERCISE));
        String diet = getString(getColumnIndex(ClientTable.Cols.DIET));
        String substanceUse = getString(getColumnIndex(ClientTable.Cols.SUBSTANCE));

        String sleep = getString(getColumnIndex(ClientTable.Cols.SLEEP));
        String otherDetails = getString(getColumnIndex(ClientTable.Cols.OTHER));

        String expectations = getString(getColumnIndex(ClientTable.Cols.EXPECTATIONS));
        boolean isActive = (getInt(getColumnIndex(ClientTable.
                Cols.IS_ACTIVE))==1) ? true: false ;

        Client client = new Client();
        client.setId(id);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setLocation(location);
        client.setPhoneNumber(phoneNumber);
        client.setEmail(email);
        client.setTimeZoneString(timeZone);
        client.setDateOfBirthString(dateOfBirthString);
        if(dateOfBirth!=0)
            client.setDateOfBirth(new Date(dateOfBirth));
        client.setGender(gender);
        client.setDiet(diet);
        client.setExercise(exercise);
        client.setSubstanceUse(substanceUse);
        client.setSleep(sleep);
        client.setOtherDetails(otherDetails);
        client.setExpectations(expectations);
        client.setActive(isActive);
        return client;
    }

    public Session getSession()
    {
        UUID clientId = UUID.fromString(getString(getColumnIndex(SessionTable.Cols.CLIENT_ID)));
        long date = getLong(getColumnIndex(SessionTable.Cols.DATE));
        boolean isPaid = (getInt(getColumnIndex(SessionTable.
                Cols.IS_PAID))==1) ? true: false ;
        String sessionNotes = getString(getColumnIndex(SessionTable.Cols.NOTES));

        Session session = new Session(clientId);
        session.setSessionDate(new Date(date));
        session.setPaid(isPaid);
        session.setSessionNotes(sessionNotes);
        return session;
    }

}

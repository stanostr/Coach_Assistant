package com.stanislavveliky.coachingapp.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Stanislav Ostrovskii
 * Copyright (c) 2018 Stanislav Ostrovskii
 */

public class Session {
    private UUID mClientId;
    private Date mSessionDate;
    private String mSessionNotes;
    private boolean mPaid;

    public Session (UUID clientId)
    {
        mClientId = clientId;
        mSessionDate = new Date();
    }

    public Session (Client client)
    {
        mClientId = client.getId();
        mSessionDate = new Date();
    }

    public Date getSessionDate() {
        return mSessionDate;
    }

    public void setSessionDate(Date sessionDate) {
        mSessionDate = sessionDate;
    }

    public String getSessionNotes() {
        return mSessionNotes;
    }

    public void setSessionNotes(String sessionNotes) {
        mSessionNotes = sessionNotes;
    }
    public UUID getClientId() {
        return mClientId;
    }

    public void setClientId(UUID clientId) {
        mClientId = clientId;
    }

    public boolean isPaid() {
        return mPaid;
    }

    public void setPaid(boolean paid) {
        mPaid = paid;
    }
}

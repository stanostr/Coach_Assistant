package com.stanislavveliky.coachingapp.model;

import java.util.Date;
import java.util.UUID;
import android.util.Log;

/**
 * Created by Stanislav Ostrovskii on 6/23/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 * The Client class holds all information associated with a particular client.
 */

public class Client {
    public static String TAG = "Client";
    private UUID mId;
    private String mFirstName;
    private String mLastName;
    private String mLocation;
    private String mTimeZoneString;
    private String mDateOfBirthString;

    private String mPhoneNumber;
    private String mEmail;

    private Date mDateOfBirth; //age is a derived attribute from DOB
    private boolean mGender; //true for male, false for female

    private String mExercise;
    private String mDiet;
    private String mSleep;
    private String mSubstanceUse;
    private String mOtherDetails;
    private String mExpectations;

    private Date mLastActive;
    private boolean mIsActive;

    public Client()
    {
        mId = UUID.randomUUID();
        mLastActive = new Date();
        mIsActive = true;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id)
    {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getTimeZoneString() {
        return mTimeZoneString;
    }

    public void setTimeZoneString(String timeZoneString) {
        mTimeZoneString = timeZoneString;
    }

    public String getDateOfBirthString() {
        return mDateOfBirthString;
    }

    public void setDateOfBirthString(String dateOfBirthString) {
        mDateOfBirthString = dateOfBirthString;
    }

    public Date getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        mDateOfBirth = dateOfBirth;
    }

    public boolean isGender() {
        return mGender;
    }

    public void setGender(boolean isMale) {
        mGender = isMale;
    }

    public String getExercise() {
        return mExercise;
    }

    public void setExercise(String exercise) {
        mExercise = exercise;
    }

    public String getDiet() {
        return mDiet;
    }

    public void setDiet(String diet) {
        mDiet = diet;
    }

    public String getSleep() {
        return mSleep;
    }

    public void setSleep(String sleep) {
        mSleep = sleep;
    }

    public String getSubstanceUse() {
        return mSubstanceUse;
    }

    public void setSubstanceUse(String substanceUse) {
        mSubstanceUse = substanceUse;
    }

    public String getOtherDetails() {
        return mOtherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        mOtherDetails = otherDetails;
    }

    public String getExpectations() {
        return mExpectations;
    }

    public void setExpectations(String expectations) {
        mExpectations = expectations;
    }

    public Date getLastActive() {
        return mLastActive;
    }

    public void setLastActive(Date lastActive) {
        mLastActive = lastActive;
    }

    public boolean isActive()
    {
        return mIsActive;
    }

    public void setActive(boolean active) {
        mIsActive = active;
    }

    /**
     * @return returns first name if only first name is set,
     * last name if only last name is set, and both if both are set.
     */
    public String getDisplayName()
    {
        if(mFirstName==null && mLastName == null) {
            return "No Name";
        }
        else if(mLastName == null)
        {
            return mFirstName;
        }
        else if(mFirstName==null) {

            return mLastName;
        }
        else
        {
            return mFirstName + " " + mLastName;
        }
    }

    public boolean isEmpty()
    {
        if(mFirstName==null && mLastName==null &&
                mLocation==null && mTimeZoneString ==null &&
                mDiet==null && mSleep==null &&
                mSubstanceUse==null && mDateOfBirthString==null &&
                mOtherDetails==null && mExercise==null &&
                mExpectations==null) return true;
        else return false;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}

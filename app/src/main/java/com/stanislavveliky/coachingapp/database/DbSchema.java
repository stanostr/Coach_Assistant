package com.stanislavveliky.coachingapp.database;

/**
 * Created by Stanislav Ostrovskii on 7/12/2018.
 * Copyright (c) 2018 Stanislav Ostrovskii
 * This class defines the String constants needed to describe the database.
 */

public class DbSchema {
    public static final class ClientTable {
        public static final String NAME = "clients";
        public static final class Cols {
            public static final String ID = "id";
            public static final String FIRST_NAME = "first_name";
            public static final String LAST_NAME = "last_name";
            public static final String LOCATION = "location";

            public static final String PHONE_NUMBER = "phone_number";
            public static final String EMAIL = "email";

            public static final String TIME_ZONE = "time_zone";
            public static final String DOB = "dob";
            public static final String DATE_OF_BIRTH = "date_of_birth";

            public static final String GENDER = "gender";
            public static final String EXERCISE = "exercise";
            public static final String DIET = "diet";
            public static final String SUBSTANCE = "substance";
            public static final String SLEEP = "sleep";
            public static final String OTHER = "other";

            public static final String EXPECTATIONS = "expectations";
            public static final String IS_ACTIVE = "is_active";
        }
    }

    public static final class SessionTable {
        public static final String NAME = "sessions";
        public static final class Cols {
            public static final String CLIENT_ID = "client_id";
            public static final String DATE = "date";
            public static final String IS_PAID = "is_paid";
            public static final String NOTES = "notes";
        }
    }

    public static final class TimezoneTable {
        public static final String NAME = "timezones";

    }

}

package com.kaooak.android.homebookkeeping.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DbContract {

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://com.kaooak.android.homebookkeeping.provider");

    public static class AccountsTable {
        public static final String NAME = "accounts";

        public class Columns implements BaseColumns {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String CURRENCY = "currency";
            public static final String VALUE = "value";
            public static final String START_VALUE = "startValue";
        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NAME);
    }

    public static class TransactionsTable {
        public static final String NAME = "transactions";

        public class Columns implements BaseColumns {
            public static final String UUID = "uuid";
            public static final String TYPE = "type";
            public static final String DATE = "date";
            public static final String ACCOUNT_ONE_UUID = "accountOneUuid";
            public static final String ACCOUNT_TWO_UUID = "accountTwoUuid";
            public static final String VALUE = "value";
            public static final String CURRENCY = "currency";
            public static final String RATE = "rate";
            public static final String COMMENT = "comment";
        }

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NAME);
//        public static final Uri CONTENT_ACCOUNTS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, NAME + "/account");
    }
}

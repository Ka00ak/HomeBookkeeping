package com.kaooak.android.homebookkeeping.database;

public class DbScheme {

    public class AccountsTable {
        public static final String NAME = "accounts";
        public class Columns {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String CURRENCY = "currency";
            public static final String VALUE = "value";
            public static final String START_VALUE = "startValue";
        }
    }

    public class TransactionsTable {
        public static final String NAME = "transactions";
        public class Columns {
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
    }
}

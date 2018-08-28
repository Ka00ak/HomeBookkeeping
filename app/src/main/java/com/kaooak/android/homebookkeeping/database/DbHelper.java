package com.kaooak.android.homebookkeeping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kaooak.android.homebookkeeping.data.Transaction;

import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AccountsDb";
    private static final int VERSION = 1;

    public static final String QUERY_ACCOUNT_DATA =
            "SELECT " +
                    "accounts._id," +
                    "accounts.name," +
                    "accounts.currency," +
                    "accounts.startValue, " +
                    "SUM(case when transactions.value >= 0 then (transactions.value * transactions.currencyValue) else 0 end) AS plusValue, " +
                    "SUM(case when transactions.value < 0 then (transactions.value * transactions.currencyValue)  else 0 end) AS minusValue " +
//                    "ifnull(accounts.startValue + SUM(transactions.value), accounts.startValue) AS currentValue " +
            "FROM accounts " +
                    "LEFT JOIN transactions ON accounts._id = transactions.accountId " +
            "GROUP BY accounts._id";
    public static final String QUERY_ACCOUNT_DATA_ONE =
            "SELECT " +
                    "accounts._id," +
                    "accounts.name," +
                    "accounts.currency," +
                    "accounts.startValue, " +
                    "SUM(case when transactions.value >= 0 then (transactions.value * transactions.currencyValue) else 0 end) AS plusValue, " +
                    "SUM(case when transactions.value < 0 then (transactions.value * transactions.currencyValue)  else 0 end) AS minusValue " +
//                    "ifnull(accounts.startValue + SUM(transactions.value), accounts.startValue) AS currentValue " +
            "FROM accounts " +
                    "LEFT JOIN transactions ON accounts._id = transactions.accountId " +
                    "WHERE accounts._id = ? " +
            "GROUP BY accounts._id";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + DbContract.AccountsTable.NAME + " (" +
                        DbContract.AccountsTable.Columns._ID + " integer primary key autoincrement, " +
                        DbContract.AccountsTable.Columns.NAME + ", " +
                        DbContract.AccountsTable.Columns.CURRENCY + ", " +
                        DbContract.AccountsTable.Columns.START_VALUE + ")");

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.AccountsTable.Columns.NAME, "Счёт 1");
        contentValues.put(DbContract.AccountsTable.Columns.CURRENCY, 0);
        contentValues.put(DbContract.AccountsTable.Columns.START_VALUE, 203124);
        long oneA = sqLiteDatabase.insert(DbContract.AccountsTable.NAME, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(DbContract.AccountsTable.Columns.NAME, "Счёт 2");
        contentValues2.put(DbContract.AccountsTable.Columns.CURRENCY, 1);
        contentValues2.put(DbContract.AccountsTable.Columns.START_VALUE, 2613);
        long twoA = sqLiteDatabase.insert(DbContract.AccountsTable.NAME, null, contentValues2);

        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(DbContract.AccountsTable.Columns.NAME, "Счёт 3");
        contentValues3.put(DbContract.AccountsTable.Columns.CURRENCY, 2);
        contentValues3.put(DbContract.AccountsTable.Columns.START_VALUE, 1899);
        long threeA = sqLiteDatabase.insert(DbContract.AccountsTable.NAME, null, contentValues3);

        sqLiteDatabase.execSQL(
                "create table " + DbContract.TransactionsTable.NAME + " (" +
                        DbContract.AccountsTable.Columns._ID + " integer primary key autoincrement, " +
                        DbContract.TransactionsTable.Columns.DATE + ", " +
                        DbContract.TransactionsTable.Columns.ACCOUNT_ID + ", " +
                        DbContract.TransactionsTable.Columns.CURRENCY + ", " +
                        DbContract.TransactionsTable.Columns.CURRENCY_VALUE + ", " +
                        DbContract.TransactionsTable.Columns.VALUE + ", " +
                        DbContract.TransactionsTable.Columns.COMMENT + ")");

//        Transaction t = new Transaction(account.getUUID(), TransactionTypes.TYPE_PLUS, new Date().getTime(), 300.00, Currencies.CURRENCY_RUB, "Просто плюс 300.00 рублей", TransactionTypes.TYPE_PLUS);
        ContentValues cv = new ContentValues();
        cv.put(DbContract.TransactionsTable.Columns.DATE, new Date().getTime());
        cv.put(DbContract.TransactionsTable.Columns.ACCOUNT_ID, oneA);
        cv.put(DbContract.TransactionsTable.Columns.CURRENCY, 0);
        cv.put(DbContract.TransactionsTable.Columns.CURRENCY_VALUE, 100);
        cv.put(DbContract.TransactionsTable.Columns.VALUE, 5059);
        cv.put(DbContract.TransactionsTable.Columns.COMMENT, "коммент 1");
        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv);

        ContentValues cv2 = new ContentValues();
        cv2.put(DbContract.TransactionsTable.Columns.DATE, new Date().getTime());
        cv2.put(DbContract.TransactionsTable.Columns.ACCOUNT_ID, twoA);
        cv2.put(DbContract.TransactionsTable.Columns.CURRENCY, 1);
        cv2.put(DbContract.TransactionsTable.Columns.CURRENCY_VALUE, 222);
        cv2.put(DbContract.TransactionsTable.Columns.VALUE, -3033);
        cv2.put(DbContract.TransactionsTable.Columns.COMMENT, "коммент 2");
        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv2);

        ContentValues cv3 = new ContentValues();
        cv3.put(DbContract.TransactionsTable.Columns.DATE, new Date().getTime());
        cv3.put(DbContract.TransactionsTable.Columns.ACCOUNT_ID, threeA);
        cv3.put(DbContract.TransactionsTable.Columns.CURRENCY, 2);
        cv3.put(DbContract.TransactionsTable.Columns.CURRENCY_VALUE, 333);
        cv3.put(DbContract.TransactionsTable.Columns.VALUE, 4564);
        cv3.put(DbContract.TransactionsTable.Columns.COMMENT, "коммент 3");
        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv3);

//        Transaction t2 = new Transaction(account.getUUID(), new Date().getTime(), 5.00, Currencies.CURRENCY_EURO, "Просто минус 5.00 евро", TransactionTypes.TYPE_MINUS);
//        ContentValues cv2 = two(t2);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv2);
//
//        Transaction t3 = new Transaction(account2.getUUID(), new Date().getTime(), 10.54, Currencies.CURRENCY_EURO, "Просто минус 10.54 евро", TransactionTypes.TYPE_MINUS);
//        ContentValues cv3 = two(t3);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv3);
//
//        Transaction t4 = new Transaction(account2.getUUID(), new Date().getTime(), 13.99, Currencies.CURRENCY_EURO, "Просто плюс 13.99 евро", TransactionTypes.TYPE_PLUS);
//        ContentValues cv4 = two(t4);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv4);
//
//        Transaction t5 = new Transaction(account3.getUUID(), new Date().getTime(), 15.96, Currencies.CURRENCY_DOLLAR, "Просто плюс 15.96 долларов", TransactionTypes.TYPE_PLUS);
//        ContentValues cv5 = two(t5);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv5);
//
//        Transaction t6 = new Transaction(account3.getUUID(), new Date().getTime(), 1700.02, Currencies.CURRENCY_RUB, "Просто минус 1700.02 рублей", TransactionTypes.TYPE_MINUS);
//        ContentValues cv6 = two(t6);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv6);
//
//        Transaction t7 = new Transaction(account.getUUID(), new Date().getTime(), 7.48, Currencies.CURRENCY_DOLLAR, "Перевод на \"Счёт 3\" (7.48 долларов)", TransactionTypes.TYPE_MINUS);
//        ContentValues cv7 = two(t7);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv7);
//        Transaction t8 = new Transaction(account3.getUUID(), new Date().getTime(), 7.48, Currencies.CURRENCY_DOLLAR, "Перевод от \"Счёт 1\" (7.48 долларов)", TransactionTypes.TYPE_PLUS);
//        ContentValues cv8 = two(t8);
//        sqLiteDatabase.insert(DbContract.TransactionsTable.NAME, null, cv8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

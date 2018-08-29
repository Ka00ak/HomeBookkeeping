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
                    "SUM(case when (transactions.currency = accounts.currency AND transactions.value >= 0) " +
                        "then transactions.value else 0 end) AS plusMainValue, " +
                    "SUM(case when (transactions.currency = accounts.currency AND transactions.value < 0) " +
                            "then transactions.value else 0 end) AS minusMainValue, " +
                    "SUM(case when (transactions.currency <> accounts.currency AND transactions.value >= 0) " +
                            "then (transactions.value * transactions.currencyValue) else 0 end) AS plusValue, " +
                    "SUM(case when (transactions.currency <> accounts.currency AND transactions.value < 0) " +
                            "then (transactions.value * transactions.currencyValue)  else 0 end) AS minusValue " +
            "FROM accounts " +
                    "LEFT JOIN transactions ON accounts._id = transactions.accountId " +
            "GROUP BY accounts._id";
    public static final String QUERY_ACCOUNT_DATA_ONE =
            "SELECT " +
                    "accounts._id," +
                    "accounts.name," +
                    "accounts.currency," +
                    "accounts.startValue, " +
                    "SUM(case when (transactions.currency = accounts.currency AND transactions.value >= 0) " +
                        "then transactions.value else 0 end) AS plusMainValue, " +
                    "SUM(case when (transactions.currency = accounts.currency AND transactions.value < 0) " +
                        "then transactions.value else 0 end) AS minusMainValue, " +
                    "SUM(case when (transactions.currency <> accounts.currency AND transactions.value >= 0) " +
                        "then (transactions.value * transactions.currencyValue) else 0 end) AS plusValue, " +
                    "SUM(case when (transactions.currency <> accounts.currency AND transactions.value < 0) " +
                        "then (transactions.value * transactions.currencyValue)  else 0 end) AS minusValue " +
            "FROM accounts " +
                    "LEFT JOIN transactions ON accounts._id = transactions.accountId " +
                    "WHERE accounts._id = ? " +
            "GROUP BY accounts._id";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);

        db.setForeignKeyConstraintsEnabled(true);
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
                        DbContract.TransactionsTable.Columns.COMMENT + ", " +
                        "CONSTRAINT account_id_foreign_key FOREIGN KEY (accountId) REFERENCES accounts (_id) ON DELETE CASCADE ON UPDATE CASCADE )");

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

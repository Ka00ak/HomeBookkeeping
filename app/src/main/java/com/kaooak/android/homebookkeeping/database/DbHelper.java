package com.kaooak.android.homebookkeeping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kaooak.android.homebookkeeping.TransactionTypes;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.Transaction;

import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "AccountsDb";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table " + DbScheme.AccountsTable.NAME + " " +
                        "(_id integer primary key autoincrement, " +
                        DbScheme.AccountsTable.Columns.UUID + ", " +
                        DbScheme.AccountsTable.Columns.NAME + ", " +
                        DbScheme.AccountsTable.Columns.CURRENCY + ", " +
                        DbScheme.AccountsTable.Columns.VALUE + ", " +
                        DbScheme.AccountsTable.Columns.START_VALUE + ")");

        Account account = new Account("Счёт 1", Currencies.CURRENCY_RUB, 2031.24,2031.24);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbScheme.AccountsTable.Columns.UUID, account.getUUID());
        contentValues.put(DbScheme.AccountsTable.Columns.NAME, account.getName());
        contentValues.put(DbScheme.AccountsTable.Columns.CURRENCY, account.getCurrency());
        contentValues.put(DbScheme.AccountsTable.Columns.VALUE, account.getValue());
        contentValues.put(DbScheme.AccountsTable.Columns.START_VALUE, account.getValue());
        sqLiteDatabase.insert(DbScheme.AccountsTable.NAME, null, contentValues);

        Account account2 = new Account("Счёт 2", Currencies.CURRENCY_DOLLAR,26.13, 26.13);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(DbScheme.AccountsTable.Columns.UUID, account2.getUUID());
        contentValues2.put(DbScheme.AccountsTable.Columns.NAME, account2.getName());
        contentValues2.put(DbScheme.AccountsTable.Columns.CURRENCY, account2.getCurrency());
        contentValues2.put(DbScheme.AccountsTable.Columns.VALUE, account2.getValue());
        contentValues2.put(DbScheme.AccountsTable.Columns.START_VALUE, account2.getValue());
        sqLiteDatabase.insert(DbScheme.AccountsTable.NAME, null, contentValues2);

        Account account3 = new Account("Счёт 3", Currencies.CURRENCY_EURO, 18.99,18.99);
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(DbScheme.AccountsTable.Columns.UUID, account3.getUUID());
        contentValues3.put(DbScheme.AccountsTable.Columns.NAME, account3.getName());
        contentValues3.put(DbScheme.AccountsTable.Columns.CURRENCY, account3.getCurrency());
        contentValues3.put(DbScheme.AccountsTable.Columns.VALUE, account3.getValue());
        contentValues3.put(DbScheme.AccountsTable.Columns.START_VALUE, account3.getValue());
        sqLiteDatabase.insert(DbScheme.AccountsTable.NAME, null, contentValues3);

        sqLiteDatabase.execSQL(
                "create table " + DbScheme.TransactionsTable.NAME + " " +
                        "(_id integer primary key autoincrement, " +
                        DbScheme.TransactionsTable.Columns.UUID + ", " +
                        DbScheme.TransactionsTable.Columns.DATE + ", " +
                        DbScheme.TransactionsTable.Columns.TYPE + ", " +
                        DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID + ", " +
                        DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID+ ", " +
                        DbScheme.TransactionsTable.Columns.CURRENCY + ", " +
                        DbScheme.TransactionsTable.Columns.VALUE + ", " +
                        DbScheme.TransactionsTable.Columns.RATE + ", " +
                        DbScheme.TransactionsTable.Columns.COMMENT + ")");

//        Transaction t = new Transaction(account.getUUID(), TransactionTypes.TYPE_PLUS, new Date().getTime(), 300.00, Currencies.CURRENCY_RUB, "Просто плюс 300.00 рублей", TransactionTypes.TYPE_PLUS);
//        ContentValues cv = two(t);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv);
//
//        Transaction t2 = new Transaction(account.getUUID(), new Date().getTime(), 5.00, Currencies.CURRENCY_EURO, "Просто минус 5.00 евро", TransactionTypes.TYPE_MINUS);
//        ContentValues cv2 = two(t2);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv2);
//
//        Transaction t3 = new Transaction(account2.getUUID(), new Date().getTime(), 10.54, Currencies.CURRENCY_EURO, "Просто минус 10.54 евро", TransactionTypes.TYPE_MINUS);
//        ContentValues cv3 = two(t3);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv3);
//
//        Transaction t4 = new Transaction(account2.getUUID(), new Date().getTime(), 13.99, Currencies.CURRENCY_EURO, "Просто плюс 13.99 евро", TransactionTypes.TYPE_PLUS);
//        ContentValues cv4 = two(t4);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv4);
//
//        Transaction t5 = new Transaction(account3.getUUID(), new Date().getTime(), 15.96, Currencies.CURRENCY_DOLLAR, "Просто плюс 15.96 долларов", TransactionTypes.TYPE_PLUS);
//        ContentValues cv5 = two(t5);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv5);
//
//        Transaction t6 = new Transaction(account3.getUUID(), new Date().getTime(), 1700.02, Currencies.CURRENCY_RUB, "Просто минус 1700.02 рублей", TransactionTypes.TYPE_MINUS);
//        ContentValues cv6 = two(t6);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv6);
//
//        Transaction t7 = new Transaction(account.getUUID(), new Date().getTime(), 7.48, Currencies.CURRENCY_DOLLAR, "Перевод на \"Счёт 3\" (7.48 долларов)", TransactionTypes.TYPE_MINUS);
//        ContentValues cv7 = two(t7);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv7);
//        Transaction t8 = new Transaction(account3.getUUID(), new Date().getTime(), 7.48, Currencies.CURRENCY_DOLLAR, "Перевод от \"Счёт 1\" (7.48 долларов)", TransactionTypes.TYPE_PLUS);
//        ContentValues cv8 = two(t8);
//        sqLiteDatabase.insert(DbScheme.TransactionsTable.NAME, null, cv8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private ContentValues two(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbScheme.TransactionsTable.Columns.UUID, transaction.getUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID, transaction.getAccountOneUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID, transaction.getAccountTwoUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.DATE, transaction.getDate());
        contentValues.put(DbScheme.TransactionsTable.Columns.TYPE, transaction.getType());
        contentValues.put(DbScheme.TransactionsTable.Columns.CURRENCY, transaction.getCurrency());
        contentValues.put(DbScheme.TransactionsTable.Columns.VALUE, transaction.getValue());
        contentValues.put(DbScheme.TransactionsTable.Columns.COMMENT, transaction.getComment());
        return contentValues;
    }
}

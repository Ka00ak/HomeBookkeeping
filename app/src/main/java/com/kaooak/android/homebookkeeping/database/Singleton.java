package com.kaooak.android.homebookkeeping.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kaooak.android.homebookkeeping.TransactionTypes;
import com.kaooak.android.homebookkeeping.data.Account;
import com.kaooak.android.homebookkeeping.data.Transaction;

import java.util.ArrayList;

public class Singleton {
    private static Singleton mInstance;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static Singleton getInstance(Context context) {
        if (mInstance == null)
            mInstance = new Singleton(context);

        return mInstance;
    }

    private Singleton(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = (new DbHelper(mContext)).getWritableDatabase();
    }

    //
    public Account selectAccount(String uuid) {
        String selection = DbContract.AccountsTable.Columns.UUID + " = ?";
        String[] selectArgs = new String[]{uuid};
        Cursor cursor = mDatabase.query(DbContract.AccountsTable.NAME, null, selection, selectArgs,null,null,null);

        Account account = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex(DbContract.AccountsTable.Columns.NAME));
            int currency = cursor.getInt(cursor.getColumnIndex(DbContract.AccountsTable.Columns.CURRENCY));
            double value = cursor.getDouble(cursor.getColumnIndex(DbContract.AccountsTable.Columns.VALUE));
            double startValue = cursor.getDouble(cursor.getColumnIndex(DbContract.AccountsTable.Columns.START_VALUE));

            account = new Account(uuid, name, currency, value, startValue);
        }
        cursor.close();

        return account;
    }
    public ArrayList<Account> selectAccounts() {
        Cursor cursor = mDatabase.query(DbContract.AccountsTable.NAME, null, null,null,null,null,null);

        ArrayList<Account> accounts = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String uuid = cursor.getString(cursor.getColumnIndex(DbContract.AccountsTable.Columns.UUID));
            String name = cursor.getString(cursor.getColumnIndex(DbContract.AccountsTable.Columns.NAME));
            int currency = cursor.getInt(cursor.getColumnIndex(DbContract.AccountsTable.Columns.CURRENCY));
            double value = cursor.getDouble(cursor.getColumnIndex(DbContract.AccountsTable.Columns.VALUE));
            double startValue = cursor.getDouble(cursor.getColumnIndex(DbContract.AccountsTable.Columns.START_VALUE));

            Account account = new Account(uuid, name, currency, value, startValue);
            accounts.add(account);

            cursor.moveToNext();
        }
        cursor.close();

        return accounts;
    }

    public void insertAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.AccountsTable.Columns.UUID, account.getUUID());
        contentValues.put(DbContract.AccountsTable.Columns.NAME, account.getName());
        contentValues.put(DbContract.AccountsTable.Columns.CURRENCY, account.getCurrency());
        contentValues.put(DbContract.AccountsTable.Columns.VALUE, account.getValue());
        contentValues.put(DbContract.AccountsTable.Columns.START_VALUE, account.getStartValue());

        mDatabase.insert(DbContract.AccountsTable.NAME, null, contentValues);
    }
    public void updateAccount(Account account) {

        double value = account.getStartValue();
        ArrayList<Transaction> transactions = selectTransactions(account);
        for(int i=0; i < transactions.size(); i++) {
            double rate = transactions.get(i).getRate();

            if (transactions.get(i).getType() == TransactionTypes.TYPE_PLUS)
                value += (transactions.get(i).getValue() * rate);
            if (transactions.get(i).getType() == TransactionTypes.TYPE_MINUS)
                value -= (transactions.get(i).getValue() * rate);
        }
        account.setValue(value);


        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.AccountsTable.Columns.UUID, account.getUUID());
        contentValues.put(DbContract.AccountsTable.Columns.NAME, account.getName());
        contentValues.put(DbContract.AccountsTable.Columns.CURRENCY, account.getCurrency());
        contentValues.put(DbContract.AccountsTable.Columns.VALUE, account.getValue());
        contentValues.put(DbContract.AccountsTable.Columns.START_VALUE, account.getStartValue());

        String where = DbContract.AccountsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{account.getUUID()};

        mDatabase.update(DbContract.AccountsTable.NAME, contentValues, where, whereArgs);
    }
    public void deleteAccount(Account account) {
        String where = DbContract.AccountsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{account.getUUID()};

        mDatabase.delete(DbContract.AccountsTable.NAME, where, whereArgs);
    }

    //
    public Transaction selectTransaction(String uuid) {
        String selection = DbContract.TransactionsTable.Columns.UUID + " = ?";
        String[] selectArgs = new String[]{uuid};
        Cursor cursor = mDatabase.query(DbContract.TransactionsTable.NAME, null, selection, selectArgs,null,null,null);

        Transaction transaction = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            int type = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.DATE));
            String account_one_uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.ACCOUNT_ONE_UUID));
            String account_two_uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.ACCOUNT_TWO_UUID));
            double value = cursor.getDouble(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.VALUE));
            int currency = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.CURRENCY));
            double rate = cursor.getDouble(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.RATE));
            String comment = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.COMMENT));

            transaction = new Transaction(uuid, type, date, account_one_uuid, account_two_uuid, value, currency, rate, comment);
        }
        cursor.close();

        return transaction;
    }
    public ArrayList<Transaction> selectTransactions() {
        String orderBy = DbContract.TransactionsTable.Columns.DATE;
        Cursor cursor = mDatabase.query(DbContract.TransactionsTable.NAME, null, null, null, null, null, orderBy);

        ArrayList<Transaction> transactions = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.UUID));
            int type= cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.DATE));
            String account_one_uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.ACCOUNT_ONE_UUID));
            String account_two_uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.ACCOUNT_TWO_UUID));
            double value = cursor.getDouble(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.VALUE));
            int currency = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.CURRENCY));
            double rate = cursor.getDouble(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.RATE));
            String comment = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.COMMENT));

            Transaction transaction = new Transaction(uuid, type, date, account_one_uuid, account_two_uuid, value, currency, rate, comment);
            transactions.add(transaction);

            cursor.moveToNext();
        }
        cursor.close();

        return transactions;
    }
    public ArrayList<Transaction> selectTransactions(Account account) {
        String selection = DbContract.TransactionsTable.Columns.ACCOUNT_ONE_UUID + " = ?";
        String[] selectionArgs = new String[]{account.getUUID()};
        String orderBy = DbContract.TransactionsTable.Columns.DATE;
        Cursor cursor = mDatabase.query(DbContract.TransactionsTable.NAME, null, selection, selectionArgs, null, null, orderBy);

        ArrayList<Transaction> transactions = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.UUID));
            int type= cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.DATE));
            String account_one_uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.ACCOUNT_ONE_UUID));
            String account_two_uuid = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.ACCOUNT_TWO_UUID));
            double value = cursor.getDouble(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.VALUE));
            int currency = cursor.getInt(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.CURRENCY));
            double rate = cursor.getDouble(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.RATE));
            String comment = cursor.getString(cursor.getColumnIndex(DbContract.TransactionsTable.Columns.COMMENT));

            Transaction transaction = new Transaction(uuid, type, date, account_one_uuid, account_two_uuid, value, currency, rate, comment);
            transactions.add(transaction);

            cursor.moveToNext();
        }
        cursor.close();

        return transactions;
    }

    public void insertTransaction(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.TransactionsTable.Columns.UUID, transaction.getUUID());
        contentValues.put(DbContract.TransactionsTable.Columns.TYPE, transaction.getType());
        contentValues.put(DbContract.TransactionsTable.Columns.DATE, transaction.getDate());
        contentValues.put(DbContract.TransactionsTable.Columns.ACCOUNT_ONE_UUID, transaction.getAccountOneUUID());
        contentValues.put(DbContract.TransactionsTable.Columns.ACCOUNT_TWO_UUID, transaction.getAccountTwoUUID());
        contentValues.put(DbContract.TransactionsTable.Columns.VALUE, transaction.getValue());
        contentValues.put(DbContract.TransactionsTable.Columns.CURRENCY, transaction.getCurrency());
        contentValues.put(DbContract.TransactionsTable.Columns.RATE, transaction.getRate());
        contentValues.put(DbContract.TransactionsTable.Columns.COMMENT, transaction.getComment());

        mDatabase.insert(DbContract.TransactionsTable.NAME, null, contentValues);
    }
    public void updateTransaction(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.TransactionsTable.Columns.UUID, transaction.getUUID());
        contentValues.put(DbContract.TransactionsTable.Columns.TYPE, transaction.getType());
        contentValues.put(DbContract.TransactionsTable.Columns.DATE, transaction.getDate());
        contentValues.put(DbContract.TransactionsTable.Columns.ACCOUNT_ONE_UUID, transaction.getAccountOneUUID());
        contentValues.put(DbContract.TransactionsTable.Columns.ACCOUNT_TWO_UUID, transaction.getAccountTwoUUID());
        contentValues.put(DbContract.TransactionsTable.Columns.VALUE, transaction.getValue());
        contentValues.put(DbContract.TransactionsTable.Columns.CURRENCY, transaction.getCurrency());
        contentValues.put(DbContract.TransactionsTable.Columns.RATE, transaction.getRate());
        contentValues.put(DbContract.TransactionsTable.Columns.COMMENT, transaction.getComment());

        String where = DbContract.TransactionsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{transaction.getUUID()};

        mDatabase.update(DbContract.TransactionsTable.NAME, contentValues, where, whereArgs);
    }
    public void deleteTransaction(Transaction transaction) {
        String where = DbContract.TransactionsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{transaction.getUUID()};

        mDatabase.delete(DbContract.TransactionsTable.NAME, where, whereArgs);
    }

}

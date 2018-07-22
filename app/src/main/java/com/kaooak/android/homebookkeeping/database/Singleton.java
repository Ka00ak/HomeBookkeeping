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
        String selection = DbScheme.AccountsTable.Columns.UUID + " = ?";
        String[] selectArgs = new String[]{uuid};
        Cursor cursor = mDatabase.query(DbScheme.AccountsTable.NAME, null, selection, selectArgs,null,null,null);

        Account account = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.NAME));
            int currency = cursor.getInt(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.CURRENCY));
            double value = cursor.getDouble(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.VALUE));
            double startValue = cursor.getDouble(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.START_VALUE));

            account = new Account(uuid, name, currency, value, startValue);
        }
        cursor.close();

        return account;
    }
    public ArrayList<Account> selectAccounts() {
        Cursor cursor = mDatabase.query(DbScheme.AccountsTable.NAME, null, null,null,null,null,null);

        ArrayList<Account> accounts = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String uuid = cursor.getString(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.UUID));
            String name = cursor.getString(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.NAME));
            int currency = cursor.getInt(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.CURRENCY));
            double value = cursor.getDouble(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.VALUE));
            double startValue = cursor.getDouble(cursor.getColumnIndex(DbScheme.AccountsTable.Columns.START_VALUE));

            Account account = new Account(uuid, name, currency, value, startValue);
            accounts.add(account);

            cursor.moveToNext();
        }
        cursor.close();

        return accounts;
    }

    public void insertAccount(Account account) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbScheme.AccountsTable.Columns.UUID, account.getUUID());
        contentValues.put(DbScheme.AccountsTable.Columns.NAME, account.getName());
        contentValues.put(DbScheme.AccountsTable.Columns.CURRENCY, account.getCurrency());
        contentValues.put(DbScheme.AccountsTable.Columns.VALUE, account.getValue());
        contentValues.put(DbScheme.AccountsTable.Columns.START_VALUE, account.getStartValue());

        mDatabase.insert(DbScheme.AccountsTable.NAME, null, contentValues);
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
        contentValues.put(DbScheme.AccountsTable.Columns.UUID, account.getUUID());
        contentValues.put(DbScheme.AccountsTable.Columns.NAME, account.getName());
        contentValues.put(DbScheme.AccountsTable.Columns.CURRENCY, account.getCurrency());
        contentValues.put(DbScheme.AccountsTable.Columns.VALUE, account.getValue());
        contentValues.put(DbScheme.AccountsTable.Columns.START_VALUE, account.getStartValue());

        String where = DbScheme.AccountsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{account.getUUID()};

        mDatabase.update(DbScheme.AccountsTable.NAME, contentValues, where, whereArgs);
    }
    public void deleteAccount(Account account) {
        String where = DbScheme.AccountsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{account.getUUID()};

        mDatabase.delete(DbScheme.AccountsTable.NAME, where, whereArgs);
    }

    //
    public Transaction selectTransaction(String uuid) {
        String selection = DbScheme.TransactionsTable.Columns.UUID + " = ?";
        String[] selectArgs = new String[]{uuid};
        Cursor cursor = mDatabase.query(DbScheme.TransactionsTable.NAME, null, selection, selectArgs,null,null,null);

        Transaction transaction = null;
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();

            int type = cursor.getInt(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.DATE));
            String account_one_uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID));
            String account_two_uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID));
            double value = cursor.getDouble(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.VALUE));
            int currency = cursor.getInt(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.CURRENCY));
            double rate = cursor.getDouble(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.RATE));
            String comment = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.COMMENT));

            transaction = new Transaction(uuid, type, date, account_one_uuid, account_two_uuid, value, currency, rate, comment);
        }
        cursor.close();

        return transaction;
    }
    public ArrayList<Transaction> selectTransactions() {
        String orderBy = DbScheme.TransactionsTable.Columns.DATE;
        Cursor cursor = mDatabase.query(DbScheme.TransactionsTable.NAME, null, null, null, null, null, orderBy);

        ArrayList<Transaction> transactions = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.UUID));
            int type= cursor.getInt(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.DATE));
            String account_one_uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID));
            String account_two_uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID));
            double value = cursor.getDouble(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.VALUE));
            int currency = cursor.getInt(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.CURRENCY));
            double rate = cursor.getDouble(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.RATE));
            String comment = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.COMMENT));

            Transaction transaction = new Transaction(uuid, type, date, account_one_uuid, account_two_uuid, value, currency, rate, comment);
            transactions.add(transaction);

            cursor.moveToNext();
        }
        cursor.close();

        return transactions;
    }
    public ArrayList<Transaction> selectTransactions(Account account) {
        String selection = DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID + " = ?";
        String[] selectionArgs = new String[]{account.getUUID()};
        String orderBy = DbScheme.TransactionsTable.Columns.DATE;
        Cursor cursor = mDatabase.query(DbScheme.TransactionsTable.NAME, null, selection, selectionArgs, null, null, orderBy);

        ArrayList<Transaction> transactions = new ArrayList<>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.UUID));
            int type= cursor.getInt(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.TYPE));
            long date = cursor.getLong(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.DATE));
            String account_one_uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID));
            String account_two_uuid = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID));
            double value = cursor.getDouble(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.VALUE));
            int currency = cursor.getInt(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.CURRENCY));
            double rate = cursor.getDouble(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.RATE));
            String comment = cursor.getString(cursor.getColumnIndex(DbScheme.TransactionsTable.Columns.COMMENT));

            Transaction transaction = new Transaction(uuid, type, date, account_one_uuid, account_two_uuid, value, currency, rate, comment);
            transactions.add(transaction);

            cursor.moveToNext();
        }
        cursor.close();

        return transactions;
    }

    public void insertTransaction(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbScheme.TransactionsTable.Columns.UUID, transaction.getUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.TYPE, transaction.getType());
        contentValues.put(DbScheme.TransactionsTable.Columns.DATE, transaction.getDate());
        contentValues.put(DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID, transaction.getAccountOneUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID, transaction.getAccountTwoUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.VALUE, transaction.getValue());
        contentValues.put(DbScheme.TransactionsTable.Columns.CURRENCY, transaction.getCurrency());
        contentValues.put(DbScheme.TransactionsTable.Columns.RATE, transaction.getRate());
        contentValues.put(DbScheme.TransactionsTable.Columns.COMMENT, transaction.getComment());

        mDatabase.insert(DbScheme.TransactionsTable.NAME, null, contentValues);
    }
    public void updateTransaction(Transaction transaction) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbScheme.TransactionsTable.Columns.UUID, transaction.getUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.TYPE, transaction.getType());
        contentValues.put(DbScheme.TransactionsTable.Columns.DATE, transaction.getDate());
        contentValues.put(DbScheme.TransactionsTable.Columns.ACCOUNT_ONE_UUID, transaction.getAccountOneUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.ACCOUNT_TWO_UUID, transaction.getAccountTwoUUID());
        contentValues.put(DbScheme.TransactionsTable.Columns.VALUE, transaction.getValue());
        contentValues.put(DbScheme.TransactionsTable.Columns.CURRENCY, transaction.getCurrency());
        contentValues.put(DbScheme.TransactionsTable.Columns.RATE, transaction.getRate());
        contentValues.put(DbScheme.TransactionsTable.Columns.COMMENT, transaction.getComment());

        String where = DbScheme.TransactionsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{transaction.getUUID()};

        mDatabase.update(DbScheme.TransactionsTable.NAME, contentValues, where, whereArgs);
    }
    public void deleteTransaction(Transaction transaction) {
        String where = DbScheme.TransactionsTable.Columns.UUID + " = ?";
        String[] whereArgs = new String[]{transaction.getUUID()};

        mDatabase.delete(DbScheme.TransactionsTable.NAME, where, whereArgs);
    }

}

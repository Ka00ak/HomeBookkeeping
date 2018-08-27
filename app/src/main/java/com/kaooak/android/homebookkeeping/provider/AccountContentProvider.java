package com.kaooak.android.homebookkeeping.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kaooak.android.homebookkeeping.database.DbContract;
import com.kaooak.android.homebookkeeping.database.DbHelper;

import java.util.ArrayList;

public class AccountContentProvider extends ContentProvider {

    public static final String TAG = "ActivityProvider";

    private static final int ACCOUNTS = 100;
    private static final int ACCOUNT_ID = 101;

    private static final int TRANSACTIONS = 200;
//    private static final int TRANSACTIONS_ACCOUNT_ID = 201;
    private static final int TRANSACTION_ID = 202;
//    private static final int TRANSACTIONS_ACCOUNT_ID = 202;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.AccountsTable.NAME, ACCOUNTS);
        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.AccountsTable.NAME + "/#", ACCOUNT_ID);

        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.TransactionsTable.NAME, TRANSACTIONS);
//        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.TransactionsTable.NAME + "/account/#", TRANSACTIONS_ACCOUNT_ID);
        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.TransactionsTable.NAME + "/#", TRANSACTION_ID);
    }

    private DbHelper mDbHelper;

    //
    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Log.d(TAG, "query : " + match + " " + uri.toString());
        switch (match) {
            case ACCOUNTS:
                Cursor cursor = null;
                try {
                    cursor = database.rawQuery(DbHelper.QUERY_ACCOUNT_DATA, strings1);
                } catch (Exception e) {
                    Log.d(TAG, "query: " + e.toString());
                }
//                Cursor cursor = database.query(DbContract.AccountsTable.NAME, strings, s, strings1,null, null,  s1);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case ACCOUNT_ID:
//                s = DbContract.AccountsTable.Columns._ID + " = ?";
                strings1 = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                Cursor cursor2 = database.rawQuery(DbHelper.QUERY_ACCOUNT_DATA_ONE, strings1);
//                Cursor cursor2 = database.query(DbContract.AccountsTable.NAME, strings, s, strings1, null, null, s1);
                cursor2.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor2;
            case TRANSACTIONS:
                Cursor cursor5 = database.query(DbContract.TransactionsTable.NAME, strings, s, strings1,null, null,  s1);
                cursor5.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor5;
//            case TRANSACTIONS_ACCOUNT_ID:
//                long id = (ContentUris.parseId(uri));
//                s = DbContract.TransactionsTable.Columns.ACCOUNT_ID + " = " + id;
//                Cursor cursor3 = database.query(DbContract.TransactionsTable.NAME, strings, s, strings1,null, null,  s1);
//                cursor3.setNotificationUri(getContext().getContentResolver(), uri);
//                return cursor3;
            case TRANSACTION_ID:
                long id2 = (ContentUris.parseId(uri));
                s = DbContract.TransactionsTable.Columns._ID + " = " + id2;
//                strings1 = new String[]{ "500" };
                Cursor cursor4 = database.query(DbContract.TransactionsTable.NAME, strings, s, strings1, null, null, s1);
                cursor4.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor4;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCOUNTS:
                return "Accounts";
            case ACCOUNT_ID:
                return "AccountID";
//            case TRANSACTIONS_ACCOUNT_ID:
//                return "TransactionsAccountId";
            case TRANSACTION_ID:
                return "TransactionId";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCOUNTS:
                long id = database.insert(DbContract.AccountsTable.NAME, null, contentValues);
                Uri insertedUri = ContentUris.withAppendedId(DbContract.AccountsTable.CONTENT_URI, id);
                getContext().getContentResolver().notifyChange(uri, null);
                return insertedUri;
            case TRANSACTIONS:
                long id2 = database.insert(DbContract.TransactionsTable.NAME, null, contentValues);
                Uri insertedUri2 = ContentUris.withAppendedId(DbContract.TransactionsTable.CONTENT_URI, id2);
                getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver().notifyChange(DbContract.AccountsTable.CONTENT_URI, null);
                return insertedUri2;
            default:
                return null;
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ACCOUNTS:
                int count = database.delete(DbContract.AccountsTable.NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case ACCOUNT_ID:
                s = DbContract.AccountsTable.Columns._ID + " = ?";
                strings = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                int count2 = database.delete(DbContract.AccountsTable.NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count2;
            case TRANSACTIONS:
                int count3 = database.delete(DbContract.TransactionsTable.NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count3;
            case TRANSACTION_ID:
                s = DbContract.TransactionsTable.Columns._ID + " = ?";
                strings = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                int count4 = database.delete(DbContract.TransactionsTable.NAME, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count4;
            default:
                return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Log.d(TAG, "update : " + match + " " + uri.toString());
        switch (match) {
            case ACCOUNTS:
                int count = database.update(DbContract.AccountsTable.NAME, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count;
            case ACCOUNT_ID:
                s = DbContract.AccountsTable.Columns._ID + " = ?";
                strings = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                int count2 = database.update(DbContract.AccountsTable.NAME, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                return count2;
            case TRANSACTIONS:
                int count3 = database.update(DbContract.TransactionsTable.NAME, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver().notifyChange(DbContract.AccountsTable.CONTENT_URI, null);
                return count3;
            case TRANSACTION_ID:
                s = DbContract.TransactionsTable.Columns._ID + " = ?";
                strings = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                int count4 = database.update(DbContract.TransactionsTable.NAME, contentValues, s, strings);
                getContext().getContentResolver().notifyChange(uri, null);
                getContext().getContentResolver().notifyChange(DbContract.AccountsTable.CONTENT_URI, null);
                return count4;
            default:
                return 0;
        }
    }
}

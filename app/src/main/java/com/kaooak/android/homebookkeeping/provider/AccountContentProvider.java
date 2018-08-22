package com.kaooak.android.homebookkeeping.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kaooak.android.homebookkeeping.database.DbContract;
import com.kaooak.android.homebookkeeping.database.DbHelper;

public class AccountContentProvider extends ContentProvider {

    private static final int ACCOUNTS = 100;
    private static final int ACCOUNT_ID = 101;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.AccountsTable.NAME, ACCOUNTS);
        sUriMatcher.addURI("com.kaooak.android.homebookkeeping.provider", DbContract.AccountsTable.NAME + "/#", ACCOUNT_ID);
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
        switch (match) {
            case ACCOUNTS:
                Cursor cursor = database.query(DbContract.AccountsTable.NAME, strings, s, strings1,null, null,  s1);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            case ACCOUNT_ID:
                s = DbContract.AccountsTable.Columns._ID + " = ?";
                strings1 = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                Cursor cursor2 = database.query(DbContract.AccountsTable.NAME, strings, s, strings1, null, null, s1);
                cursor2.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor2;
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
            default:
                return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
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
            default:
                return 0;
        }    }
}

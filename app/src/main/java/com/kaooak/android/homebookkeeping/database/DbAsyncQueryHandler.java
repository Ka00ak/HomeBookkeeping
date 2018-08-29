package com.kaooak.android.homebookkeeping.database;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

public class DbAsyncQueryHandler extends AsyncQueryHandler {

    public DbAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }
}

package com.kaooak.android.homebookkeeping.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.database.DbAsyncQueryHandler;
import com.kaooak.android.homebookkeeping.database.DbContract;
import com.kaooak.android.homebookkeeping.fragments.AccountFragment;
import com.kaooak.android.homebookkeeping.fragments.AccountTransactionFragment;

public class AccountActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = AccountActivity.class.getSimpleName();

    private static final String EXTRA_UUID = "com.kaooak.android.homebookkeeping.activities.accountactivity.extra.uuid";

    private EditText mEtAccountName;
    private Spinner mSpinnerAccountCurrenncy;
    private EditText mEtAccountStartValue;
    private Button mBtnSave;

    private Uri mUri;

    public static Intent getIntent(Context context) {
        Log.d(TAG, "getIntent: ");
        Intent intent = new Intent(context, AccountActivity.class);
        return intent;
    }

    public static Intent getIntent(Context context, String uuid) {
        Log.d(TAG, "getIntent: ");
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_account);

        Log.d(TAG, "onCreate: ");

        Toolbar toolbar = findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEtAccountName = findViewById(R.id.et_account_name);
        mSpinnerAccountCurrenncy = findViewById(R.id.spinner_account_currency);
        mEtAccountStartValue = findViewById(R.id.et_account_start_value);
        mBtnSave = findViewById(R.id.btn_account_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(DbContract.AccountsTable.Columns.NAME, mEtAccountName.getText().toString());
                contentValues.put(DbContract.AccountsTable.Columns.CURRENCY, mSpinnerAccountCurrenncy.getSelectedItemId());
                contentValues.put(DbContract.AccountsTable.Columns.START_VALUE, mEtAccountStartValue.getText().toString());

                if (mUri == null) {
                    DbAsyncQueryHandler handler = new DbAsyncQueryHandler(getContentResolver());
                    handler.startInsert(0, null, DbContract.AccountsTable.CONTENT_URI, contentValues);
                } else {
                    DbAsyncQueryHandler handler = new DbAsyncQueryHandler(getContentResolver());
                    handler.startUpdate(0, null, mUri, contentValues, null, null);
                }

                finish();
            }
        });

        //
        Log.d(TAG, "onCreate: ");


        Intent intent = getIntent();
        mUri = intent.getData();

        if (mUri == null) {
            mBtnSave.setText("Создать");
        } else {
            mBtnSave.setText("Сохранить");
            getSupportLoaderManager().initLoader(1, null, this);
        }


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//        Intent intent = getIntent();
//        String uuid = intent.getStringExtra(EXTRA_UUID);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment_account = fragmentManager.findFragmentById(R.id.container_fragment_accounts);
//        if (fragment_account == null) {
//            fragment_account = AccountFragment.newInstance(uuid);
//            fragmentManager.beginTransaction()
//                    .add(R.id.container_fragment_accounts, fragment_account)
//                    .commit();
//        }
//
//        Fragment fragment_transactions = fragmentManager.findFragmentById(R.id.container_fragment_transactions);
//        if (fragment_transactions == null) {
//            fragment_transactions = AccountTransactionFragment.newInstance(uuid);
//            fragmentManager.beginTransaction()
//                    .add(R.id.container_fragment_transactions, fragment_transactions)
//                    .commit();
//        }
//
//        TabHost tabHost = findViewById(R.id.tabHost);
//        tabHost.setup();
//
//        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag 1");
//        tabSpec.setContent(R.id.container_fragment_accounts);
//        tabSpec.setIndicator("Данные");
//        tabHost.addTab(tabSpec);
//
//        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tag 2");
//        tabSpec2.setContent(R.id.container_fragment_transactions);
//        tabSpec2.setIndicator("Транзакции");
//        tabHost.addTab(tabSpec2);
//
//        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");

        if (mUri == null) {
            return true;
        } else {
            getMenuInflater().inflate(R.menu.menu_account, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        
        switch (item.getItemId()) {
            case R.id.menu_account_delete:
                DbAsyncQueryHandler handler = new DbAsyncQueryHandler(getContentResolver());
                handler.startDelete(0, null, mUri, null,null);
                finish();
                return true;
            default:
                return false;
        }
    }

    //
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(this, mUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");

        if (data.getCount() > 0) {
            data.moveToFirst();

            String name = data.getString(data.getColumnIndex(DbContract.AccountsTable.Columns.NAME));
            int currency = data.getInt(data.getColumnIndex(DbContract.AccountsTable.Columns.CURRENCY));
            String startValue = data.getString(data.getColumnIndex(DbContract.AccountsTable.Columns.START_VALUE));

            mEtAccountName.setText(name);
            mSpinnerAccountCurrenncy.setSelection(currency);
            mEtAccountStartValue.setText(startValue);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }
}

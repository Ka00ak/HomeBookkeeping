package com.kaooak.android.homebookkeeping.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kaooak.android.homebookkeeping.R;
import com.kaooak.android.homebookkeeping.DecimalFilter;
import com.kaooak.android.homebookkeeping.data.Currencies;
import com.kaooak.android.homebookkeeping.data.JSON.GsonData;
import com.kaooak.android.homebookkeeping.data.RetrofitCBR;
import com.kaooak.android.homebookkeeping.data.Transaction;
import com.kaooak.android.homebookkeeping.database.DbAsyncQueryHandler;
import com.kaooak.android.homebookkeeping.database.DbContract;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransactionActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, DateFragment.OnCallbacksListener{

    private static final String TAG = TransactionActivity.class.getSimpleName();

    private static final String EXTRA_ACCOUNT_ID = "com.kaooak.android.homebookkeeping.activities.transactionactivity.extra.accountid";
    private static final String EXTRA_ACCOUNT_CURRENT_VALUE = "com.kaooak.android.homebookkeeping.activities.transactionactivity.extra.accountcurrentvalue";
    private static final String EXTRA_ACCOUNT_CURRENCY = "com.kaooak.android.homebookkeeping.activities.transactionactivity.extra.accountcurrency";

    private static final String TAG_DIALOG_DATE = "dialogDate";

//    private static final int REQUEST_DIALOG_DATE = 0;

    private Button mBtnTransactionDate;
    private EditText mEtTransactionValue;
    private Spinner mSpinnerTransactionCurrency;
    private EditText mEtTransactionComment;
    private Button mBtnSave;

    private Uri mUri;

    private long mAccountId;

    private long mMillis;

    public static Intent getIntent(Context context, Uri uri, long accountId, int accountCurrentValue, int accountCurrency) {
        Log.d(TAG, "getIntent: ");
        Intent intent = new Intent(context, TransactionActivity.class);
        intent.setData(uri);
        intent.putExtra(EXTRA_ACCOUNT_ID, accountId);
        intent.putExtra(EXTRA_ACCOUNT_CURRENT_VALUE, accountCurrentValue);
        intent.putExtra(EXTRA_ACCOUNT_CURRENCY, accountCurrency);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Toolbar toolbar = findViewById(R.id.toolbar_transaction);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mBtnTransactionDate = findViewById(R.id.btn_transaction_date);
        mBtnTransactionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFragment dateFragment = DateFragment.newInstance(new Date().getTime());
                dateFragment.show(getSupportFragmentManager(), TAG_DIALOG_DATE);
            }
        });

        mEtTransactionValue = findViewById(R.id.et_transaction_value);
        mEtTransactionValue.setFilters(new InputFilter[]{ new DecimalFilter(9, 2) });

        mSpinnerTransactionCurrency = findViewById(R.id.spinner_transaction_currency);

        mEtTransactionComment = findViewById(R.id.et_transaction_comment);

        mBtnSave = findViewById(R.id.btn_transaction_save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long millis = mMillis;
                int value = (int)(Double.valueOf(mEtTransactionValue.getText().toString()) * 100);
                int currency = (int) mSpinnerTransactionCurrency.getSelectedItemId();
                String comment= mEtTransactionComment.getText().toString();

                Transaction transaction = new Transaction(millis, mAccountId, currency, value, comment);

                RetrofitAsyncTask retrofitAsyncTask = new RetrofitAsyncTask();
                retrofitAsyncTask.execute(transaction);
            }
        });

        Intent intent = getIntent();
        mUri = intent.getData();
        mAccountId = intent.getLongExtra(EXTRA_ACCOUNT_ID, 0);

        if (mUri == null) {
            mBtnSave.setText("Создать");
        } else {
            mBtnSave.setText("Сохранить");
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mUri == null) {
            return true;
        } else {
            getMenuInflater().inflate(R.menu.menu_transaction, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_transaction_delete:
                DbAsyncQueryHandler handler = new DbAsyncQueryHandler(getContentResolver());
                handler.startDelete(0, null, mUri, null,null);
                finish();
                return true;
            default:
                return false;
        }
    }

    //
    @Override
    public void onDatePicked(long millis) {
        mMillis = millis;
        String millisStr = DateFormat.format("dd.MM.yyyy", millis).toString();
        mBtnTransactionDate.setText(millisStr);
    }

    //
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(this, mUri, null, null, null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");
        
        if (data.getCount() > 0) {
            data.moveToFirst();

            long millis = data.getLong(data.getColumnIndex(DbContract.TransactionsTable.Columns.DATE));
            String dateStr = DateFormat.getDateFormat(this).format(new Date(millis));
            String value = String.valueOf(data.getInt(data.getColumnIndex(DbContract.TransactionsTable.Columns.VALUE)) / 100.0);
            int currency = data.getInt(data.getColumnIndex(DbContract.TransactionsTable.Columns.CURRENCY));
            String comment = data.getString(data.getColumnIndex(DbContract.TransactionsTable.Columns.COMMENT));

            mMillis = millis;
            mBtnTransactionDate.setText(dateStr);
            mEtTransactionValue.setText(value);
            mSpinnerTransactionCurrency.setSelection(currency);
            mEtTransactionComment.setText(comment);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");
    }

    //
    private class RetrofitAsyncTask extends AsyncTask<Transaction, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Transaction... transactions) {

            Date date = transactions[0].getDate();
            long accountId = transactions[0].getAccountId();
            int value = transactions[0].getValue();
            int currency = transactions[0].getCurrency();
            String comment = transactions[0].getComment();

            //
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbContract.TransactionsTable.Columns.DATE, date.getTime());

            String strDate = DateFormat.format("yyyy/MM/dd", date).toString();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.cbr-xml-daily.ru/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            RetrofitCBR retrofitCBR = retrofit.create(RetrofitCBR.class);

            Call<GsonData> cur = retrofitCBR.getData(strDate);

            GsonData gsonData;
            try {
                gsonData = cur.execute().body();
            } catch (Exception e) {
                return false;
            }

            //
            contentValues.put(DbContract.TransactionsTable.Columns.VALUE, value);
            contentValues.put(DbContract.TransactionsTable.Columns.CURRENCY, currency);
            switch (currency) {
                case Currencies.CURRENCY_RUB:
                    contentValues.put(DbContract.TransactionsTable.Columns.CURRENCY_VALUE, 100);
                    break;
                case Currencies.CURRENCY_DOLLAR:
                    contentValues.put(DbContract.TransactionsTable.Columns.CURRENCY_VALUE, gsonData.getValute().getUSD().getValue() * 100);
                    break;
                case Currencies.CURRENCY_EURO:
                    contentValues.put(DbContract.TransactionsTable.Columns.CURRENCY_VALUE, gsonData.getValute().getEUR().getValue() * 100);
                    break;
            }
            contentValues.put(DbContract.TransactionsTable.Columns.COMMENT, comment);

            if (mUri == null) {
                contentValues.put(DbContract.TransactionsTable.Columns.ACCOUNT_ID, accountId);

                getContentResolver().insert(DbContract.TransactionsTable.CONTENT_URI, contentValues);
            } else {
                getContentResolver().update(mUri, contentValues, null, null);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Для работы с другими валютами необходимо подключение к интернету", Toast.LENGTH_LONG).show();
            }

        }
    }
}
